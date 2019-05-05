package com.example.mytool.retrofit

import com.example.mytool.retrofit.RequestCallback.Status.resultCode
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

/**
 * Created by whstywh on 2019/1/14.
 * description：rxjava 订阅回调封装
 */
abstract class RequestCallback<T : ResponseWrapper> : MaybeObserver<T> {

    abstract fun success(t: T)
    abstract fun faiilure(e: ResponseWrapper)

    private object Status {
        const val resultCode = 0
    }

    override fun onSuccess(t: T) {
        if (t.resultCode == resultCode) {
            success(t)
            return
        }
        faiilure(ResponseWrapper(t.result, t.reason, t.resultCode))
    }


    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {

        faiilure(ResponseWrapper("", e.message.toString(), -1))
    }
}