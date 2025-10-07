#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#define _SIZE_ 10 

static int round = 0;

/**
 * get random number.
 */
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
        //printf("seed=%ld\n",seed);
        srand(seed);
        initialized++;
    }   
    return rand();
}

/**
 * print int array.
 */
void printa(int* p, int l)
{
    for (int i = 0; i < l; i++) {
        printf("%3d ", p[i]);
        if (((i+1) % 10) == 0) {
            printf("\n");
        }
    }
    printf("\n");
}

/**
 * popup sort.
 */
void popup()
{
    printf("%s sort.\n", __func__);
    int tmp;
    int a[_SIZE_];
    int l = sizeof(a)/sizeof(int);
    for (int i  = 0; i < l; i ++) {
        a[i] = random_xid()%_SIZE_;
    }
    printf("sort %d random number\n", l);
    printa(a, l);
    for (int i = 0; i < l; i ++) {
        for (int j = i; j < l; j ++) {
            round++;
            if (a[i] < a[j]) {
                tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        }
    }
    printf("\nrun %d round, after popup:\n", round);
    printa(a, l);
}

/**
 * qsort recursion.
 */
void q_sort(int *a, int left, int right)
{
    if (left >= right) {
        return;
    }
    int i = left;
    int j = right;
    int key = a[left];
     
    while (i < j) {
        while (i < j && key <= a[j]) {
            round ++;
            j--;
        }
        a[i] = a[j];
        while(i < j && key >= a[i]) {
            i++;
            round ++;
        }
        a[j] = a[i];
    }
    a[i] = key;
    printa(a, _SIZE_);
    q_sort(a, left, i - 1);
    q_sort(a, i + 1, right);
}

/**
 *  my qsort function.
 */
void myqsort()
{
    printf("run %s\n", __func__);
    int a[_SIZE_] ;
    int l = sizeof(a)/sizeof(int);
    for (int i  = 0; i < l; i ++) {
        a[i] = random_xid()%_SIZE_;
    }
    printf("sort %d random number\n", l);
    printa(a, l);
    q_sort(a, 0, l-1);
    printf("after run %d round\n", round);
    printa(a, l);
}

int main() 
{
    myqsort();
}
