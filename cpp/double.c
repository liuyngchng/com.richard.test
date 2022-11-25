#include <stdio.h>
#include <math.h>
/**
 * 输出一个字节的2进制字符串
 */
void print_bin(char c){
	for(int i =0; i < 8; i++){
		printf("%d", ((c<<i)& 0b10000000) >> 7);
	}
}

int main()
{
	unsigned long x = 0x40E743CD1BF68000;
	double *a = (double*)&x;
	printf("%f, sizeof(double)=%lu\n", *a, sizeof(double));
	for(int i=sizeof(double)-1; i>=0;i--){
		char *p = (char*)a;
		char c = *(p+i);
		print_bin(c);
		printf(" ");
	}
	printf("\n");
	unsigned char b=0xFF;
	printf("%d\n", b);
	print_bin(b);
}
