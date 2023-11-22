#include "tcp.h"
#include "http.h"
#include<stdio.h>

void main(int argc, char* argv[]) {
    char *s = "GET / HTTP/1.1";
    // char *s = "key1=value1&key2=value2&key3=value3";
    char t[20] = {0};
    // readline(s, t, sizeof(t), 2);
    // getmethod(s, t, 10);
    geturi(s, t, 10);
    printf("t, %s\n", t);
    startsrv();
}
