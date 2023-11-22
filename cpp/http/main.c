#include "tcp.h"
#include "http.h"
#include<stdio.h>

// http request test
void myhttpreq();

void main(int argc, char* argv[]) {
    // char *s = "GET / HTTP/1.1";
    // char *s = "key1=value1&key2=value2&key3=value3";
    // char t[20] = {0};
    // readline(s, t, sizeof(t), 2);
    // getmethod(s, t, 10);
    // geturi(s, t, 10);
    // printf("t, %s\n", t);
    // startsrv();
    myhttpreq();

}

void myhttpreq() {
    char *ip = "127.0.0.1";
    int port = 8083;
    char *req = "GET / HTTP/1.1\r\nHost: localhost:8083\r\nAccept:application/json\r\n\r\n";
    char response[1024] = {0};
    printf("write msg\n%s\n", req);
    writemsg(ip, port, req, response);
    printf("\nread msg\n%s\n", response);
    return;
}
