//windows:
// #define filename(x) strrchr(x,'\\')?strrchr(x,'\\')+1:x
//linux :
#define filename(x) strrchr(x,'/')?strrchr(x,'/')+1:x

#define _SRV_PORT_      8083
#define _SRV_IP_        "127.0.0.1"
#define _BACKLOG_       10

/**
 * 获取 http request method
 **/
void getmethod(char *s, char *t, int n);

/**
 * 获取 http request uri
 **/
void geturi(char *s, char *t, int n);

/**
 * 从字符 s 中读取第 l(从0开始)行，最多读取 n 个字符,
 * 数据保存在数组 t 中
 */
void getln(char *s, char *t, int n, int l);

/**
 * 获取 http response body
 * s, source str,
 * t, target str,
 * n, 返回的字符串最大长度
 **/
void getbody(char *s, char *t, int n);

/**
 * 获取当前时间
 **/
char *gettime();

