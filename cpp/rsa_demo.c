#include<stdio.h>
#include<string.h>
#include <stdlib.h>

#define max 20000
static int size;
static int cypher[max];			//为加密后的数字密文

/**
 * 公约数只有1的两个数叫做互质数
 * 判断两个数是否互为素数  eg:p和q e和 t
 */
int is_coprime_number(int p,int q) {
	int m,n;
	if(q<p) {
		m=p;  p=q;  q=m;  //将p换成p和q之间那个小的数
		m=q%p;  n=q/p;  //辗转相除法求两个数的最大公因数
	}
	while(m!=0) {
		q=p; p=m;  //将p换成p和q之间那个小的数
		m=q%p;  n=q/p;
	}
	if (m==0&&n==q) {
		return 1;
	} else {
		printf("%d and %d is not coprime！\n", p, q);
	    return 0;
	}
}
/**
 * 判断输入的p和q是不是素数
 */
int is_prime(int s){
	for (int i=2;i<s;i++) {
		if (s%i==0) {
			printf("%d 不是一个素数(%d is not a prime)\n", s, s);
			return 0;
		}
	}
	return 1;
}
/**
 * 求私钥d
 * //t:欧拉函数
 */
int get_private_key(int e, int t) {
	int d;
	for(d=0;d<t;d++) {
	    if (e * d % t==1) {
	       return d;
	    }
	}
	printf("获取私钥发生错误(get_private_key_err)\n");
	return -1;
}
/**
 * 随机生成与 t互质的数e
 */
int get_random(int p,int q) {
	int t=(p-1)*(q-1);
	while(1) {
		int e=rand() % t;
		if(is_coprime_number(e,t)==1) {
			return e;
		}
	//	if(e<=2)
	//	e=3;
	}
}
void encrypt_dt(int e, int n) {
	//先将符号明文转换成字母所对应的ascii码。
	char plain_txt[100];    //符号明文
	printf("请输入明文(please input plain text for encryption)：\n");
	scanf("%s",plain_txt);
	size=strlen(plain_txt);
	int plain_num[strlen(plain_txt)];   //定义符号明文
	for (int i=0;i<strlen(plain_txt);i++) {
	   plain_num[i]=plain_txt[i];        //将字母转换成对应的ascii码。
	//printf("%d",mingwen[i]);  //将字母转换成对应的ascii码。可以不输出
	}
	int flag=1;    //miwen为加密后的数字密文
	for (int i=0;i<strlen(plain_txt);i++) {
	    for(int j=0;j<e;j++) {
		    flag=flag*plain_num[i]%n;
	    }
	    cypher[i]=flag;
	    flag=1;
	}
	printf("加密明文 %s 后的密文为(plain text '%s' be encrypted as following)：\n", plain_txt, plain_txt);
	for(int i=0;i<strlen(plain_txt);i++)
	printf("%d",cypher[i]);
}
void decrypt_dt(int d,int n) {
	int dec_num[size],flag=1;	//解密后得到的数字明文（即ascii码）
	char dec_str[size];//解密后得到的字符串明文
	for (int i=0;i<size;i++) {
	   for (int j=0;j<d;j++) {
	   	  flag=flag*cypher[i]%n;
	   }
	   dec_num[i]=flag;
	   flag=1;
	}
	printf("解密后的明文为(cypher be decrypted as following)：\n");
	for (int i=0;i<size;i++) {
		dec_str[i]=dec_num[i];
		printf("%c",dec_str[i]);
	}
}
int main()
{
	int p,q,e,d,n,t,tep;
	while(1) {
		printf("请输入p(please input a number for p):");
		scanf("%d",&p);
		tep=is_prime(p);
		if(!tep) {
			printf("p=%d 不是素数，请重新输入p！(p=%d is not a prime ,please input a number again for p)\n", p, p);
		    continue;
		}
		printf("请输入q(please input a number for q):");
		scanf("%d",&q);
	    tep=is_prime(q);
	    if (!tep) {
			printf("q=%d 不是素数，请重新输入q！(q=%d is not a prime ,please input a number again for q)\n", q, q);
			printf("请输入q(please input a number for q):");
			scanf("%d",&q);
			tep=is_prime(q);
		}
		int n=p*q;
		int t=(p-1)*(q-1);
		tep=is_coprime_number(p,q);
		if (!tep) {
			printf("%d 和 %d 互为质数不成立(%d and %d is not with a co-prime relation)\n", p, q, p, q);
			continue;
		}
		printf("t=(q-1)*(p-1)=%d\n",t);
		e=get_random(p,q);
		printf("公钥(public_key)(e=%d n=%d)\n",e,n);
		tep=(e,t);
		d=get_private_key(e,t);
		printf("私钥(private_key)d=%d",d);
		int a=0;
		while (a!=3) {
			printf("\n-------------------------\n");
			printf("[1] 输入1进行加密(input 1 for encrypt data)\n");
	        printf("[2] 输入2进行解密(input 2 for decrypt data)\n");
	        printf("[3] 其他输入则退出(any other input for program exit)");
	        printf("\n-------------------------\n");
	        scanf("%d",&a);
	        getchar();
	        switch (a) {
				case 1:
					encrypt_dt(e,n);
					break;
				case 2:
					printf("请输入密钥(please input decrypt key)：");
					scanf("%d",&d);
					decrypt_dt(d,n);
					break;
				default:
					return 0;
					break;
	        }
		}
    }
    return 0;
}
