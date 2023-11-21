#include <stdio.h>
#include "dtparser.h"
int main(int argc, char* argv[]) {
    if(NULL == argv[1]) {
        printf("pls input dt\n");
        return -1;
    }
    //printf("prs_dt, %s\n", argv[1]);
//    printf("%s\n", prs_up_rpt_dt(argv[1]));
    prs_dt(argv[1]);
    return 0;
}
