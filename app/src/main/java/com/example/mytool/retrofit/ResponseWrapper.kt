package com.example.mytool.retrofit

/**
 * Created by whstywh on 2019/1/14.
 * description：网络响应失败/成功
 */
open class ResponseWrapper(
    var reason: String = "",
    var result: String = "",
    var resultCode: Int = 0
)