#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <fcntl.h>
#include "http.h"

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
        printf("create socket error\n");
        return -1;
    }
    printf("socket ready\n");
    srvaddr.sin_family = AF_INET;
    srvaddr.sin_port = htons(SRV_PORT);
    srvaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    setsockopt(sfd, SOL_SOCKET, SO_REUSEADDR, &sockopt, sizeof(int));

    res = bind(sfd, (struct sockaddr *)&srvaddr, sizeof(srvaddr));
    if(res < 0) {
        printf("bind error, port %d\n", SRV_PORT);
        close(sfd);
        return -1;
    }
    printf("bind ready, port %d\n", SRV_PORT);
    /*listen, 监听端口*/
    listen(sfd, 10);
    printf("wait for connect\n");
    while (1) {
        struct sockaddr_in cliaddr;
        socklen_t len = sizeof(cliaddr);
        int cfd;
        cfd = accept(sfd, (struct sockaddr *)&cliaddr, &len);
        if(cfd < 0) {
            printf("accept error\n");
            close(sfd);
            return -1;
        }
        // output client info
        char *ip = inet_ntoa(cliaddr.sin_addr);
        printf("client %s connected\n", ip);

        // output client request info
        char buf[1024] = {0};
        int size = read(cfd, buf, sizeof(buf));
        printf("%d bytes,request info:\n%s\n", size, buf);
        char l0[100]={0};
        readline(buf, l0, sizeof(l0), 0);
        printf("line0, %s\n", l0);
        char method[5]={0};
        char uri[50]={0};
        getmethod(l0, method, sizeof(method));
        geturi(l0, uri, sizeof(uri));
        printf("method %s, uri %s\n", method, uri);
        write(cfd, MSG, strlen(MSG));
        close(cfd);
    }
    close(sfd);
    return 0;
}
