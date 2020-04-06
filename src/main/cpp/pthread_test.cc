#include <iostream>
#include <pthread.h>

using namespace std;

void *fn(void *arg)
{
    int i = *(int *)arg;
    cout<<"i = "<<i<<endl;

    return ((void *)0);
}

int main()
{
    int err1;
    int i=10;
	pthread_t thread;
    err1 = pthread_create(&thread, NULL, &fn, &i);
    pthread_join(thread, NULL);

}
