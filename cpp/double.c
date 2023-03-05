#include <stdio.h>
#include <string.h>
/**
 * 输出一个字节的2进制字符串
 */
void print_bin(char c) {
	for (int i = 0; i < 8; i++) {
		printf("%d", ((c<<i) & 0b10000000) >> 7);
	}
}

int hexcharToInt(char c)
{
	if (c >= '0' && c <= '9') return (c - '0');
	if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
	if (c >= 'a' && c <= 'f') return (c - 'a' + 10);
	return 0;
}
//
void hex2bin(unsigned char *bin, char *hex, int binlength) {
	int i = 0;
	for (i = 0; i < strlen(hex); i += 2) {
		bin[i / 2] = (char)((hexcharToInt(hex[i]) << 4)
			| hexcharToInt(hex[i + 1]));
	}
}


int main()
{
	unsigned long x = 0x40E743CD1BF68000;
	printf("unsigned long x=%lX\n", x);
	double *a = (double*)&x;
	printf("double a=%f, sizeof(a)=%lu\n", *a, sizeof(a));
	for (int i = sizeof(a) - 1; i >= 0; i--) {
		char *p = (char*)a;
		unsigned char c = *(p+i);
		printf("byte%d ", i);
		print_bin(c);
		printf(" 0X%02X\n", c);
	}
	printf("\n");
	unsigned char b=0xFF;
	printf("unsigned char b = %d, 0X%X, 0B", b, b);
	print_bin(b);
	printf("\n");
	char *y = "40E743CD1BF68000";
	printf("char *y = %s, strlen(y)=%lu\n", y, strlen(y));
	unsigned char z[(int)strlen(y)/2];
	unsigned char *m=z;
	hex2bin(m, y ,strlen(y));
	for (int i = strlen(z) - 1; i >= 0; i--) {
		char c = *(z+i);
		printf("byte%d ", i);
		print_bin(c);
		printf(" 0X%02X\n", c);
	}
}
