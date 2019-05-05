package com.example.mytool.retrofit

import io.reactivex.Maybe
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by whstywh on 2019/1/14.
 * description：接口管理类
 */
interface ApiService {

    @POST("client/login")
    fun login(@Body requestBody: RequestBody): Maybe<LoginEntity>

}