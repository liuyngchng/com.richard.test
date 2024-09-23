CC=gcc
PAHO_SRC=/home/rd/workspace/paho.mqtt.c/src
#CFLAGS += -D__SSL1_0__
all: _pub _sub
_pub:publish.c
	${CC} -g -o _pub -I ${PAHO_SRC} -L ${PAHO_SRC} publish.c -lpaho-mqtt3c
_sub:subscribe.c
	${CC} -g -o _sub -I ${PAHO_SRC} -L ${PAHO_SRC} subscribe.c -lpaho-mqtt3c
clean:
	rm -fr _* *.o *.so *.out
