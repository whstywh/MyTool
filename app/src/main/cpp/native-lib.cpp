#include <jni.h>
#include <string>
#include "MyAES.h"

using std::string;


/**
 * AES加密算法
 */
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_mytool_ndk_Jni_AESEncrypt(JNIEnv *env, jobject instance, jbyteArray data_src) {

    //计算jbyteArray长度
    int src_len = env->GetArrayLength (data_src);
    //jbyteArray -> unsigned char*
    auto * char_src = new unsigned char[src_len];
    env->GetByteArrayRegion (data_src, 0, src_len, reinterpret_cast<jbyte*>(char_src));

    auto * data_dest = new unsigned char[65536];
    //aes加密
    int dest_len = MyAES::AESEncrypt(char_src,data_dest,src_len);

    //unsigned char* -> jbyteArray
    jbyteArray result = env->NewByteArray(dest_len);
    env->SetByteArrayRegion(result,0,dest_len,(jbyte*)data_dest);
    //释放内存资源
    env->ReleaseByteArrayElements(data_src,(jbyte*)char_src, 0);
    return result;
}




/**
 * AES解密算法
 */
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_mytool_ndk_Jni_AESDecrypt(JNIEnv *env, jobject instance, jbyteArray data_src) {

    int src_len = env->GetArrayLength (data_src);
    auto * char_src = new unsigned char[src_len];
    env->GetByteArrayRegion (data_src, 0, src_len, reinterpret_cast<jbyte*>(char_src));

    auto * data_dest = new unsigned char[65536];
    int dest_len = MyAES::AESDecrypt(char_src,data_dest,src_len);

    jbyteArray result = env->NewByteArray(dest_len);
    env->SetByteArrayRegion(result,0,dest_len,(jbyte*)data_dest);

    env->ReleaseByteArrayElements(data_src,(jbyte*)char_src, 0);
    return result;
}

