#include<string.h>
//windows:
// #define filename(x) strrchr(x,'\\')?strrchr(x,'\\')+1:x
//linux :
#define filename(x) strrchr(x,'/')?strrchr(x,'/')+1:x

/**
 * 启动 TCP server
 **/
int startsrv();


/**
 * 连接 ip:port 表示的 host,发送内容 msg
 **/
int writemsg(char *ip, int port, char *req, char *resp);