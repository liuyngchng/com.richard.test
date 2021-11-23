#include "config.h"
#include "semaphore.h"

#ifndef CONTEXT_H
#define CONTEXT_H
struct context
{
    struct config config;
    struct semaphore semaphore;
    int a[20];
    int b[2];
    int c[2];
    int d[3][4][2];
};

// get int array from a 2d array string
void get_int_array(char *str_a2d, int *a, int size);

void get_str_a(int *a, char *a);
#endif
