#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <errno.h>
#include "util.h"

int writemsg(char *ip, int port, char *req, char *resp) {
    struct sockaddr_in server_sock;
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        printf(
            "[%s][%s-%d]socket error, cause %d,%s\n",
            gettime(),filename(__FILE__), __LINE__, errno, strerror(errno)
        );
    }
    bzero(&server_sock, sizeof(server_sock));
    server_sock.sin_family = AF_INET;
    inet_pton(AF_INET, ip, &server_sock.sin_addr);
    server_sock.sin_port = htons(port);
    int ret = connect(sock, (struct sockaddr*)& server_sock, sizeof(server_sock));
    if (ret < 0) {
        printf(
            "[%s][%s-%d]connect error, cause %d,%s\n", 
            gettime(),filename(__FILE__), __LINE__, errno, strerror(errno)
        );
        return 1;
    }
    printf("[%s][%s-%d]connected to %s:%d\n", gettime(),filename(__FILE__), __LINE__, ip, port);
    write(sock, req, strlen(req));
    // printf("write msg, %s", req);
    int n=0;
    // printf("read buf\n");
    int count = 0;
    while(1) {
        char buf[256] = {0};
        // m=read(sock, buf, sizeof(buf));
        int m=recv(sock, buf, sizeof(buf), 0);
        n+=m; 
        // printf("\n%d, =====buf====\n%s\n", count++, buf);
        // fflush;
        strncat(resp, buf, m);
        int a = (m!=sizeof(buf));
        if (a) {
            printf("[%s][%s-%d]rcv finish\n", gettime(),filename(__FILE__), __LINE__);
            break;
        } 
    }
    resp[n]='\0';
    close(sock);
    return n;
}
