/**
 * 1: little-endian
 * 0: big-endian
 */
# include <stdio.h>
int main()
{
    int a = 0x01020304;
    char *p = (char *)&a;
    printf("byte[0]=%d, addr=%p\n", *p, p);
    printf("byte[1]=%d, addr=%p\n", *(p+1), p+1);
    printf("byte[2]=%d, addr=%p\n", *(p+2), p+2);
    printf("byte[3]=%d, addr=%p\n", *(p+3), p+3);
}  
