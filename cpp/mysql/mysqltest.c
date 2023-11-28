
/**
 * gcc mysqltest.c -lmysqlclient -I /home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include  -L/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/lib
 **/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "/home/rd/software/mysql-8.0.28-linux-glibc2.12-x86_64/include/mysql.h"


const char *host = "127.0.0.1";
const char *user = "test";
const char *passwd = "test";
const char *db = "ind";
const int port = 3306;


int main(){   
    //显示MySQL客户端库版本
    printf("mysql client version: %s\n",mysql_get_client_info());
    MYSQL *my = mysql_init(NULL); 
 
    // 链接数据库
    if(0 == mysql_real_connect(my, host, user, passwd, db, port, NULL, 0)){
        printf("mysql 连接失败!\n");
        return 1;
    }
 
    // 设置连接的编码也是utf8
    mysql_set_character_set(my, "utf8");
    printf("mysql 连接成功!\n");
	
    //增删改
    char *sql = "insert into user values (4,21, \'123\')";
    mysql_query(my, sql);
    // std::string sql = "update user set age=18 where id=1";
    // std::string sql = "delete from user where id=4";
 
    // 查找所有的数据
    // 1. 执行查询语句
    char *select_sql = "select id, acc_vol,status from act_info limit 5";
    if (0 != mysql_query(my, select_sql)){
        printf("execute failed, %s\n", select_sql);
        return 2;
    }
    printf("execute success, %s\n", select_sql);
    
    // 2. 获取查询结果数据
    MYSQL_RES *res = mysql_store_result(my);
 
    // 3. 数据的行列信息
    // 获取结果行数
    int rows = mysql_num_rows(res);
    // 获取列数
    int cols = mysql_num_fields(res);
 
    // 获取所有的列属性
    MYSQL_FIELD *fields = mysql_fetch_field(res);
    // for (int i = 0; i < cols; i++)
    for (int i = 0; i < cols; i++){
        // 获取列名，通常没什么用
        printf("%s\t", fields[i].name);
    }
    printf("\n");
    for(int i = 0; i < 3; i++) {
        MYSQL_ROW row = mysql_fetch_row(res);
        for(int j = 0; j < cols; j++) {
            printf("%s\t",row[j]);
        }
        printf("\n");
        if (i>10){
            break;
        }
        
    }
    printf("read data finish\n");
    free(res); //不要忘记释放空间

    mysql_close(my);
    printf("connection closed\n");
    return 0;
}
