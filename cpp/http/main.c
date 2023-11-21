#include "read_line.h"
#include "http.h"
#include<stdio.h>

void main(int argc, char* argv[]) {
    char *s = "\nline1\nline2\nlin33333\nline4";
    char t[20] = {0};
    read_line(s, t, sizeof(t), 3);
    printf("t, %s\n", t);
    start_srv();

}
