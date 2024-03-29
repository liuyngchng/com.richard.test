

/**
 * http 请求参数
 **/
struct param
{
    char *key;
    char *value;
};


/**
 * 获取 http request 参数
 **/
void getparam(char *s, char *param, int n);

/**
 * http request, 最多返回n个字节的报文保存至 resp
 * ip, HTTP 请求头中的 IP 地址;
 * port, HTTP 请求头中的端口;
 * method, HTTP 请求行中的 method;
 * path, HTTP 请求行中的地址;
 * body, HTTP 请求报文体;
 * resp，HTTP 响应报文的报文体;
 * bodyline, HTTP 响应报文报文体中，从第几行开始读取，默认0
 **/
char *req(char *ip, int port, char *method, char *path, char *body, char *resp, int n, int bodyline);