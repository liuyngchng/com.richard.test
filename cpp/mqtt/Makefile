CC=gcc
PAHO_SRC=/home/rd/workspace/paho.mqtt.c/src
#CFLAGS += -D__SSL1_0__
all: _test
_test:publish.c
	${CC} -g -o _test -I ${PAHO_SRC} -L ${PAHO_SRC} publish.c -lpaho-mqtt3c
clean:
	rm -fr _* *.o *.so *.out
