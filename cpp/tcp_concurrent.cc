#include <sys/types.h>
#include <stdio.h>
//#include <apra/inet.h>
#include <netinet/in.h>
#include <errno.h>
#include <sys/socket.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>

using namespace std;
void perr_exit(const char *s){
    perror(s);
    exit(-1);
}

int Accept(int fd, struct sockaddr *sa, socklen_t *salenptr)
{
    int n;
    again:
    if((n = accept(fd,sa,salenptr))<0){
        if((errno == ECONNABORTED) || errno == EINTR)
            goto again;
        else
            perr_exit("accept error");
    }
    return n;
}

int Bind(int fd, struct sockaddr *sa, socklen_t salen)
{
    int n;
    if ((n = bind(fd, sa, salen))<0){
        perr_exit("bind error");
    }
    return n;
}

int Connect(int fd, const struct sockaddr *sa, socklen_t salen)
{
    int n;
    n = connect(fd, sa, salen);
    if (n < 0)
    {
        perr_exit("connect error");
    }
    return n;
}

int Listen(int fd, int backlog)
{
    int n;
    if ((n=listen(fd, backlog))<0)
    {
        perr_exit("listen error");
    }
    return n;
}

int Socket(int family, int type, int protocol)
{
    int n;
    if ((n=socket(family, type, protocol))<0)
    {
        perr_exit("socket error");
    }
    return n;
}

ssize_t Read(int fd, void *ptr, size_t nbytes)
{
    ssize_t n;
    again:
    if ( (n=read(fd, ptr, nbytes))==-1 )
    {
        if (errno == EINTR)
            goto again;
        else
            return -1;
    }
    return n;
}

ssize_t Write(int fd, const void *ptr, size_t nbytes)
{
    ssize_t n;
    again:
    if ((n=write(fd, ptr, nbytes)) == -1)
    {
        if (errno == EINTR)
            goto again;
        else
            return -1;
    }
    return n;
}

int Close(int fd)
{
    int n;
    n = close(fd);
    if (n==-1){
        perr_exit("close error");
    }
    return n;
}

// 读取指定大小的内容
ssize_t Readn(int fd, void *vptr, size_t n)
{
    size_t nleft;  //usigned int 剩余未读取的字节数
    ssize_t nread; //int 实际读到的字节数
    char *ptr;

    ptr = vptr;
    nleft = n;

    while (nleft > 0){
        if ((nread=read(fd, ptr, nleft))<0)
        {
            if(errno=EINTR)
                nread=0;
            else
                return -1;
        } else if (nread == 0)
            break;

        nleft -= nread;
        ptr += nread;

    }
    return n - nleft;


}

ssize_t Writen(int fd, const void *vptr, size_t n)
{
    size_t nleft;
    ssize_t nwritten;
    const char *ptr;

    ptr = vptr;
    nleft = n;
    while (nleft >0){
        if ((nwritten = write(fd, ptr, nleft))<=0){
            if (nwritten <0 && errno == EINTR)
                nwritten = 0;
            else
                return -1;
        }
        nleft -= nwritten;
        ptr += nwritten;
    }
    return n;
}

static ssize_t  my_read(int fd, char *ptr)
{
    static int read_cnt;
    static char *read_ptr;
    static char read_buf[100];

    if (read_cnt <=0 )
    {
        again:
        if((read_cnt = read(fd, read_buf, sizeof(read_buf)))<0){
            if (errno==EINTR)
                goto again;
            return -1;
        } else if (read_cnt == 0)
            return 0;
        read_ptr = read_buf;
    }
    read_cnt--;
    *ptr = *read_ptr++;

    return 1;
}

ssize_t Readline(int fd, void *vptr, size_t maxlen){
    ssize_t n, rc;
    char c, *ptr;
    ptr = vptr;

    for (n=1; n<maxlen; n++){
        if((rc =my_read(fd, &c))==1){
            *ptr++ = c;
            if (c == "\n")
                break;
        }else if (rc==0){
            *ptr = 0;
            return n-1;
        } else
            return -1;
    }
    *ptr = 0;
    return n;
}


int main(int agrv, char *agrc[])
{
  int lfd, cfd;
    pid_t pid;
  
    struct sockaddr_in srv_addr, clt_addr;
  
    socklen_t clt_addr_len;
    char buf[BUFSIZ];
    int ret,i;

    memset(&srv_addr, 0, sizeof(srv_addr));  // 将地址结构清0
//    bzero(&srv_addr, sizeof(srv_addr));
    srv_addr.sin_family = AF_INET;
    srv_addr.sin_port = htons(7000);
    srv_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    lfd = Socket(AF_INET, SOCK_STREAM, 0);

    Bind(lfd, (struct sockaddr *)&srv_addr, sizeof(srv_addr));

    Listen(lfd, 128);
    clt_addr_len = sizeof(clt_addr);
    while (1){

        cfd = Accept(lfd, (struct sockaddr *)&clt_addr, &clt_addr_len);
        // 创建子进程
        pid = fork();
        if (pid < 0) {
            perr_exit("fork error");

        } else if (pid==0){
            // 子进程
            close(lfd);
            break;
        } else{
            // 注册信号捕捉
            struct sigaction act;
            act.sa_handler = catch_child;
            sigemptyset(&act.sa_mask);
            act.sa_flags = 0;
            ret = sigaction(SIGCHLD, &act, NULL);
            if (ret!=0){
               perr_exit("sigaction error");
            }
            close(cfd);
            continue;
        }
    }
    if (pid == 0){
        for (;;){
        ret = read(cfd, buf, sizeof(buf));
        if(ret == 0){
            // 检测到客户端关闭了
            close(cfd);
            exit(1);
        }
        for (i=0; i<ret; i++){
            cout << buf[i] << endl;
            buf[i] = toupper(buf[i]);
        }
        request = buf;
        cout <<request << endl;
      
        write(cfd, buf, ret);
				write(STDOUT_FILENO, buf, ret);
        }
    }
}
