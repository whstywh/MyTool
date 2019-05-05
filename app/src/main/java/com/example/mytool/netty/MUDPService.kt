package com.example.mytool.netty


import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleStateHandler
import java.net.InetSocketAddress
import java.util.*





/**
 * Created by whstywh on 2019/3/18.
 * description：udp服务
 */
class MUDPService(var address: InetSocketAddress) {
//
//    private var group: EventLoopGroup = NioEventLoopGroup()
//    private var bootstrap: Bootstrap = Bootstrap()
//    private var datagramChannel: DatagramChannel? = null
//    private var channel: Channel? = null
//    private var isStop = true
////    private var cGuid = HardwareInfoU.instance?.getClientGuid()
//    private var pGuid = ""
//
//    companion object {
//        @Volatile
//        var instance: MUDPService? = null
//
//        fun getI(address: InetSocketAddress) =
//            instance ?: synchronized(MUDPService::class.java) {
//                instance ?: MUDPService(address).also { instance = it }
//            }
//    }
//
//    fun bind() {
//        try {
//            isStop = false
//            channel = bootstrap.bind(0).sync().channel()
//            channel?.closeFuture()?.await()
//        } catch (e: Exception) {
//            LoggerU.LogError(e.message.toString())
//        } finally {
////            stop()
//        }
//    }
//
//    init {
//
//        try {
//            bootstrap.group(group)
//                .channelFactory(object : ChannelFactory<NioDatagramChannel> {
//                    override fun newChannel(): NioDatagramChannel {
//                        datagramChannel = DatagramChannel.open()
//                        return NioDatagramChannel(datagramChannel)
//                    }
//                })
//                .option(ChannelOption.SO_BROADCAST, true)
//                .handler(object : ChannelInitializer<NioDatagramChannel>() {
//                    override fun initChannel(p0: NioDatagramChannel?) {
//                        p0?.pipeline()?.also {
//                            it.addLast(EntityEncoder(address))
//                            it.addLast(IdleStateHandler(0, 4, 0, TimeUnit.SECONDS))
//                            it.addLast(EntityHandler(cGuid, object : TransmitPathGuidListener {
//                                override fun transmitPathGuid(): String {
//                                    return pGuid
//                                }
//                            }))
//                        }
//                    }
//                })
//        } catch (e: Exception) {
//            stop()
//        }
//    }
//
//
//    fun send(pathGuid: String, len: Int, data: ByteArray) {
//        try {
//            if (MVPNService.instance.protect(datagramChannel?.socket())) {
//                pGuid = pathGuid
//                val entity = cGuid?.let {
//                    UdpEntity(
//                        client_guid = it,
//                        route_guid = pathGuid,
//                        type = 0,
//                        len = len,
//                        data = data
//                    )
//                }
//                channel?.writeAndFlush(entity)?.sync()
//            }
//        } catch (e: Exception) {
//            LoggerU.LogError(e.message.toString())
//        }
//    }
//
//    fun stop() {
//        isStop = true
//        channel?.close()
//        datagramChannel?.close()
//        group.shutdownGracefully().sync()
//        instance = null
//    }
//
//    fun isStop() = isStop
//}
//
///*TODO: 发送*/
///*UdpEntity -> 加密 -> datagramPacket*/
//class EntityEncoder(private var remoteAddress: InetSocketAddress) : MessageToMessageEncoder<UdpEntity>() {
//
//    private var jni: JNI = JNI()
//
//    override fun encode(p0: ChannelHandlerContext?, p1: UdpEntity?, p2: MutableList<Any>?) {
//
//        p0?.let {
//            val byteBuf = it.alloc()?.buffer()
//            p1?.getBytes()?.let {
//                //aes加密
//                val enBytearray = jni.AESEncrypt(it)
//                //aes加密后的最后一个字节与数据头部(前36字节)进行逐字节异或
//                for (i in 0 until 36) {
//                    enBytearray[i] = enBytearray[i] xor enBytearray.last()
//                }
//                //对udp发送长度随机增加小与16的数(实际上增加的最后一个字节& 0xf)
//                val l = enBytearray.size + (enBytearray.last() and 0xf)
//                byteBuf?.writeBytes(enBytearray, 0, enBytearray.size)
//            }?.let {
//                p2?.add(DatagramPacket(it, remoteAddress))
//            }
//        }
//    }
}
//
//
//class EntityHandler(cGuid: String?, t: TransmitPathGuidListener) :
//    SimpleChannelInboundHandler<DatagramPacket>() {
//
//    private var clientGuid = cGuid
//    private var jni: JNI = JNI()
//    private val tPListerner = t
//
//
//    //断开连接时 重连
//    override fun channelInactive(ctx: ChannelHandlerContext?) {
//
////        MUDPService.instance?.bind()
//    }
//
//
//    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
//        // 写事件空闲时向服务器发送心跳包
//        if (evt is IdleStateEvent) {
//            val state = evt.state()
//            if (state == IdleState.WRITER_IDLE) {
//                val time = System.currentTimeMillis()
//                val pathGuid = tPListerner.transmitPathGuid()
//                val timeBytes = ByteConvert.longToBytes(time)
//                val e = clientGuid?.let {
//                    UdpEntity(
//                        client_guid = it,
//                        route_guid = pathGuid,
//                        type = 2,
//                        len = 8 + 36,
//                        data = timeBytes
//                    )
//                }
//                ctx?.writeAndFlush(e)
//            } else {
//                super.userEventTriggered(ctx, evt)
//            }
//        }
//    }
//
//    /*TODO: 接受*/
//    override fun channelRead0(p0: ChannelHandlerContext?, p1: DatagramPacket?) {
//
//        p1?.content()?.let {
//            //将udp接收长度更改为正确长度,去除小于16字节的冗余尾部
//            val readable = it.readableBytes()
//            val bytes = it.array()
//                .filterIndexed { index, _ ->
//                    index < (readable / 16) * 16
//                }.toByteArray()
//            //使用最后一个字节与数据头部进行异或还原数据头部
//            for (i in 0 until 36) {
//                bytes[i] = bytes[i] xor bytes.last()
//            }
//            //aes解密
//            jni.AESDecrypt(bytes)
//        }?.let {
//            val type = ByteConvert.bytesToUint(it.slice(32..33).toByteArray())
//            val data = it.slice(36..it.lastIndex).toByteArray()
//            when (type) {
//                0 -> {
//                    MVPNService.instance.responseUdpPacket(data)
//                }
//                3 -> {
//                    val hb = formatterDate(ByteConvert.bytesToLong(data))
//                    LoggerU.LogDebug("心跳回复包:$hb")
//                }
//                else -> {
//                    LoggerU.LogError("线路无法使用!")
//                }
//            }
//        }
//
//    }
//
//    /*系统时间 格式化*/
//    private fun formatterDate(long: Long): String {
//        val date = Date(long)
//        val df = DateFormat.getDateTimeInstance(DateFormat.YEAR_FIELD, DateFormat.ERA_FIELD, Locale("zh", "CN"))
//        return df.format(date)
//    }
//
//}
//
///*接口回调 传递pathGuid*/
//interface TransmitPathGuidListener {
//    fun transmitPathGuid(): String
//}


