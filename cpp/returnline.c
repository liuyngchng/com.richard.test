#include <stdio.h>
#include <unistd.h>

int main()
{
	while(1) {
		printf("\rthis is a test");
		fflush(stdout);
		sleep(1);
		printf("\rtest a is this");
		fflush(stdout);
		sleep(1);
	}
}
