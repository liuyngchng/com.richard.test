/**
 * gcc mysqltest.c -lmysqlclient -I /home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include  -L/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/lib
 * 开发文档
 * https://dev.mysql.com/doc/c-api/8.2/en/c-api-introduction.html
 **/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "const.h"
#include "/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include/mysql.h"

MYSQL *get_connection() {
    printf("mysql client version, %s\n",mysql_get_client_info());
    MYSQL *my = mysql_init(NULL);
    if (0 == mysql_real_connect(my, _HST, _USR, _PWD, _DB, _PRT, NULL, 0)) {
        printf("mysql conenct failed!\n");
        exit(-1);
    }
    printf("mysql server version, %s\n", mysql_get_server_info(my));
    return my;
}

int select_dt() {
    MYSQL *my = get_connection();
    mysql_set_character_set(my, "utf8");
    printf("mysql connected!\n");
    char *select_sql = "select id, a, b from a limit 5";
    if (0 != mysql_query(my, select_sql)) {
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
    printf("select data finish\n");
    mysql_free_result(res);
    mysql_close(my);
    printf("connection closed\n");
    return 0;
}

int insert_dt() {
    MYSQL *my = get_connection();
    mysql_set_character_set(my, "utf8");
    printf("mysql connected!\n");
    char *sql = "insert into test.a(a,b) values ('testa', 'testb')";
    int result = mysql_query(my, sql);
    if (0 != result) {
        printf("execute failed, %s\n", sql);
        return 2;
    }
    printf("execute success, %s, %d\n", sql, result);
    printf("insert data finish\n");
    mysql_close(my);
    printf("connection closed\n");
    return 0;
}

int update_dt() {
    MYSQL *my = get_connection();
    mysql_set_character_set(my, "utf8");
    printf("mysql connected!\n");
    char *sql = "update test.a set a='testa1' where a='testa' limit 1;";
    int result = mysql_query(my, sql);
    if (0 != result) {
        printf("execute failed, %s\n", sql);
        return 2;
    }
    printf("execute success, %s, %d\n", sql, result);
    printf("update data finish\n");
    mysql_close(my);
    printf("connection closed\n");
    return 0;
}

int delete_dt() {
    MYSQL *my = get_connection();
    mysql_set_character_set(my, "utf8");
    printf("mysql connected!\n");
    char *sql = "delete from test.a where a='testa1' limit 1;";
    int result = mysql_query(my, sql);
    if (0 != result) {
        printf("execute failed, %s\n", sql);
        return 2;
    }
    printf("execute success, %s, %d\n", sql, result);
    printf("delete data finish\n");
    mysql_close(my);
    printf("connection closed\n");
    return 0;
}

int main() {
    select_dt();
}
