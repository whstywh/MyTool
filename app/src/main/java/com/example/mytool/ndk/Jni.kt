package com.example.mytool.ndk

/**
 * Created by whstywh on 2019/4/23.
 * description：
 */
class Jni {

    companion object {

        init {
            System.loadLibrary("native-lib")
        }
    }


    //Java_com_example_mytool_ndk_Jni_AESEncrypt
    external fun AESEncrypt(msg: ByteArray): ByteArray

    //Java_com_example_mytool_ndk_Jni_AESDecrypt
    external fun AESDecrypt(msg: ByteArray): ByteArray

}
