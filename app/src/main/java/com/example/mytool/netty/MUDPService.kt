package com.example.mytool.netty


import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleStateHandler
import java.net.InetSocketAddress
import java.nio.channels.DatagramChannel
import java.util.concurrent.TimeUnit


/**
 * Created by whstywh on 2019/3/18.
 * description：netty udp服务
 */
class MUDPService(var address: InetSocketAddress) {

    private var group: EventLoopGroup = NioEventLoopGroup()
    private var bootstrap: Bootstrap = Bootstrap()
    private var datagramChannel: DatagramChannel? = null
    private var channel: Channel? = null
    private var isStop = true

    companion object {
        @Volatile
        var instance: MUDPService? = null

        fun getI(address: InetSocketAddress) =
            instance ?: synchronized(MUDPService::class.java) {
                instance ?: MUDPService(address).also { instance = it }
            }
    }

    fun bind() {
        try {
            isStop = false
            channel = bootstrap.bind(0).sync().channel()
            //监听服务器关闭
            channel?.closeFuture()?.await()
        } catch (e: Exception) {
        } finally {
//            stop()
        }
    }

    init {

        try {
            /**
             * Netty创建全部都是实现自AbstractBootstrap。
             * 客户端的是Bootstrap，服务端的则是ServerBootstrap。
             **/
            bootstrap.group(group)
                    //外部创建DatagramChannel，为了实现 VPNService.protect(datagramChannel)
                .channelFactory(object : ChannelFactory<NioDatagramChannel> {
                    override fun newChannel(): NioDatagramChannel {
                        datagramChannel = DatagramChannel.open()
                        return NioDatagramChannel(datagramChannel)
                    }
                })
//                .channel(NioDatagramChannel::class.java)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(object : ChannelInitializer<NioDatagramChannel>() {
                    override fun initChannel(p0: NioDatagramChannel?) {
                        p0?.pipeline()?.also {
                            it.addLast(EntityEncoder(address))
                            //以实现对三种心跳的检测，分别是:
                            //1）readerIdleTime：为读超时时间（即测试端一定时间内未接受到被测试端消息）;
                            //2）writerIdleTime：为写超时时间（即测试端一定时间内向被测试端发送消息）
                            //3）allIdleTime：所有类型的超时时间;
                            it.addLast(IdleStateHandler(0, 4, 0, TimeUnit.SECONDS))
                            it.addLast(EntityHandler())
                        }
                    }
                })
        } catch (e: Exception) {
            stop()
        }
    }


    fun send(len: Int, data: ByteArray) {
        try {
//            if (MVPNService.instance.protect(datagramChannel?.socket())) {
            val entity = UdpEntity(
                type = 0,
                len = len,
                data = data
            )
            channel?.writeAndFlush(entity)?.sync()
//            }
        } catch (e: Exception) {
        }
    }

    fun stop() {
        isStop = true
        channel?.close()
        datagramChannel?.close()
        //关闭EventLoopGroup，释放掉所有资源包括创建的线程
        group.shutdownGracefully().sync()
        instance = null
    }

    fun isStop() = isStop
}

/*TODO: 发送数据
*
* 编码: UdpEntity -> DatagramPacket
* */
class EntityEncoder(private var remoteAddress: InetSocketAddress) : MessageToMessageEncoder<UdpEntity>() {

    override fun encode(p0: ChannelHandlerContext?, p1: UdpEntity?, p2: MutableList<Any>?) {

        p0?.let {
            val byteBuf = it.alloc()?.buffer()
            p1?.getBytes()?.let {

                //省略数据处理过程（加密...）

                byteBuf?.writeBytes(it, 0, it.size)
            }?.let {
                p2?.add(DatagramPacket(it, remoteAddress))
            }
        }
    }
}


class EntityHandler : SimpleChannelInboundHandler<DatagramPacket>() {


    //断开连接时回调，可进行重连操作
    override fun channelInactive(ctx: ChannelHandlerContext?) {
//        MUDPService.instance?.bind()
    }


    //处理心跳超时事件,IdleStateHandler设置超时时间，条件触发时回调
    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if (evt is IdleStateEvent) {
            val state = evt.state()
            // 当空闲事件为写事件时向服务器发送心跳包
            if (state == IdleState.WRITER_IDLE) {
                val time = System.currentTimeMillis()
                val timeBytes = ByteConvert.longToBytes(time)
                val e = UdpEntity(
                    type = 2,
                    len = 8 + 4,
                    data = timeBytes
                )
                ctx?.writeAndFlush(e)
            } else {
                super.userEventTriggered(ctx, evt)
            }
        }
    }

    /*
    * TODO: 接受数据
    *
    * 业务逻辑处理
    * */
    override fun channelRead0(p0: ChannelHandlerContext?, p1: DatagramPacket?) {

        //省略数据处理（解密...）

    }

}


