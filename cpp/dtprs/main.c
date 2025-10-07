#include <stdio.h>
#include "dtparser.h"
int main(int argc, char* argv[]) {
    if(NULL == argv[1]) {
        printf("pls input dt\n");
        return -1;
    }
    printf("%s\n", prs_dt(argv[1]));
    return 0;
}
