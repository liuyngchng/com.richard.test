/**
 * gcc -g -o _rsa_test1 rsa_test1.c
 */
#include<stdio.h>
#include<string.h>
#include <stdlib.h>

#define SIZE 20

unsigned int* encrypt_dt(int e, int n, const char* plain_txt) {
    printf("encrypt_dt(%d, %d, %s)\n", e, n, plain_txt);
	static int cypher[SIZE] = {0};
	int plain_num[SIZE];
	for (int i=0;i<SIZE;i++) {
	   plain_num[i]=plain_txt[i];
	}
	for (int i=0;i<SIZE;i++) {
		int flag=1;
	    for(int j=0;j<e;j++) {
		    flag=(flag*plain_num[i])%n;
	    }
	    cypher[i]=flag;
	}
	return cypher;
}


char *decrypt_dt(int d,int n, const int *cypher) {
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
 * 	公钥(public_key)(e=94956003 n=112444807)
	私钥(private_key)d=107685867
	plain_txt is hellorsa
	加密后的密文为(plain text be encrypted as following)：
	24425950 32535838 49790529 49790529 54270420 86497833 34531454 98113242
	解密后的明文为(cypher be decrypted as following)： hellorsa
 */
int main(){
	char * test1= "00366375";
	int a = atoi(test1);
	int e = 480317;
	int n = 968087;
	int d = 537653;
	printf("(e=%d, n=%d)\n", e, n);
	printf("(d=%d, n=%d)\n",d, n);
	char *txt = "ZeYJd8HFkI9l2$lGifRT";
	printf("txt=%s\n", txt);
	int *cypher = encrypt_dt(e, n, txt);
	printf("cypher=");
	for(int i=0;i<strlen(txt);i++) {
		printf("%d ", cypher[i]);
	}
	printf("cypher_hex=");
	for(int i=0;i<strlen(txt);i++) {
		printf("%02X", cypher[i]);
	}
	printf("\n");
	char *dec_dt = decrypt_dt(d,n, cypher);
	printf("dec_dt=%s\n", dec_dt);
    return 0;
}
