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
void hex2bin(unsigned char *bin, const char *hex) {
	int i = 0;
	for (i = 0; i < strlen(hex); i += 2) {
		bin[i / 2] = (char)((hexcharToInt(hex[i]) << 4) | hexcharToInt(hex[i + 1]));
        printf("bin[%d]=%02X\n", i/2, bin[i/2]);
	}
}


int main()
{
	//unsigned long x = 0x40E743CD1BF68000;
	unsigned long x = 0x47BF716620000000;
	printf("unsigned long x=%lX\n", x);
	double *a = (double*)&x;
	printf("double a=%f, sizeof(a)=%lu\n", *a, sizeof(a));
	for (int i = sizeof(a) - 1; i >= 0; i--) {
		char *p = (char*)a;
		unsigned char c = *(p+i);
		printf("byte%d=", i);
		print_bin(c);
		printf(" 0X%02X\n", c);
	}
	printf("\n");
	unsigned char b=0xFF;
	printf("unsigned char b = %d, 0X%X, 0B", b, b);
	print_bin(b);
	printf("\n");
	const char *y = "40E743CD1BF68000";
	printf("literal hex str: char *y = %s, strlen(y)=%lu\n", y, strlen(y));
	for(int i = 0; i< strlen(y); i++) {
		printf("byte%d=%c\n", i, *(y+i));
	}
	unsigned char z[8];
    printf("copcy hex str char *y to bin str char *z\n");
	hex2bin(z, y);
	size_t size = strlen((const char*)z);
	printf("strlen(z)=%lu\n", size);
	printf("bin char str: unsigned char *z=");
	for (int i = 0; i < 8; i++) {
		printf("%02X", *(z+i));
	}
	printf("\n");
	for (int i = 0; i < 8; i++) {
		unsigned char c = *(z+i);
		printf("byte%d=", i);
		print_bin(c);
		printf(" 0X%02X\n", c);
	}
	double *c = (double*)z;
	printf("double c=%f\n", *c);
	unsigned char dz[8];
	for(int i = 0; i < 8; i++) {
		*(dz+i) = *(z + 7 -i);
	}
	printf("bin char str in small end: unsigned char *dz=\n");
	for (int i = 7; i >=0; i--) {
		printf("%02X", *(dz+i));
	}
    printf("\ndouble value of *dz=%f\n", *(double *)dz);
}
