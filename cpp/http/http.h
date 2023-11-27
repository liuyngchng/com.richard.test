

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
 * bodyline, 需要读取body 中的第几行
 **/
char *req(char *ip, int port, char *method, char *path, char *body, char *resp, int n, int bodyline);