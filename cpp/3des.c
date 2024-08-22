/*
 * 3des.c
 *
 *  Created on: Aug 21, 2024
 *      Author: rd
 */

#include<stdio.h>
#include<string.h>
#include<stdlib.h>
/**
 * 定义枚举型全局变量
 */
typedef enum {
    false = 0,
    true = 1
} bool;

/**
 * 十六轮子密钥
 */
static bool sub_key[16][48] = {0};

/*---------------------*/
/*-------------------------------------------------------------
     各种置换表
-------------------------------------------------------------*/
/**
 * IP置换表
 */
const char ip_tbl[64] = {
	58,50,42,34,26,18,10, 2,60,52,44,36,28,20,12, 4,
	62,54,46,38,30,22,14, 6,64,56,48,40,32,24,16, 8,
	57,49,41,33,25,17, 9, 1,59,51,43,35,27,19,11, 3,
	61,53,45,37,29,21,13, 5,63,55,47,39,31,23,15, 7
};

/**
 * IP-1置换表
 */
const char ipr_tbl[64] = {
	40, 8,48,16,56,24,64,32,39, 7,47,15,55,23,63,31,
	38, 6,46,14,54,22,62,30,37, 5,45,13,53,21,61,29,
	36, 4,44,12,52,20,60,28,35, 3,43,11,51,19,59,27,
	34, 2,42,10,50,18,58,26,33, 1,41, 9,49,17,57,25
};

/**
 * E扩展表
 */
static char e_tbl[48] = {
	32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9,
	8, 9,10,11,12,13,12,13,14,15,16,17,
    16,17,18,19,20,21,20,21,22,23,24,25,
    24,25,26,27,28,29,28,29,30,31,32, 1
};

/**
 * PC1置换表
 */
static char pc1_tbl[56] = {
	57,49,41,33,25,17, 9, 1,58,50,42,34,26,18,
	10, 2,59,51,43,35,27,19,11, 3,60,52,44,36,
	63,55,47,39,31,23,15, 7,62,54,46,38,30,22,
	14, 6,61,53,45,37,29,21,13, 5,28,20,12, 4
};

/**
 * pc2表
 */
static char pc2_tbl[48] = {
	14,17,11,24, 1, 5, 3,28,15, 6,21,10,
	23,19,12, 4,26, 8,16, 7,27,20,13, 2,
	41,52,31,37,47,55,30,40,51,34,33,48,
	44,49,39,56,34,53,46,42,50,36,29,32
};

/**
 * 移位表
 */
static char mv_tbl[16] = {
	1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
};

/**
 * S盒
 */
static char s_box[8][4][16] = {
    //S1
    14, 4,13, 1, 2,15,11, 8, 3,10, 6,12, 5, 9, 0, 7,
     0,15, 7, 4,14, 2,13, 1,10, 6,12,11, 9, 5, 3, 8,
     4, 1,14, 8,13, 6, 2,11,15,12, 9, 7, 3,10, 5, 0,
    15,12, 8, 2, 4, 9, 1, 7, 5,11, 3,14,10, 0, 6,13,
    //S2
    15, 1, 8,14, 6,11, 3, 4, 9, 7, 2,13,12, 0, 5,10,
     3,13, 4, 7,15, 2, 8,14,12, 0, 1,10, 6, 9,11, 5,
     0,14, 7,11,10, 4,13, 1, 5, 8,12, 6, 9, 3, 2,15,
    13, 8,10, 1, 3,15, 4, 2,11, 6, 7,12, 0, 5,14, 9,
    //S3
    10, 0, 9,14, 6, 3,15, 5, 1,13,12, 7,11, 4, 2, 8,
    13, 7, 0, 9, 3, 4, 6,10, 2, 8, 5,14,12,11,15, 1,
    13, 6, 4, 9, 8,15, 3, 0,11, 1, 2,12, 5,10,14, 7,
     1,10,13, 0, 6, 9, 8, 7, 4,15,14, 3,11, 5, 2,12,
    //S4
     7,13,14, 3, 0, 6, 9,10, 1, 2, 8, 5,11,12, 4,15,
    13, 8,11, 5, 6,15, 0, 3, 4, 7, 2,12, 1,10,14, 9,
    10, 6, 9, 0,12,11, 7,13,15, 1, 3,14, 5, 2, 8, 4,
     3,15, 0, 6,10, 1,13, 8, 9, 4, 5,11,12, 7, 2,14,
     //S5
     2,12, 4, 1, 7,10,11, 6, 8, 5, 3,15,13, 0,14, 9,
    14,11, 2,12, 4, 7,13, 1, 5, 0,15,10, 3, 9, 8, 6,
     4, 2, 1,11,10,13, 7, 8,15, 9,12, 5, 6, 3, 0,14,
    11, 8,12, 7, 1,14, 2,13, 6,15, 0, 9,10, 4, 5, 3,
    //S6
    12, 1,10,15, 9, 2, 6, 8, 0,13, 3, 4,14, 7, 5,11,
    10,15, 4, 2, 7,12, 0, 5, 6, 1,13,14, 0,11, 3, 8,
     9,14,15, 5, 2, 8,12, 3, 7, 0, 4,10, 1,13,11, 6,
     4, 3, 2,12, 9, 5,15,10,11,14, 1, 7, 6, 0, 8,13,
    //S7
     4,11, 2,14,15, 0, 8,13, 3,12, 9, 7, 5,10, 6, 1,
    13, 0,11, 7, 4, 0, 1,10,14, 3, 5,12, 2,15, 8, 6,
     1, 4,11,13,12, 3, 7,14,10,15, 6, 8, 0, 5, 9, 2,
     6,11,13, 8, 1, 4,10, 7, 9, 5, 0,15,14, 2, 3,12,
    //S8
    13, 2, 8, 4, 6,15,11, 1,10, 9, 3,14, 5, 0,12, 7,
     1,15,13, 8,10, 3, 7, 4,12, 5, 6,11, 0,14, 9, 2,
     7,11, 4, 1, 9,12,14, 2, 0, 6,10,13,15, 3, 5, 8,
     2, 1,14, 7, 4,10, 8,13,15,12, 9, 0, 3, 5, 6,11
};

/**
 * P置换表
 */
static char p_tbl[32] = {
	16, 7,20,21,29,12,28,17, 1,15,23,26, 5,18,31,10,
	 2, 8,24,14,32,27, 3, 9,19,13,30, 6,22,11, 4,25
};

/**
 * 生成16轮的子密钥
 */
void set_key(char my_key[8]);

/**
 * 字节转换成位
 */
void byte_to_bit(bool *const data_out, const char* data_in, const int num);


/**
 * 位转换成字节
 */
void bit_to_byte(char my_msg[8], const bool *msg_in, const int num);

/**
 * 各种表的置换算法
 */
void tbl_replace(bool *const data_out, const bool *data_in,
	const char* table, const int num);

/**
 * 二进制数组的拷贝
 */
void bit_cpy(bool *const data_out, const bool *data_in, const int num);

/**
 * 左移位
 */
void loop_bit(bool *const data_out, const int mv_step, const int len);

/**
 * DES 加密
 */
void encrypt(const char plain[8], char cypher[16]);

/**
 * 异或操作
 */
void xor(bool *const msg_out, const bool* msg_in, const int num);

/**
 * S盒变换
 */
void s_change(bool *data_out, const bool *data_in);

/**
 * 十六进制转二进制
 */
void hex_to_bit(bool *const data_out, const char* data_in, const int num);

/**
 * 二进制转换成十六进制
 */
void bit_to_hex(char *const data_out, const bool *data_in, const int num);

/**
 * DES轮解密算法
 */
void decrypt(char plain[8], const char cypher[16]);



/**
 * for test purpose only
 */
int test() {
    int i = 0, j;
    char enc_key[8] = {0};  	//	记录加密密钥；
    char dec_key[8] = {0}; 		//	解密密钥
    char plain[9] 	= {0}; 		//	明文， 8 字节
    char cypher[17] = {0};		// 	16进制的密文, 16 字节
    char dec_plain[9] = {0};	//	解密后的明文, 8 字节
    printf("请输入加密内容(8 bytes):\n");
    scanf("%s", plain);
    printf("请输入密钥(8 bytes):\n");
    scanf("%s", enc_key);
    i = strlen(enc_key);
    while(i != 8) {
        printf("请输入密钥(8 Byte)\n");
        scanf("%s", enc_key);
        i = 0;
        i = strlen(enc_key);
    }
    set_key(enc_key);  				//	生成16轮的加密子密钥；
    encrypt(plain, cypher); 		//	des的轮加密过程
    printf("经过加密的密文为 %s:\n", cypher);
    printf("请输入你的解密密钥(8 Byte):\n");
    scanf("%s", dec_key);
    i = strlen(dec_key);
    while (i != 8) {
        printf("请输入解密密钥(8 Byte)\n");
        scanf("%s", dec_key);
        i = 0;
        i = strlen(dec_key);
    }
    set_key(dec_key);  				//	生成16轮的解密子密钥；
    decrypt(dec_plain, cypher);			//	解密;
    printf("解密结果为: %s\n", dec_plain);
    return 0;
}

int main() {
	char *plain = "hellodec";
	char *enc_key = "hellokey";
	char *cypher_str = "8656C6C6F6465636";
	char cypher[17] = {0};		// 	16进制的密文, 16 字节
	char dec_plain[9] = {0};	//	解密后的明文, 8 字节
	set_key(enc_key);  				//	生成16轮的加密子密钥；
	encrypt(plain, cypher);
	printf("加密 %s with key %s, get %s\n", plain, enc_key, cypher);
	decrypt(dec_plain, cypher_str);
	printf("解密 %s with key %s 结果为: %s\n", plain, enc_key, dec_plain);
//	test();
}


void bit_cpy(bool *const data_out, const bool* data_in, const int num) {
    int i = 0;
    for (i = 0; i < num; i++) {
        data_out[i] = data_in[i];
    }

}

void byte_to_bit(bool *const data_out, const char* data_in, const int num) {
    int i, j;
    for(i = 0; i < num; i++) {
        data_out[i] = (data_in[i / 8] >> (i % 8)) & 0x01;
    }
}

void bit_to_hex(char *const data_out, const bool *data_in, const int num) {
    int i;
    for (i = 0; i < num / 4; i++) {
        data_out[i] = 0;
    }
    for (i = 0; i < num / 4; i++) {
        data_out[i] = data_in[4 * i] + data_in[4 * i + 1] * 2
			+ data_in[4 * i + 2] * 4 + data_in[4 * i + 3] * 8;
        if (data_out[i] % 16 > 9) {
            data_out[i] = data_out[i] % 16 + '7';
        } else {
            data_out[i] = data_out[i] % 16 + '0';
        }
    }
}


void hex_to_bit(bool *const data_out, const char* data_in, const int num) {
    int i;
    for (i = 0; i < num; i++) {
        if (data_in[i / 4] <= '9') {
            data_out[i] = ((data_in[i / 4] - '0') >> (i % 4)) & 0x01;
        } else {
            data_out[i] = ((data_in[i / 4] - '7') >> (i % 4)) & 0x01;
        }
    }
}


void bit_to_byte(char my_msg[8], const bool* msg_in, const int num) {
    int i = 0;
    for (i = 0; i < (num / 8); i++) {
        my_msg[i] = 0;
    }
    for (i = 0; i < num; i++) {
        my_msg[i / 8] |= msg_in[i] << (i % 8);
    }
}


void tbl_replace(bool *const data_out, const bool* data_in,
	const char* table, const int num) {
    int i = 0;
    static bool tmp[256] = {0};
    for(i = 0; i < num; i++){
        tmp[i] = data_in[table[i] - 1];
    }
    bit_cpy(data_out, tmp, num);
}


void loop_bit(bool *const data_out, const int mv_step, const int len) {
    static bool tmp[256] = {0};
    bit_cpy(tmp, data_out, mv_step);
    bit_cpy(data_out, data_out + mv_step, len - mv_step);
    bit_cpy(data_out + len - mv_step, tmp, mv_step);
}


void xor(bool *const msg_out, const bool *msg_in, const int num) {
    int i;
    for (i = 0; i < num; i++) {
        msg_out[i] = msg_out[i] ^ msg_in[i];
    }
}


void set_key(char my_key[8]) {
    int i, j;
    static bool key_bit[64] = {0}; //Key的二进制缓存；
    static bool* key_bit_l, * key_bit_r;
    key_bit_l = &key_bit[0]; //key的左边28位；
    key_bit_r = &key_bit[28]; //key的右边28位；
    byte_to_bit(key_bit, my_key, 64);
    tbl_replace(key_bit, key_bit, pc1_tbl, 56);//pc-1 置换
    for(i = 0; i < 16; i++) {
        loop_bit(key_bit_l, mv_tbl[i], 28);
        loop_bit(key_bit_r, mv_tbl[i], 28);
        tbl_replace(sub_key[i], key_bit, pc2_tbl, 48);//pc-2置换
    }
}


void s_change(bool *data_out, const bool *data_in) {
    int i;
    int r = 0, c = 0;			//S盒的行和列；
    for (i = 0; i < 8; i++,
    data_in = data_in + 6, data_out = data_out + 4) {
        r = data_in[0] * 2 + data_in[5] * 1;
        c = data_in[1] * 8 + data_in[2] * 4
			+ data_in[3] * 2 + data_in[4] * 1;
        byte_to_bit(data_out, &s_box[i][r][c], 4);
    }
}


void f_change(bool data_out[32], const bool data_in[48]) {
    int i;
    static bool msg_e[48] = {0};
    tbl_replace(msg_e, data_out, e_tbl, 48);
    xor(msg_e, data_in, 48);
    s_change(data_out, msg_e);
    tbl_replace(data_out, data_out, p_tbl, 32);
}


void encrypt(const char plain[8], char cypher[16]) {
    int i;
    static bool msg_bit[64] = {0};
    static bool* msg_bit_l = &msg_bit[0], * msg_bit_r = &msg_bit[32];
    static bool tmp[32] = {0};
    byte_to_bit(msg_bit, plain, 64);
    tbl_replace(msg_bit, msg_bit, ip_tbl, 64);
    for (i = 0; i < 16; i++) {
        bit_cpy(tmp, msg_bit_r, 32);
        f_change(msg_bit_r, sub_key[i]);
        xor(msg_bit_r, msg_bit_l, 32);
        bit_cpy(msg_bit_l, tmp, 32);
    }
    tbl_replace(msg_bit, msg_bit, ipr_tbl, 64);
    bit_to_hex(cypher, msg_bit, 64);
}


void decrypt(char plain[8], const char cypher[16]) {
    int i = 0;
    static bool msg_bit[64] = {0};
    static bool* msg_bit_l = &msg_bit[0], * msg_bit_r = &msg_bit[32];
    static bool tmp[32] = {0};
    hex_to_bit(msg_bit, cypher, 64);
    tbl_replace(msg_bit, msg_bit, ip_tbl, 64);
    for(i = 15; i >= 0; i--) {
        bit_cpy(tmp, msg_bit_l, 32);
        f_change(msg_bit_l, sub_key[i]);
        xor(msg_bit_l, msg_bit_r, 32);
        bit_cpy(msg_bit_r, tmp, 32);
    }
    tbl_replace(msg_bit, msg_bit, ipr_tbl, 64);
    bit_to_byte(plain, msg_bit, 64);
}
