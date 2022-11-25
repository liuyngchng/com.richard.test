/**
 * 1: little-endian
 * 0: big-endian
 */
# include <stdio.h>
int main()
{
    int a = 0x0102;
    char *p = (char *)&a;
    printf("byte[0]=%d, addr=%p\n", *p, p);
    printf("byte[1]=%d, addr=%p\n", *(p+1), p+1);
}  
