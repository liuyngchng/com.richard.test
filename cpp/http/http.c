#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <fcntl.h>

#define SERVER_PORT 8083
#define MSG "HTTP/1.1 200 OK\r\nContent-Length: 14\r\n\r\n{\"status\":200}"

int main()
{
    struct sockaddr_in srvaddr;
    int locfd;
    int sockopt = 1;
    int res;
    /*创建一个套接字*/
    locfd = socket(AF_INET, SOCK_STREAM, 0);
    if(locfd < 0) {
        printf("create socket error\n");
        return -1;
    }
    printf("socket ready\n");
    srvaddr.sin_family = AF_INET;
    srvaddr.sin_port = htons(SERVER_PORT);
    srvaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    setsockopt(locfd, SOL_SOCKET, SO_REUSEADDR, &sockopt, sizeof(int));

    res = bind(locfd, (struct sockaddr *)&srvaddr, sizeof(srvaddr));
    if(res < 0) {
        printf("bind error\n");
        close(locfd);
        return -1;
    }
    printf("bind ready\n");
    /*listen, 监听端口*/
    listen(locfd, 10);

    printf("wait for connect\n");

    while(1) {
        struct sockaddr_in cliaddr;
        socklen_t len = sizeof(cliaddr);
        int clifd;
        clifd = accept(locfd, (struct sockaddr *)&cliaddr, &len);
        if(clifd < 0) {
            printf("accept error\n");
            close(locfd);
            return -1;
        }

        // output client info
        char *ip = inet_ntoa(cliaddr.sin_addr);
        printf("client %s connected\n", ip);

        // output client request info
        char buff[1024] = {0};
        int size = read(clifd, buff, sizeof(buff));
        printf("request information:\n");
        printf("%s\n", buff);
        printf("%d bytes\n", size);
        write(clifd, MSG, strlen(MSG));
        close(clifd);
    }
    close(locfd);
    return 0;
}
