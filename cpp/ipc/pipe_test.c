#include <stdio.h>
#include <unistd.h>                                                                                                          
#include <stdlib.h>
#include <string.h>
 
int main()
{
    int pipefd[2] = {0};
    if(pipe(pipefd) != 0){
        perror("pipe error!");
        return 1;
    }
 
    //pipefd[0]:读取段  pipefd[1]:写入端
    printf("pipefd[0]：%d\n",pipefd[0]);//3
    printf("pipefd[1]：%d\n",pipefd[1]);//4

	if(fork() == 0){
        //子进程---写入
        close(pipefd[0]); //关闭子进程的读取端
        const char* msg = "hello-linux!";
        while(1){
            write(pipefd[1], msg, strlen(msg)); //子进程不断的写数据
            sleep(1);
        }
    	exit(0);
    }
 
    //父进程---读取
    close(pipefd[1]); //关闭父进程的写入端
    char buffer[64] = {0};
    while(1){
        //如果read返回值是0，就意味着子进程关闭文件描述符了
        ssize_t s = read(pipefd[0], buffer, sizeof(buffer)); //父进程不断的读数据
        if(s == 0){
            break;
        }
        else if(s > 0){
            buffer[s] = 0;
            printf("child say to parent：%s\n",buffer);
        }
        else{
            break;
        }
    }
    return 0;
}

