CC=gcc
#CFLAGS += -D__SSL1_0__
SOURCE_FILE= kwdb_demo.c 
all:_kwdb
_kwdb: ${SOURCE_FILE}
	${CC} -g ${CFLAGS} -o _kwtest ${SOURCE_FILE} -lodbc
clean:
	rm -fr _* *.o *.so *.out

