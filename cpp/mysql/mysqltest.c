/**
 * gcc mysqltest.c -lmysqlclient -I /home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include  -L/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/lib
 * 开发文档
 * https://dev.mysql.com/doc/c-api/8.2/en/c-api-introduction.html
 **/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include/mysql.h"

#define MAX_CONNECTIONS 100;

const char 	*host 	= "127.0.0.1";
const char 	*user 	= "root";
const char 	*pwd 	= "P@$$W0rd123";
const char 	*db 	= "test";
const int 	port 	= 3306;

struct ConnectionPool {
	int maxConnections;
	int connecCount;
	MYSQL **connections;
	MYSQL **freeConnections;
	int index;
};

int getdt() {
    printf("mysql client version: %s\n",mysql_get_client_info());
    MYSQL *my = mysql_init(NULL); 
    if(0 == mysql_real_connect(my, host, user, pwd, db, port, NULL, 0)){
        printf("mysql conenct failed!\n");
        return -1;
    }
    mysql_set_character_set(my, "utf8");
    printf("mysql connected!\n");
    char *select_sql = "select id, a, b from a limit 5";
    if (0 != mysql_query(my, select_sql)){
        printf("execute failed, %s\n", select_sql);
        return -2;
    }
    printf("execute success, %s\n", select_sql);
    MYSQL_RES *res = mysql_store_result(my);
    int rows = mysql_num_rows(res);
    int cols = mysql_num_fields(res);
    MYSQL_FIELD *fields = mysql_fetch_field(res);
    for (int i = 0; i < cols; i++) {
        printf("%s\t", fields[i].name);
    }
    printf("\n");
    for (int i = 0; i < 3; i++) {
        MYSQL_ROW row = mysql_fetch_row(res);
        for (int j = 0; j < cols; j++) {
            printf("%s\t",row[j]);
        }
        printf("\n");
        if (i>10) {
            break;
        }
    }
    printf("read data finish\n");
    mysql_free_result(res);
    mysql_close(my);
    printf("connection closed\n");
    return 0;
}

int savedt() {
    MYSQL *my = mysql_init(NULL);
    if (0 == mysql_real_connect(my, host, user, pwd, db, port, NULL, 0)) {
        printf("mysql conenct failed!\n");
        return 1;
    }
    mysql_set_character_set(my, "utf8");
    printf("mysql connected!\n");
    char *sql = "insert into test.a(a,b) values ('testa', 'testb')";
    int result = mysql_query(my, sql);
    if (0 != result){
        printf("execute failed, %s\n", sql);
        return 2;
    }
    printf("execute success, %s, %d\n", sql, result);
    printf("save data finish\n");
    mysql_close(my);
    printf("connection closed\n");
    return 0;
}

int updatedt() {
    MYSQL *my = mysql_init(NULL);
    if(0 == mysql_real_connect(my, host, user, pwd, db, port, NULL, 0)){
        printf("mysql conenct failed!\n");
        return 1;
    }
    mysql_set_character_set(my, "utf8");
    printf("mysql connected!\n");
    char *sql = "update test.a set a='testa1' where a='testa' limit 1;";
    int result = mysql_query(my, sql);
    if (0 != result){
        printf("execute failed, %s\n", sql);
        return 2;
    }
    printf("execute success, %s, %d\n", sql, result);
    printf("update data finish\n");
    mysql_close(my);
    printf("connection closed\n");
    return 0;
}

int deletedt() {
    MYSQL *my = mysql_init(NULL);
    if(0 == mysql_real_connect(my, host, user, pwd, db, port, NULL, 0)){
        printf("mysql conenct failed!\n");
        return 1;
    }
    mysql_set_character_set(my, "utf8");
    printf("mysql connected!\n");
    char *sql = "delete from test.a where a='testa1' limit 1;";
    int result = mysql_query(my, sql);
    if (0 != result){
        printf("execute failed, %s\n", sql);
        return 2;
    }
    printf("execute success, %s, %d\n", sql, result);
    printf("delete data finish\n");
    mysql_close(my);
    printf("connection closed\n");
    return 0;
}

/**
 * 创建连接池
 */
struct ConnectionPool *createConnectionPool() {
	struct ConnectionPool *ppool = (struct ConnectionPool*)malloc(sizeof(struct ConnectionPool));
	ppool->maxConnections = MAX_CONNECTIONS;
	ppool->connecCount = 0;
	ppool->connections = (MYSQL**)malloc(sizeof(MYSQL*) * ppool->maxConnections);
	ppool->freeConnections = (MYSQL**)malloc(sizeof(MYSQL*) * ppool->maxConnections);
	ppool->index = 0;
	return ppool;
}


/**
 * 获取一个可用连接
 */
MYSQL *getConnection(struct ConnectionPool *pool){
	MYSQL *pcon = NULL;
	if(pool->index == 0) {
		pcon = pool->connections[pool->index++]; //首先获取第一个用户连接
	} else {
		pcon = pool->freeConnections[pool->index];
	}
	return pcon;
}

/**
 * 连接释放
 */
void releaseConnection(struct ConnectionPool *pool, MYSQL* pconn){
	pool->freeConnections[pool->index++] = pconn;
}

/**
 * 销毁连接池
 */
void destroyConnectionPool(struct ConnectionPool *pool){
	if (pool) {
		free((void*)pool->connections);
		free((void*)pool->freeConnections);
		free(pool);
	}
}

int main() {
	getdt();
}
