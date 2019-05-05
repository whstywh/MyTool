package com.example.mytool.retrofit

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by whstywh on 2019/2/20.
 * description：log拦截器
 */
class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

//        Logger.d(content)

        val response = chain.proceed(request)
        val mediaType = response.body()?.contentType()
        val content = response.body()?.string() ?: "null"

//        Logger.json(content)

        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build()
    }
}