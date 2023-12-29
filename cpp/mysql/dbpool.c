/**
 * gcc mysqltest.c -lmysqlclient -I /home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include  -L/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/lib
 * 开发文档
 * https://dev.mysql.com/doc/c-api/8.2/en/c-api-introduction.html
 **/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "const.h"

struct conpool {
	/**
	 * size connection count in the pool
	 */
	int size;

	/**
	 * count of busy connection
	 * busy in range [0, size-1]
	 */
	int busy;

	/**
	 * the state of connections, 0 for idle, 1 for busy
	 */
	int flag[MAX_CONNECTIONS];

	/**
	 * a connection pool with busy and idle connections
	 */
	MYSQL **cons;
};

MYSQL *init_con() {
	MYSQL *my = mysql_init(NULL);
	if (0 == mysql_real_connect(my, _HST, _USR, _PWD, _DB, _PRT, NULL, 0)) {
		printf("mysql con init failed\n");
		exit(-1);
	}
	return my;
}


/**
 * 创建连接池
 */
struct conpool *createConPool() {
	struct conpool *ppool = (struct conpool*)
		malloc(sizeof(struct conpool));
	ppool->size 	= MAX_CONNECTIONS;
	ppool->busy 	= 0;
	ppool->cons 	= (MYSQL**)malloc(sizeof(MYSQL*) * ppool->size);
	int flag_size = sizeof(ppool->flag)/sizeof(int);
	int i;
	for (i = 0; i < flag_size;i++) {
		ppool->flag[i]=0;
	}
	return ppool;
}

/**
 * init connection pool
 */
struct conpool *init_pool() {
	struct conpool *ppool= createConPool();
	int i;
	for (i=0; i < ppool->size; i++) {
		MYSQL *my = init_con();
		ppool->cons[i]= my;
	}
	printf("init %d cons in pool\n", ppool->size);
	return ppool;
}

/**
 * 获取一个可用连接
 */
MYSQL *get_con(struct conpool *ppool) {
	MYSQL *pcon = NULL;
	int i;
	for(i = 0; i < ppool->size; i++) {
		if (!ppool->flag[i]) {
			pcon = ppool->cons[i];
			ppool->flag[i]=1;
			ppool->busy++;
			break;
		}
	}
	return pcon;
}

/**
 * 连接释放
 */
void release_con(struct conpool *ppool, MYSQL *pcon) {
	int i;
	for(i=0; i< ppool->size; i++) {
		if(pcon == ppool->cons[i]) {
			ppool->flag[i]=0;
			ppool->busy--;
			break;
		}
	}
}

/**
 * 销毁连接池
 */
void destroy_pool(struct conpool *ppool) {
	if (ppool) {
		int i;
		for(i = 0; i < ppool->size; i++) {
			mysql_close(ppool->cons[i]);
		}
		free(ppool->cons);
		free(ppool);
	}
}

int main() {
	struct conpool *ppool = init_pool();
	printf("ppool init ,size %d\n", ppool->size);
	MYSQL *con1, *con2, *con3;
	con1= get_con(ppool);
	printf("get con con1 %p\n",con1);
	con2 = get_con(ppool);
	printf("get con con2 %p\n",con2);
	con3 = get_con(ppool);
	printf("get con con3 %p\n",con3);
	release_con(ppool, con3);
	printf("release con3 %p", con3);
	destroy_pool(ppool);
	getchar();
}
