CC=gcc
#CFLAGS += -D__SSL1_0__
SOURCE_FILE= gtk_notebook.c
all:_game
_game: ${SOURCE_FILE}
	${CC} -g ${CFLAGS} -o _gtk ${SOURCE_FILE} `pkg-config --cflags --libs gtk+-2.0`
clean:
	rm -fr _* *.o *.so *.out

