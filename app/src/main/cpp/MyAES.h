#ifndef RSADEMO_MYAES_H
#define RSADEMO_MYAES_H


#include <string>

class MyAES {


public:

   static int AESEncrypt(uint8_t *data_source,uint8_t *data_dest,uint16_t source_len);

   static int AESDecrypt(uint8_t *data_source,uint8_t *data_dest,uint16_t source_len);
};


#endif
