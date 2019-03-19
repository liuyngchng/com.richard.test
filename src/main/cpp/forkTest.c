/* 
 *  fork_test.c 
 *  version 1 
 *  Created on: 2010-5-29 
 *      Author: wangth 
 */  
#include <unistd.h>  
#include <stdio.h>   
int main ()   
{   
    pid_t fpid; //fpid表示fork函数返回的值  
    int count=0;  
    fpid=fork();   
    if (fpid < 0)   
        printf("error in fork!");   
    else if (fpid == 0) {  
        printf("i am the child process, my process id is %d\r\n/n",getpid());
        count++;
    }  
    else {  
        printf("i am the parent process, my process id is %d\r\n/n",getpid());
        count++;  
    }  
    printf("result is : %d/n",count);
	char x[100],*p;
	read(0,x,100);
	printf("%s", *p);
    return 0;  
}
