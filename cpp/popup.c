#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

unsigned long random_xid(void)
{
    static int initialized;
    if (!initialized) {
        int fd; 
        unsigned long seed;
 
        fd = open("/dev/urandom", 0); 
        if (fd < 0 || read(fd, &seed, sizeof(seed)) < 0) {
            printf("Could not load seed from /dev/urandom: %s", strerror(errno));
            seed = time(0);
        }
        if (fd >= 0) {
            close(fd);
        }
        printf("seed=%ld\n",seed);
        //设置随机种子
        srand(seed);
        //下次取同样的随机数
        initialized++;
    }   
    return rand();
}

void printa( int* p, int l) {
	for (int i = 0; i < l; i++) {
        printf("%4d ", p+i);
        if ((i % 10) == 0) {
            printf("\n");
        }
    }
}


int main()
{
    int tmp;
    int a[1000];
    int l = sizeof(a)/sizeof(int);
    for (int i  = 0; i < l; i ++) {
        a[i] = random_xid()%1000;
    }
    printf("l=%d\n", l);
	printa(a, l);
    for (int i = 0; i < l; i ++) {
        for (int j = i; j < l; j ++) {
           if (i < j) {
                tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
           }
        }
    }
	printa(a, l);
}
