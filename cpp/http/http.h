/**
 * 从数组 s 中读取第 l(从0开始)行，最多读取 n 个字符,
 * 数据保存在数组 t 中
 */
void readline(char *s, char *t, int n, int l);

/**
 * 获取 http request method
 **/
void getmethod(char *s, char *t, int n);

/**
 * 获取 http request uri
 **/
void geturi(char *s, char *t, int n);

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
 * 发起 http request
 **/
void httpreq(char *method, char *url, char *param, char *result);
