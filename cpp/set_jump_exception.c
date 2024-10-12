#include <stdio.h>
#include <setjmp.h>
#include <limits.h>


void check_overflow(unsigned int a, unsigned int b) {
    jmp_buf env;
    if (setjmp(env) == 0) {
        unsigned int result = a + b;
        if (result > INT_MAX || result < INT_MIN) {
            printf("Overflow occurred, result = %d\n", result);
            longjmp(env, 1);
        } else {
            printf("Result: %d\n", result);
        }
    } else {
        printf("No overflow occurred.\n");
    }
}
int main() {
    printf("INT_MAX=%d, INT_MIN=%d, UINT_MAX=%u, LONG_MIN=%ld, LONG_MAX=%ld, LLONG_MIN=%lld, LLONG_MAX=%lld\n", INT_MAX, INT_MIN, UINT_MAX, LONG_MIN, LONG_MAX, LLONG_MIN, LLONG_MAX);
    printf("sizeof(int)=%ld, sizeof(long)=%ld, sizeof(long long)=%ld, sizeof(char)=%ld, sizeof(short)=%ld, sizeof(float)=%ld, sizeof(double)=%ld\n", sizeof(int), sizeof(long), sizeof(long long), sizeof(char), sizeof(short), sizeof(float), sizeof(double));
    check_overflow(UINT_MAX, 1); // 溢出情况
    check_overflow(UINT_MAX, 1); // 不溢出情况
    return 0;
}
