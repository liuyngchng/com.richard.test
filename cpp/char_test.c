#include<stdio.h>
#include<string.h>
int main()
{
    char *a = "hello";
    printf("%lu, sizeof(a)=%lu", strlen(a), sizeof(a));
}
