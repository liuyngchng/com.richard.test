/**
 * gcc mysqltest.c -lmysqlclient -I /home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include  -L/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/lib
 * 开发文档
 * https://dev.mysql.com/doc/c-api/8.2/en/c-api-introduction.html
 **/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "const.h"

struct ConPool {

	/**
	 * size connection count in the pool
	 */
	int size;

	/**
	 * count of busy connection
	 * buzy in range [0, size-1]
	 */
	int buzy;

	/**
	 * the state of connections, 0 for idle, 1 for busy
	 */
	int flag[MAX_CONNECTIONS];
	/**
	 * a connection pool with busy and idle connections
	 */
	MYSQL **cons;
};

MYSQL *init_con(MYSQL *my) {
	my = mysql_init(NULL);
	if (0 == mysql_real_connect(my, _HST, _USR, _PWD, _DB, _PRT, NULL, 0)) {
		printf("mysql con init failed\n");
		exit(-1);
	}
	return my;
}


/**
 * 创建连接池
 */
struct ConPool *createConPool() {
	struct ConPool *ppool = (struct ConPool*)
		malloc(sizeof(struct ConPool));
	ppool->size 	= MAX_CONNECTIONS;
	ppool->buzy 	= 0;
	ppool->cons 	= (MYSQL**)malloc(sizeof(MYSQL*) * ppool->size);
	for (int i=0; i< sizeof(ppool->flag);i++) {
		ppool->flag[i]=0;
	}
	return ppool;
}

/**
 * init connection pool
 */
struct ConPool *init_pool() {
	struct ConPool *ppool= createConPool();
	for (int i=0; i < ppool->size; i++) {
		MYSQL *my;
		init_con(my);
		ppool->cons[i]= my;
		printf("init %d conn\n", i);
	}
	return ppool;
}

/**
 * 获取一个可用连接
 */
MYSQL *getCon(struct ConPool *ppool) {
	MYSQL *pcon = NULL;
	for(int i = 0; i < ppool->size; i++) {
		if (!ppool->flag[i]) {
			pcon = ppool->cons[i];
			ppool->flag[i]=1;
			break;
		}
	}
	return pcon;
}

/**
 * 连接释放
 */
void releaseCon(struct ConPool *ppool, MYSQL *pconn) {
	for(int i=0; i< ppool->size; i++) {
		if(pconn == ppool->cons[i]) {
			ppool->flag[i]=0;
			break;
		}
	}
}

/**
 * 销毁连接池
 */
void destroyConPool(struct ConPool *ppool) {
	if (ppool) {
		free(ppool->cons);
		free(ppool);
	}
}

int main() {
	struct ConPool *ppool = init_pool();
	printf("ppool init ,size %d\n", ppool->size);
	getchar();
}
