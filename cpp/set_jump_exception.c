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
    printf("INT_MAX=%d, INT_MIN=%d, UINT_MAX=%u\n", INT_MAX, INT_MIN, UINT_MAX);
    check_overflow(UINT_MAX, 1); // 溢出情况
    check_overflow(UINT_MAX, 1); // 不溢出情况
    return 0;
}
