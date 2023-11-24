#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <errno.h>
#include <pthread.h>
#include "util.h"

#define _SRV_PORT_ 8083
#define _BACKLOG_ 10
#define _MSG_ "HTTP/1.1 200 OK\r\nContent-Length: 14\r\n\r\n{\"status\":200}"

void* rcvdata(void *);

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
    srvaddr.sin_family = AF_INET;
    srvaddr.sin_port = htons(_SRV_PORT_);
    srvaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    setsockopt(sfd, SOL_SOCKET, SO_REUSEADDR, &sockopt, sizeof(int));
    res = bind(sfd, (struct sockaddr *)&srvaddr, sizeof(srvaddr));
    if (res < 0) {
        printf(
            "[%s][%s-%d]bind error, port %d, cause %d,%s\n",gettime(), 
            filename(__FILE__),__LINE__, _SRV_PORT_, errno, strerror(errno)
        );
        close(sfd);
        return -1;
    }
    listen(sfd, _BACKLOG_);
    printf("[%s][%s-%d]listening %d\n",gettime(), filename(__FILE__),__LINE__,_SRV_PORT_);
    while (1) {
        struct sockaddr_in cliaddr;
        socklen_t len = sizeof(cliaddr);
        int cfd;
again:  cfd = accept(sfd, (struct sockaddr *)&cliaddr, &len);
        if(cfd < 0) {
            printf(
                "[%s][%s-%d]accept error, cause %d,%s\n", 
                gettime(), filename(__FILE__),__LINE__, errno, strerror(errno)
            );
            if((errno == ECONNABORTED) || errno == EINTR) {
				goto again;
			} else {
				close(sfd);
                printf("[%s][%s-%d]server closed\n", gettime(), filename(__FILE__),__LINE__);
                return -1;
			}
        }
        char *ip = inet_ntoa(cliaddr.sin_addr);
        printf("[%s][%s-%d] %s connected\n", gettime(), filename(__FILE__),__LINE__,ip);
        pthread_t t;
		pthread_create(&t, NULL, &rcvdata, &cfd);
		pthread_detach(t);
    }
    close(sfd);
    printf("[%s][%s-%d]server closed\n", gettime(), filename(__FILE__),__LINE__);
    return 0;
}

/**
 * rcv data sent from peer
 **/
void *rcvdata(void* sockfd) {
    char buf[1024] = {0};
    int cfd = *(int*)sockfd;
    int size = read(cfd, buf, sizeof(buf));
    printf("[%s][%s-%d] %d bytes recieved\n%s\n",gettime(), filename(__FILE__),__LINE__, size, buf);
    char resp[1024] = {0};
    getresponse(buf,resp);
    write(cfd, resp, strlen(resp));
    printf("[%s][%s-%d]return msg\n%s\n", gettime(), filename(__FILE__), __LINE__, resp);
    close(cfd);
    printf("[%s][%s-%d]connection closed\n", gettime(), filename(__FILE__), __LINE__);
    return NULL;
}

int getresponse(char *buf, char *resp) {
    char l0[100]={0};
    char method[5]={0};
    char uri[50]={0};
    char body[1024] = {0};
    getln(buf, l0, sizeof(l0), 0);
    getln(buf, body, sizof(body), 2);
    getmethod(l0, method, sizeof(method));
    geturi(l0, uri, sizeof(uri));
    printf("[%s][%s-%d]method %s, uri %s\n", gettime(), filename(__FILE__), __LINE__, method, uri);
    char *prsdt="/prs/dt";
    if(strncmp(uri, prsdt, strlen(prsdt))) {
        strcat(resp, "prsdt");
    } else {
        strcat(resp, _MSG_);
    }
    
}

int main(int argc, char* argv[]) {
    printf("[%s][%s-%d]server starting\n", gettime(), filename(__FILE__), __LINE__);
    startsrv();
    return 0;
}