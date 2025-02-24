/*************************************************************************
    > File Name: tcp_server.c
    > Author: weierxiao
    > Mail: 1091868910@qq.com
    > Created Time: Sat 08 Jul 2017 05:15:29 PM CST
 ************************************************************************/

#include<stdio.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<errno.h>
#include<sys/types.h>
#include<unistd.h>
#include<string.h>
#define _PORT_ 9999
#define _BACKLOG_ 10
int main()
{
    int sock = socket(AF_INET, SOCK_STREAM,0);
    if (sock < 0)
    {
        printf("socket error, errno is %d, errstring is %s\n", errno, strerror(errno));
    }
    struct sockaddr_in server_sock;
    struct sockaddr_in client_sock;
    bzero(&server_sock, sizeof(server_sock));

    server_sock.sin_family = AF_INET;
    server_sock.sin_addr.s_addr = htonl(INADDR_ANY);
    server_sock.sin_port = htons(_PORT_);

    if (bind(sock, (struct sockaddr*)&server_sock, sizeof(struct sockaddr_in))< 0)
    {
        printf("bind error, errno is %d, errstring is %s\n", errno, strerror(errno));
        close(sock);
        return 1;
    }

    if (listen(sock, _BACKLOG_)< 0)
    {
        printf("listen error, errno is %d, errstring is %s\n", errno, strerror(errno));
        close(sock);
        return 2;
    }

    printf("bind and listen success, wait accept!\n");

    while (1)
    {
        socklen_t len = sizeof(client_sock);
        int client_socket = accept(sock, (struct sockaddr*)&client_sock,&len);
        if (client_socket < 0)
        {
            printf("accept  error, errno is %d, errstring is %s\n", errno, strerror(errno));
            close(sock);
            return 3;
        }

        char buf_ip[INET_ADDRSTRLEN];
        memset(buf_ip, 0, sizeof(buf_ip));
        inet_ntop(AF_INET, &client_sock.sin_addr, buf_ip, sizeof(buf_ip));

        printf("get connection :ip is  %s, port is %d\n ",buf_ip, ntohs(client_sock.sin_port) );

        while (1)
        {
            char buf[1024];
            memset(buf, 0, sizeof(buf));

            read(client_socket, buf, sizeof(buf));
            printf("client# : %s\n", buf);
            printf("server# :");
            memset(buf, 0, sizeof(buf));
            fgets(buf, sizeof(buf), stdin);
            buf[strlen(buf) - 1 ]= '\0';
            write(client_socket, buf, strlen(buf)+1);
            printf("please wait ...\n");

        }
    }

    close(sock);
    return 0;

    }
