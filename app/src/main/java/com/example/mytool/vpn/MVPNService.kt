package com.example.mytool.vpn

import android.content.Intent
import android.net.VpnService
import com.example.mytool.netty.MUDPService
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by whstywh on 2019/3/14.
 * description：vpn服务
 */
class MVPNService : VpnService(), Runnable {

    var isRunning = false
    var vpnThread: Thread? = null
    private var byteArray: ByteArray = ByteArray(20000) { 0 }
    private var mIPHeader: IPHeader
    var upSpeed = 0
    var downSpeed = 0


    private object Holder {
        val INSTANCE = MVPNService()
    }

    companion object {
        var ous: FileOutputStream? = null
        val instance = Holder.INSTANCE
    }

    init {
        mIPHeader = IPHeader(byteArray, 0)
    }

    //UDP数据返回
    fun responseUdpPacket(b: ByteArray?) {
        try {
            b?.let {
                //下行速率统计
                downSpeed += it.size
                ous?.write(it, 0, it.lastIndex)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreate() {
        vpnThread = Thread(this, "MVPService_thread")
        vpnThread?.start()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /*通知栏设置断开连接时回调*/
    override fun onRevoke() {
        super.onRevoke()
        isRunning = false
    }

    override fun run() {
        runVpn()
    }

    private fun runVpn() {

        val pfd = Builder().let {
            //虚拟网络端口的最大传输单元，如果发送的包长度超过这个数字，则会被分包,一般设为1500
            it.setMtu(1500)
            //虚拟网络端口的IP地址
            it.addAddress("10.8.0.2", 32)
            //路由,如果是0.0.0.0/0的话，则会将所有的IP包都路由到虚拟端口上
            it.addRoute("0.0.0.0", 0)
            //该端口的DNS服务器地址
            it.addDnsServer("114.114.114.114")
            //建立的VPN连接的名字，会在系统管理的与VPN连接相关的通知栏和对话框中显示出来
            it.setSession("test")
            return@let it.establish()
        }

        pfd.fileDescriptor.let {
            ous = FileOutputStream(it)
            val ins = FileInputStream(it)
            var size = 0
            ins.use { input ->
                ous.use {
                    while (size != -1 && isRunning) {
                        while (ins.read(byteArray).also { size = it } > 0 && isRunning) {
                            if (MUDPService.instance?.isStop() == true) {
                                isRunning = false
                            }
                            onIpPacketUdpSend(mIPHeader, size)
                        }
                        Thread.sleep(20)
                    }
                }
            }
        }
        closeVpn()

    }


    private fun onIpPacketUdpSend(mIPHeader: IPHeader, size: Int) {
        //上行速率统计
        upSpeed += size
        val data = mIPHeader.m_Data.slice(0 until size).toByteArray()

        //UDP发送数据
//        MUDPService.instance?.send(pathGuid, len, data)
    }


    private fun closeVpn() {
        isRunning = false
//        MUDPService.instance?.stop()
        stopSelf()
    }

}
