package com.example.mytool.netty

/**
 * Created by whstywh on 2019/4/29.
 * description：
 */
object ByteConvert {

    /*8字节 long -> 数组*/
    fun longToBytes(n: Long): ByteArray {
        val b = ByteArray(8)
        b[0] = (n and 0xff).toByte()
        b[1] = (n shr 8 and 0xff).toByte()
        b[2] = (n shr 16 and 0xff).toByte()
        b[3] = (n shr 24 and 0xff).toByte()
        b[4] = (n shr 32 and 0xff).toByte()
        b[5] = (n shr 40 and 0xff).toByte()
        b[6] = (n shr 48 and 0xff).toByte()
        b[7] = (n shr 56 and 0xff).toByte()
        return b
    }

    /*8字节 数组 -> long*/
    fun bytesToLong(b: ByteArray): Long {
        return (b[7].toLong() and 0xff shl 56) or
                (b[6].toLong() and 0xff shl 48) or
                (b[5].toLong() and 0xff shl 40) or
                (b[4].toLong() and 0xff shl 32) or
                (b[3].toLong() and 0xff shl 24) or
                (b[2].toLong() and 0xff shl 16) or
                (b[1].toLong() and 0xff shl 8) or
                (b[0].toLong() and 0xff)
    }

    /*2字节 int -> 数组*/
    fun uintToBytes(n: Int): ByteArray {
        val b = ByteArray(2)
        b[0] = (n and 0xff).toByte()
        b[1] = (n shr 8 and 0xff).toByte()
        return b
    }

    /*2字节 数组 -> int*/
    fun bytesToUint(b: ByteArray): Int {
        return (b[1].toInt() and 0xff shl 8) or
                (b[0].toInt() and 0xff)
    }


    /*16进制字符 -> 字符串*/
    private fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }


    /*MD5加密后的32位16进制字符串 转换为 16字节byte数组*/
    fun hexStringToBytes(hexString: String?): ByteArray? {
        return hexString?.toUpperCase()?.let {
            val length = it.length / 2
            val hexChars = it.toCharArray()
            val bytes = ByteArray(length)
            for (i in 0 until length) {
                val pos = i * 2
                bytes[i] = (charToByte(hexChars[pos]).toInt() shl 4 or charToByte(hexChars[pos + 1]).toInt()).toByte()
            }
            bytes
        }
    }

    /*16字节byte数组 转换 MD5加密后的32位16进制字符串*/
    fun bytesToHexString(src: ByteArray?): String? {
        val stringBuilder = StringBuilder("")
        return src?.let {
            if (it.isEmpty()) {
                return@let null
            }
            for (i in it.indices) {
                val v = it[i].toInt() and 0xFF
                val hv = Integer.toHexString(v)
                if (hv.length < 2) {
                    stringBuilder.append(0)
                }
                stringBuilder.append(hv)
            }
            stringBuilder.toString()
        }
    }

}