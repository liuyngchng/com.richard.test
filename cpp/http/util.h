//windows:
// #define filename(x) strrchr(x,'\\')?strrchr(x,'\\')+1:x
//linux :
#define filename(x) strrchr(x,'/')?strrchr(x,'/')+1:x

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
 * 获取当前时间
 **/
// char* gettime(char *t);


/**
 * 获取当前时间
 **/
char* gettime();