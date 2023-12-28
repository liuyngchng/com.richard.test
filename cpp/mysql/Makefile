all: _test
_test: mysqltest.c
	gcc -o _test mysqltest.c -lmysqlclient -I /home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include  -L/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/lib
	
clean:
	rm -fr *.out *.so _*