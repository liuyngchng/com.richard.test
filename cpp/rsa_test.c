/**
 * gcc -g -o _rsa_test rsa_test.c `pkg-config --cflags --libs openssl`
 */
#include <openssl/rsa.h>
#include <openssl/evp.h>
#include <openssl/pem.h>
#include <openssl/err.h>
#include <stdio.h>
#include <stdlib.h>
 
//int gen_key() {
//    RSA *rsa = RSA_generate_key(2048, RSA_F4, NULL, NULL);
//    if (!rsa) {
//        printf("Error generating RSA key pair\n");
//        return -1;
//    }
//
//    // 写入私钥
//    BIO *bp_private = BIO_new_file("private.pem", "w+");
//    if (!bp_private) {
//        printf("Error creating file private.pem\n");
//        return -1;
//    }
//    PEM_write_bio_RSAPrivateKey(bp_private, rsa, NULL, NULL, 0, NULL, NULL);
//    BIO_free_all(bp_private);
//
//    // 写入公钥
//    BIO *bp_public = BIO_new_file("public.pem", "w+");
//    if (!bp_public) {
//        printf("Error creating file public.pem\n");
//        return -1;
//    }
//    PEM_write_bio_RSAPublicKey(bp_public, rsa);
//    BIO_free_all(bp_public);
//    RSA_free(rsa);
//
//    return 0;
//}

int main() {
    OpenSSL_add_all_algorithms();

    FILE *pub_fp = fopen("public.pem", "r");
    FILE *priv_fp = fopen("private.pem", "r");
    EVP_PKEY *pub_key = NULL;
    EVP_PKEY *priv_key = NULL;
    EVP_PKEY_CTX *pkey_ctx = NULL;
    size_t len = 0;
    unsigned char *buffer = NULL;
    int rv;

    // 读取公钥
    pub_key = PEM_read_PUBKEY(pub_fp, NULL, NULL, NULL);
    if (pub_key == NULL) {
        printf("公钥读取失败\n");
        goto end;
    }

    // 读取私钥
    priv_key = PEM_read_PrivateKey(priv_fp, NULL, NULL, NULL);
    if (priv_key == NULL) {
        printf("私钥读取失败\n");
        goto end;
    }
 
    // RSA加密
    pkey_ctx = EVP_PKEY_CTX_new(pub_key, NULL);
    EVP_PKEY_encrypt_init(pkey_ctx);
    EVP_PKEY_encrypt(pkey_ctx, NULL, &len, "Hello World", sizeof("Hello World"));
    buffer = malloc(len);
    EVP_PKEY_encrypt(pkey_ctx, buffer, &len, "Hello World", sizeof("Hello World"));
    printf("加密后的数据 (hex): ");
    for (int i = 0; i < len; i++) {
        printf("%02x", buffer[i]);
    }
    printf("\n");
 
    // RSA解密
    EVP_PKEY_decrypt_init(pkey_ctx);
    EVP_PKEY_decrypt(pkey_ctx, NULL, &len, buffer, len);
    free(buffer);
    buffer = malloc(len);
    EVP_PKEY_decrypt(pkey_ctx, buffer, &len, buffer, len);
    printf("解密后的数据:\n");
    for (int i = 0; i < len; i++) {
		printf("%02x", buffer[i]);
	}
	printf("\n");
 
end:
    free(buffer);
    EVP_PKEY_free(pub_key);
    EVP_PKEY_free(priv_key);
    EVP_PKEY_CTX_free(pkey_ctx);
    fclose(pub_fp);
    fclose(priv_fp);
 
    return 0;
}
