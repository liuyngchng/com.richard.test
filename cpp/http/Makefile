all:_server _httpcli dtparser
dtparser:dtparser.c
	gcc -o ./libdtparser.so -fPIC -shared dtparser.c
_server:server.c util.c
	gcc -o _server server.c util.c -lpthread
_httpcli:httpcli.c client.c http.c util.c
	gcc -o _httpcli httpcli.c client.c http.c util.c
clean:
	rm -f _* *.o *.so
	
