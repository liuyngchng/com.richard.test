#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[])
{
    char a[1000];
    // gets(a);         // 可用，但不推荐
    char *b = fgets(a, 100, stdin);
    int len = strlen(a);//获取串长方法
    printf("%d, %s", len, a);

}
