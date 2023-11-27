all:_server _client dtparser
dtparser:dtparser.c
	gcc -o ./libdtparser.so -fPIC -shared dtparser.c
_server:server.c util.c
	gcc -o _server server.c util.c -lpthread
_client:client.c peer.c http.c util.c
	gcc -o _client client.c peer.c http.c util.c
clean:
	rm -f _* *.o *.so
	
