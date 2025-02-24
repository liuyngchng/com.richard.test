/**
 * 连接 ip:port 表示的 host,发送报文 req, 接收返回的报文保存至 resp
 **/
int writemsg(char *ip, int port, char *req, char *resp);