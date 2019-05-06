package com.example.mytool.netty


/**
 * Created by whstywh on 2019/3/18.
 * description；自定义协议udp数据包 封装类
 */

class UdpEntity(
    var type: Int,
    var len: Int,
    var data: ByteArray
) {

    fun getBytes(): ByteArray? {

        val len = 36 + data.size

        val da = ByteArray(len) { 0 }

        val tb = ByteConvert.uintToBytes(type)

        for (t in tb.indices) {
            da[t] = tb[t]
        }
        val lb = ByteConvert.uintToBytes(len)

        for (l in lb.indices) {
            da[l + 2] = lb[l]
        }

        for (i in data.indices) {
            da[i + 4] = data[i]
        }

        return da
    }
}
