package com.example.mytool.retrofit

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * Created by whstywh on 2019/2/20.
 * description：
 * 此类是用于主机名验证。
 * 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，则验证机制可以回调此接口的实现来确定程序是否应该允许此连接。
 * return true : 放弃hostname的校验，降低了安全性
 */
class TrustAllHostnameVerifier : HostnameVerifier {
    override fun verify(hostname: String, session: SSLSession): Boolean {
        return true
    }
}