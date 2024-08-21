#include<stdio.h>
#include<string.h>
#include <stdlib.h>

#define P 11
#define Q 199


/**
 * 公约数只有1的两个数叫做互质数
 * 判断两个数是否互为素数, p和q e 和 t
 */
int get_com_div(const int p, const int q) {
	int a, b;
	if (p > q) {
		a = p, b=q;
	} else {
		a = q, b=p;
	}

	if (b == 0) {
		return a;
	} else {
		return get_com_div(b, a % b);
	}
}
/**
 * 判断输入的p和q是不是素数
 */
int is_prime(const int s) {
	for (int i=2; i<s; i++) {
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
int get_private_key(const int e, const int t) {
	int d;
	for(d=0;d<t;d++) {
	    if (e * d % t==1) {
	    	return d;
	    }
	}
	printf("get_private_key_err, give another \n");
	return -1;
}
/**
 * 随机生成与 t互质的数e
 */
int get_random(const int p, const int q) {
	int t=(p-1)*(q-1);
	while(1) {
		int e=rand() % t;
		if(get_com_div(e,t)==1) {
			return e;
		}
	//	if(e<=2)
	//	e=3;
	}
}
void encrypt_dt(const int e, const int n, const char* plain, int size, int *const cypher) {
	int plain_num[size];
	for (int i=0;i<size;i++) {
		plain_num[i]=plain[i];
	}
	for (int i=0;i<size;i++) {
		int flag=1;
	    for(int j=0;j<e;j++) {
		    flag=flag*plain_num[i]%n;
	    }
	    cypher[i]=flag;
	}

}
void decrypt_dt(const int d, const int n, const int *cypher, char *const plain, const int size) {
	int dec_num[size];
	for (int i=0;i<size;i++) {
		int flag = 1;
		for (int j=0;j<d;j++) {
			flag=flag * cypher[i]%n;
		}
		dec_num[i]=flag;
	}
	for (int i=0;i<size;i++) {
		plain[i]=dec_num[i];
	}
}
int main(){
//	int e = 82208639;
//	int n = 112444807;
//	int d = 84608559;
	int e, d, n, t, tep;
	tep = is_prime(P);
	if (!tep) {
		printf("p=%d is not a prime\n", P);
		return -1;
	}
	tep = is_prime(Q);
	if (!tep) {
		printf("q=%d is not a prime\n", Q);
		return -1;
	}

	tep = get_com_div(P, Q);
	if (tep != 1) {
		printf("%d and %d is not with a co-prime relation\n",P, Q);
		return -1;
	}
	n = P * Q;
	t = (P-1)*(Q-1);
	printf("p=%d, q=%d\n", P, Q);
	printf("t=(q-1)*(p-1)=%d\n",t);
	e=get_random(P, Q);
	printf("pub_key(e=%d n=%d)\n", e, n);
	tep=(e,t);

	d=get_private_key(e,t);
	printf("pri_key(d=%d, n=%d)\n",d, n);
	char *txt = "hellorsa";
	int size = strlen(txt);
	printf("plain_text:%s\n", txt);
	int cypher[2000] = {0};
	encrypt_dt(e, n, txt, size, cypher);
	printf("cypher:");
	for(int i=0;i<size;i++) {
		printf("%d", cypher[i]);
	}
	printf("\n");
	char dec_result[20] = {0};
	decrypt_dt(d,n, cypher, dec_result, size);
	printf("dec_dt：%s\n", dec_result);
    return 0;
}
