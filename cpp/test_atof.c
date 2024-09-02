#include <stdio.h>
#include <stdlib.h>

int main()
{
    const char *y = "40E743CD1BF68000";
    printf("y=%s\n", y);
    double d = atof(y);
    printf("d=%f\n", d);
    const char *x = "0080F61BCD43E740";
    printf("x=%s\n", x);
    double e = atof(x);
    printf("e=%f\n", e);
    const char *m="22.234";
    printf("%f\n", atof(m));
}
