gcc -c dtparser.c main.c
gcc -o libdtparser.so -shared -fPIC dtparser.c
ar crv libdtparser.a libdtparser.o
ranlib libdtparser.a
gcc -o main main.c -L./ -ldtparser
