#include <openssl/aes.h>
#include "MyAES.h"


int MyAES::AESEncrypt(unsigned char *data_source, unsigned char *data_dest, uint16_t source_len) {

    AES_KEY aes_key;


    unsigned char key[32] = {0x61, 0x6D, 0x70, 0x74, 0x68, 0x6F, 0x6E, 0x00,
                             0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                             0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                             0x00, 0x00};


    if (AES_set_encrypt_key((const unsigned char *) key, 256, &aes_key) < 0) {
        return 0;
    }

    int encrypt_len = ((source_len + AES_BLOCK_SIZE - 1) / AES_BLOCK_SIZE) * AES_BLOCK_SIZE;
    int encrypted_len = 0;
    while (encrypted_len < encrypt_len) {
        AES_encrypt(data_source, data_dest, &aes_key);
        encrypted_len += AES_BLOCK_SIZE;
        data_source += AES_BLOCK_SIZE;
        data_dest += AES_BLOCK_SIZE;
    }

    return encrypt_len;

}


int MyAES::AESDecrypt(unsigned char *data_source, unsigned char *data_dest, uint16_t source_len) {

    AES_KEY aes_key;

    unsigned char key[32] = {0x61, 0x6D, 0x70, 0x74, 0x68, 0x6F, 0x6E, 0x00,
                             0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                             0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                             0x00, 0x00};

    if (AES_set_decrypt_key((const unsigned char *) key, 256, &aes_key) < 0) {
        return 0;
    }

    int encrypted_len = 0;
    while (encrypted_len < source_len) {
        AES_decrypt(data_source, data_dest, &aes_key);
        encrypted_len += AES_BLOCK_SIZE;
        data_source += AES_BLOCK_SIZE;
        data_dest += AES_BLOCK_SIZE;
    }
    return source_len;

}


