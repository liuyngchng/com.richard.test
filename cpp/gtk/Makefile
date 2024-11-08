CC=gcc
#CFLAGS += -D__SSL1_0__
SOURCE_FILE= gtk_arrow.c 
all:_gtk _x11
_gtk: ${SOURCE_FILE}
	${CC} -g ${CFLAGS} -o _gtk ${SOURCE_FILE} `pkg-config --cflags --libs gtk+-2.0`
_x11:
	${CC} -g ${CFLAGS} -o _x11 x11_window.c -lX11
clean:
	rm -fr _* *.o *.so *.out

