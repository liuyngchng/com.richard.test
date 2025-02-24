LIB_DIR=/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64
all: _dbpool _crud
_crud: crud.c
	gcc -g -o _crud crud.c -lmysqlclient -I ${LIB_DIR}/include  -L${LIB_DIR}/lib

_dbpool: dbpool.c
	gcc -g -o _dbpool dbpool.c -lmysqlclient -I ${LIB_DIR}/include  -L${LIB_DIR}/lib
	
clean:
	rm -fr *.out *.so _*