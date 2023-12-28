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
	int maxCon;
	// active connection / busy connection
	int conCount;
	// a connection pool with busy and free connection
	MYSQL **cons;
	// free connection which is not busy, all cons are free connection in initial state
	MYSQL **freeCons;
	/**
	 * a index for fetching connection which is idle
	 * current index for freeCons which is ready and can be fetched next
	 */
	int index;
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
	ppool->maxCon 	= MAX_CONNECTIONS;
	ppool->conCount = 0;
	ppool->cons 	= (MYSQL**)malloc(sizeof(MYSQL*) * ppool->maxCon);
	ppool->freeCons = (MYSQL**)malloc(sizeof(MYSQL*) * ppool->maxCon);
	ppool->index 	= 0;
	return ppool;
}

/**
 * init connection pool
 */
struct ConPool *init_pool() {
	struct ConPool *ppool= createConPool();
	for (int i=0; i < ppool->maxCon; i++) {
		MYSQL *my;
		init_con(my);
		ppool->cons[i]= my;
		ppool->freeCons[i]= my;
		printf("init %d conn\n", i);
	}
	return ppool;
}

/**
 * 获取一个可用连接
 */
MYSQL *getCon(struct ConPool *pool) {
	MYSQL *pcon = NULL;
	if (pool->index == 0) {
		pcon = pool->cons[pool->index++]; //首先获取第一个用户连接
	} else {
		pcon = pool->freeCons[pool->index];
	}
	return pcon;
}

/**
 * 连接释放
 */
void releaseCon(struct ConPool *pool, MYSQL *pconn) {
	pool->freeCons[pool->index++] = pconn;
}

/**
 * 销毁连接池
 */
void destroyConPool(struct ConPool *pool) {
	if (pool) {
		free((void*)pool->cons);
		free((void*)pool->freeCons);
		free(pool);
	}
}

int main() {
	struct ConPool *ppool = init_pool();
	printf("ppool init ,size %d\n", ppool->maxCon);
	getchar();
}
