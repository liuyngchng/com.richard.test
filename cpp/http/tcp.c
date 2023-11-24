#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include "util.h"

#define SRV_PORT 8083
#define MSG "HTTP/1.1 200 OK\r\nContent-Length: 14\r\n\r\n{\"status\":200}"

int startsrv() {
    struct sockaddr_in srvaddr;
    int sfd;
    int sockopt = 1;
    int res;
    /*创建一个套接字*/
    sfd = socket(AF_INET, SOCK_STREAM, 0);
    if(sfd < 0) {
        printf("[%s][%s-%d]create socket error\n", gettime(), filename(__FILE__),__LINE__);
        return -1;
    }
    printf("[%s][%s-%d]socket ready\n", gettime(), filename(__FILE__),__LINE__);
    srvaddr.sin_family = AF_INET;
    srvaddr.sin_port = htons(SRV_PORT);
    srvaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    setsockopt(sfd, SOL_SOCKET, SO_REUSEADDR, &sockopt, sizeof(int));

    res = bind(sfd, (struct sockaddr *)&srvaddr, sizeof(srvaddr));
    if(res < 0) {
        printf(
            "[%s][%s-%d]bind error, port %d, cause %d,%s\n",gettime(), 
            filename(__FILE__),__LINE__, SRV_PORT, errno, strerror(errno)
        );
        close(sfd);
        return -1;
    }
    printf("[%s][%s-%d]bind ready, port %d\n", gettime(), filename(__FILE__),__LINE__,SRV_PORT);
    listen(sfd, 10);
    printf("[%s][%s-%d]listening\n",gettime(), filename(__FILE__),__LINE__);
    while (1) {
        struct sockaddr_in cliaddr;
        socklen_t len = sizeof(cliaddr);
        int cfd;
        cfd = accept(sfd, (struct sockaddr *)&cliaddr, &len);
        if(cfd < 0) {
            printf("[%s][%s-%d]socket error, cause %d,%s\n", gettime(), filename(__FILE__),__LINE__, errno, strerror(errno));
            close(sfd);
            return -1;
        }
        // output client info
        char *ip = inet_ntoa(cliaddr.sin_addr);
        printf("[%s][%s-%d] client %s connected\n", gettime(), filename(__FILE__),__LINE__,ip);

        // output client request info
        char buf[1024] = {0};
        int size = read(cfd, buf, sizeof(buf));
        printf("[%s][%s-%d] %d bytes,request info:\n%s\n",gettime(), filename(__FILE__),__LINE__, size, buf);
        char l0[100]={0};
        getln(buf, l0, sizeof(l0), 0);
        char method[5]={0};
        char uri[50]={0};
        getmethod(l0, method, sizeof(method));
        geturi(l0, uri, sizeof(uri));
        printf("[%s][%s-%d]method %s, uri %s\n", gettime(), filename(__FILE__), __LINE__, method, uri);
        write(cfd, MSG, strlen(MSG));
        printf("[%s][%s-%d]return msg\n%s\n", gettime(), filename(__FILE__), __LINE__, MSG);
        close(cfd);
        printf("[%s][%s-%d]connect closed\n", gettime(), filename(__FILE__), __LINE__);
    }
    close(sfd);
    return 0;
}

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
