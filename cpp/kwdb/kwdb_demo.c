/*
 * kaiwudb_demo.c
 * connect kaiwudb from unix odbc with driver P
 *
 *  Created on: Oct 29, 2024
 *  Author: rd
 *
 */

#include <stdio.h>
#include <sql.h>
#include <sqlext.h>

int main() {
	SQLHENV hEnv = NULL;
    SQLHDBC hDbc = NULL;
    SQLHSTMT hStmt = NULL;
    SQLRETURN retcode;
    SQLLEN id;
    SQLCHAR menuID[32];
    SQLCHAR menuName[128];

    // 分配环境句柄
    SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE, &hEnv);
    // 设置环境属性
    SQLSetEnvAttr(hEnv, SQL_ATTR_ODBC_VERSION, (void*)SQL_OV_ODBC3, 0);
    // 分配连接句柄
    SQLAllocHandle(SQL_HANDLE_DBC, hEnv, &hDbc);
    // 连接数据库
    SQLRETURN ret;
    // 输入正确的用户名和密码
    ret = SQLConnect(hDbc, (SQLCHAR*)"kwdb", SQL_NTS, (SQLCHAR*)"user", SQL_NTS, (SQLCHAR*)"password", SQL_NTS);
    if (ret != SQL_SUCCESS) {
        printf("Failed to connect the database, error code %d\n\n", ret);
        return -1;
    }
    else {
        printf("Connected to the database\n\n");
    }
    SQLHSTMT hStmt3 = NULL;
    SQLCHAR* create3 = (SQLCHAR*)"CREATE TABLE if not exists b(a varchar); ";
    SQLAllocHandle(SQL_HANDLE_STMT, hDbc, &hStmt3);
    ret = SQLExecDirect(hStmt3, create3, SQL_NTS);
    if (ret != SQL_SUCCESS) {
        printf("execute failed, %s\n", create3);
        return -1;
    }
    else {
        printf("execute success, %s\n", create3);
    }

    SQLHSTMT hStmt4 = NULL;
    SQLCHAR* query4 = (SQLCHAR*)"INSERT INTO b VALUES ('中文读取'); ";
    SQLAllocHandle(SQL_HANDLE_STMT, hDbc, &hStmt4);
    ret = SQLExecDirect(hStmt4, query4, SQL_NTS);
    if (ret != SQL_SUCCESS) {
        printf("execute failed, %s\n", query4);
        return -1;
    } else {
        printf("execute success, %s\n", query4);
    }
    SQLHSTMT hStmt9 = NULL;
    SQLCHAR* query9 = (SQLCHAR*)"select * from b;";
    // 分配语句句柄
    SQLAllocHandle(SQL_HANDLE_STMT, hDbc, &hStmt9);
    // 执行查询
    ret = SQLExecDirect(hStmt9, query9, SQL_NTS);
	if (ret != SQL_SUCCESS) {
	   printf("execute failed, %s\n", query9);
	   return -1;
	} else {
	   printf("execute success, %s\n", query9);
	   ret = SQLBindCol(hStmt9, 1, SQL_C_CHAR, &menuID, sizeof(menuID), NULL);

	   // 检索数据
	   int row = 0;
	   while ((ret = SQLFetch(hStmt9)) == SQL_SUCCESS) {
		   printf("row %d, column a: %s\n", row++, menuID);
	   }

	   if (ret == SQL_NO_DATA) {
		   printf("No more data\n");
	   } else {
		   printf("Failed to fetch data\n");
	   }
   }

   // 释放句柄
	SQLFreeHandle(SQL_HANDLE_STMT, hStmt4);
	SQLFreeHandle(SQL_HANDLE_STMT, hStmt9);

	SQLDisconnect(hDbc);
	SQLFreeHandle(SQL_HANDLE_DBC, hDbc);
	SQLFreeHandle(SQL_HANDLE_ENV, hEnv);

   return 0;
}
