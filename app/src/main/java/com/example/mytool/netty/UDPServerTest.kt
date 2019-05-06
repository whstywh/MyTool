import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import java.io.IOException
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.channels.DatagramChannel

/**
 * Created by whstywh on 2019/3/29.
 * description：服务端 udp测试服务
 */
class UDPServerTest {

    private var group: EventLoopGroup = NioEventLoopGroup()
    private var bootstrap: Bootstrap = Bootstrap()
    private var channel: Channel? = null
    private var datagramChannel: DatagramChannel? = null


    companion object {
        @Volatile
        var instance: UDPServerTest? = null

        fun getI() =
            instance ?: synchronized(UDPServerTest::class.java) {
                instance ?: UDPServerTest().also { instance = it }
            }
    }

    fun start() {
        channel = bootstrap.bind(InetAddress.getLocalHost(), 8888).sync().channel()
        channel?.closeFuture()?.await()
    }

    init {
        try {
            bootstrap.group(group)
                .channelFactory(object : ChannelFactory<NioDatagramChannel> {
                    override fun newChannel(): NioDatagramChannel {
                        datagramChannel = DatagramChannel.open()
                        return NioDatagramChannel(datagramChannel)
                    }
                })
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(object : ChannelInitializer<NioDatagramChannel>() {
                    override fun initChannel(p0: NioDatagramChannel?) {
                        p0?.pipeline()?.also {
                            it.addLast(EntityTestHandler(datagramChannel?.socket()))
                        }
                    }
                })

        } catch (e: IOException) {
            stop()
        }
    }


    fun stop() {
        channel?.close()
        datagramChannel?.close()
        group.shutdownGracefully().sync()
        instance = null
    }
}


class EntityTestHandler(datagramSocket: DatagramSocket?) : SimpleChannelInboundHandler<DatagramPacket>() {

    val datas = datagramSocket

    /*TODO: 接受*/
    override fun channelRead0(p0: ChannelHandlerContext?, p1: DatagramPacket?) {

//        if (MVPNService.instance.protect(datas)) {
            p0?.channel()?.writeAndFlush(
                DatagramPacket(
                    p1?.content()?.writeBytes("udpservertest->udpClient".toByteArray()),
                    p1?.sender()
                )
            )
//        }
    }

}
