all:_server _client _test dtparser
dtparser:dtparser.c
	gcc -o ./libdtparser.so -fPIC -shared dtparser.c
_server:server.c util.c dtparser.c dispatcher.c http.c
	gcc -o _server server.c dispatcher.c util.c dtparser.c http.c peer.c -lpthread
_client:client.c peer.c http.c util.c
	gcc -o _client client.c peer.c http.c util.c
_test:test.c peer.c http.c util.c
	gcc -o _test test.c peer.c http.c util.c
clean:
	rm -f _* *.o *.so
	
