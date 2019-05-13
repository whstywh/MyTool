cmake_minimum_required(VERSION 3.4.1)


# 配置头文件路径
include_directories(src/main/cpp/include/${ANDROID_ABI}/)


add_library( native-lib

             SHARED

             src/main/cpp/native-lib.cpp
             src/main/cpp/MyAES.cpp)

add_library(crypto STATIC IMPORTED)
set_target_properties(crypto
  PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/openssl/libcrypto.a)

add_library(ssl STATIC IMPORTED)
set_target_properties(ssl
  PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/openssl/libssl.a)


find_library( log-lib
              log )


target_link_libraries( native-lib

                       ssl
                       crypto

                       ${log-lib} )
