#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
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

int main()
{
	unsigned long l = random_xid();
	printf("l=%ld\n",l);
}
