/**
 * gcc -g -o _rsa_deploy rsa_deploy.c
 */
#include<stdio.h>
#include<string.h>
#include <stdlib.h>

// config data
#define SIZE 20
#define D 537653
#define N 968087

static char *decrypt_dt(int d, int n, const int *cypher);

/**
 * 解密数据调用的函数
 */
char *dec(const int *cypher) {
	return decrypt_dt(D, N, cypher);
}


static char *decrypt_dt(int d, int n, const int *cypher) {
	unsigned long dec_num[SIZE];
	static char dec_str[SIZE];
	for (int i=0; i<SIZE; i++) {
		unsigned long flag=1;
		for (int j=0;j<d;j++) {
			flag=flag * cypher[i] % n;
		}
		dec_num[i]=flag;
	}
	for (int i=0; i < SIZE; i++) {
		dec_str[i]=dec_num[i];
	}
	return dec_str;
}

/**
 *  for test purpose only
 *  实际的数据是一个byte[] 的字节流,
 *  每4个字节读取为一个无符号整数，形成int[]数组，作为输入参数
 */
int main(){
	printf("config(d=%d, n=%d, s=%d)\n",D, N, SIZE);
	char *txt = "test1test2test3test4";
	printf("assert_txt=%s\n", txt);

	int int_cypher[SIZE] = {0};
	// 此处为字节流hex
	char * cypher_hex = "0007A438000A841E000E339F0007A438000DE3190007A438000A841E000E339F0007A4380006965B0007A438000A841E000E339F0007A438000DF1BD0007A438000A841E000E339F0007A438000448D2";
	printf("cypher_hex=%s\n", cypher_hex);
//	printf("cypher_int=");
	for(int i = 0; i < SIZE; i++) {
		int step = 8;
		char int_str[8] = {0};
		strncpy(int_str, cypher_hex+i*step, step);
		int_cypher[i] = strtol(int_str, NULL, 16);
//		printf("%d ", int_cypher[i]);
	}
//	printf("\n");
	char *dec_dt = dec(int_cypher);
	printf("dec_dt=%s\n", dec_dt);
    return 0;
}
