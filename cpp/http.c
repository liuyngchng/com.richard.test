
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <time.h>
#include <errno.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define IPSTR "127.0.0.1"
#define PORT 80
#define BUFSIZE 4096 


int get_data()
{
    int sockfd, ret, i, h;
    struct sockaddr_in servaddr;
    char str1[4096], str2[4096], buf[BUFSIZE], *str;
    socklen_t len;
    fd_set   t_set1;
    struct timeval  tv;

    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0 ) {
            printf("create socket error!\n");
            exit(0);
    };

    bzero(&servaddr, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(PORT);
    if (inet_pton(AF_INET, IPSTR, &servaddr.sin_addr) <= 0 ){
            printf("inet pton error!\n");
            exit(0);
    };

    if (connect(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0){
            printf("connect error!\n");
            exit(0);
    }
    printf("connect success\n");

    //send data
    memset(str2, 0, 4096);
    strcat(str2, "a=1&b=2");
    str=(char *)malloc(128);
    len = strlen(str2);
    sprintf(str, "%d", len);

    memset(str1, 0, 4096);
    strcat(str1, "GET /data HTTP/1.1\n");
    strcat(str1, "Host: www.test.cn\n");
    strcat(str1, "Content-Type: application/x-www-form-urlencoded\n");
    strcat(str1, "Content-Length: ");
    strcat(str1, str);
    strcat(str1, "\n\n");

    strcat(str1, str2);
    strcat(str1, "\r\n\r\n");
    printf("%s\n",str1);

    ret = write(sockfd,str1,strlen(str1));
    if (ret < 0) {
        printf("snd fail, err_code = %d，err_msg = '%s'\n",errno, strerror(errno));
        exit(0);
    } else {
        printf("snd success %d byte！\n\n", ret);
    }

    FD_ZERO(&t_set1);
    FD_SET(sockfd, &t_set1);
    while(1) {
        sleep(1);
        tv.tv_sec= 0;
        tv.tv_usec= 0;
        h = 0;
        h = select(sockfd +1, &t_set1, NULL, NULL, &tv);
        if (h == 0) continue;
        if (h < 0) {
            close(sockfd);
            printf("在读取数据报文时SELECT检测到异常，该异常导致线程终止！\n");
            return -1;
        };
        if (h > 0) {
            memset(buf, 0, 4096);
            i= read(sockfd, buf, 4095);
            if (i==0){
                close(sockfd);
                printf("读取数据报文时发现远端关闭，该线程终止！\n");
                return -1;
            }

            printf("%s\n", buf);
            return 0;
        }
    }
    close(sockfd);
    return 0;
}

int main(int argc, char **argv)
{
    get_data();
    return 0;
}
