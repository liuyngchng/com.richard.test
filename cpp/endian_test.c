/**
 * 1: little-endian
 * 0: big-endian
 */
# include <stdio.h>
int main()
{
    int a = 0x0200;
    char *p = (char *)&a;
    printf("%d, %p\n", *p, p);
    printf("%d, %p\n", *(p+1), p+1);
}  
