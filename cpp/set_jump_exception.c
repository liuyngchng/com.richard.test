#include <stdio.h>
#include <setjmp.h>
#include <limits.h>


void check_overflow(int a, int b) {
    jmp_buf env;
    if (setjmp(env) == 0) {
        int result = a + b;
        if (result > INT_MAX || result < 0) {
            printf("Overflow occurred!\n");
            longjmp(env, 1);
        } else {
            printf("Result: %d\n", result);
        }
    } else {
        printf("No overflow occurred.\n");
    }
}
int main() {
	printf("INT_MAX=%d, INT_MIN=%d\n", INT_MAX, INT_MIN);
    check_overflow(2147483647, 1); // 溢出情况
    check_overflow(2147483647, 1); // 不溢出情况
    return 0;
}
