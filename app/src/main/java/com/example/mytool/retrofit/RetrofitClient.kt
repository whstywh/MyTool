package com.example.mytool.retrofit

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Created by whstywh on 2019/1/11.
 * description：retrofit客户端
 */
class RetrofitClient private constructor() {

    lateinit var apiService: ApiService
    val TIMEOUT: Long = 5//超时时间 5秒

    private object Holder {
        val INSTANCE = RetrofitClient()
    }

    companion object {
        val instance by lazy { Holder.INSTANCE }

        lateinit var serverInputStream: InputStream
        lateinit var clientInputStream: InputStream
        const val pass: String = ".pfx格式证书密码，用于得到客户端私钥"

        fun set(context: Context) {
            serverInputStream = context.assets.open("server.cer")
            clientInputStream = context.assets.open("client.pfx")
        }
    }


    //BaseApplication -> onCreate() 初始化
    fun init() {

        /*TODO: https 双向认证*/
        clientInputStream.use {
            serverInputStream.use {

                //设置服务端证书
                val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null)
                val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")

                val certificateAlias1 = 0.toString()
                keyStore.setCertificateEntry(
                    certificateAlias1,
                    certificateFactory.generateCertificate(serverInputStream)
                )
                val trustManagerFactory: TrustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(keyStore)
                val trustManagers = trustManagerFactory.trustManagers
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
//                    Logger.e("Unexpected default trust managers:" + Arrays.toString(trustManagers))
                    return
                }
                val trustManager = trustManagers[0] as X509TrustManager

                //设置客户端证书
                //PKCS12-->.pfx || BKS --> .bks
                val clientkeyStore = KeyStore.getInstance("PKCS12")
                clientkeyStore.load(clientInputStream, pass.toCharArray())
                val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                keyManagerFactory.init(clientkeyStore, pass.toCharArray())

                //构建ssl上下文
                val sslContext: SSLContext = SSLContext.getInstance("TLS")
                //添加受信任的证书和生成随机数
                sslContext.init(
                    keyManagerFactory.keyManagers,
                    arrayOf(trustManager),
                    SecureRandom()
                )

                val okHttpClient = OkHttpClient().newBuilder()
                    .sslSocketFactory(sslContext.socketFactory, trustManager)
                    //信任当前证书设置
                    .hostnameVerifier(TrustAllHostnameVerifier())
                    .addInterceptor(LoggingInterceptor())
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://45.76.143.175:3443/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()

                apiService = retrofit.create(ApiService::class.java)

            }
        }
    }

}



