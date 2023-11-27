#include <stdio.h>
#include <string.h>
#include "peer.h"
#include "util.h"

// HTTP/1.1 200 
// Set-Cookie: JSESSIONID=FCC697ED16FE18D3FA21F951BF39E0AE; Path=/industry; HttpOnly
// Content-Type: application/json;charset=UTF-8
// Transfer-Encoding: chunked
// Date: Thu, 23 Nov 2023 06:00:57 GMT

// 206
// {"code":200,"message":"test"}

void getbody(char *s, char *t, int n) {
    int i = 0;
    int j = 0;
    int k = 0;      // start flag
    for (; i < strlen(s); i++){
        if(i<3){
            continue;
        }
        if(j>=n){
            break;
        }
        if (k){
            t[j++] = s[i];
            continue;
        }
        if (s[i-3] == '\r' && s[i-2] == '\n' && s[i-1] == '\r' && s[i] == '\n') {
            k=1;
        }
        continue;
    }
    t[j]='\0';
}


/* 
GET / HTTP/1.1
Host: localhost:8083
User-Agent: curl/7.68.0
Accept: text/html
Content-Type:application/json; 
*/


/* 
POST /v2/api/?login HTTP/1.1
Accept:text/html
Accept-Encoding: gzip, deflate, br
Host: passport.baidu.com

username=admin&password=admin 
*/


char *req(char *ip, int port, char *method, char *path, char *body, char *resp, int n, int bodyline) {
    char req[1024] = {0};
    char header[512]={0};
    sprintf(header, 
        "%s %s HTTP/1.1\r\n"
        "Host: %s:%d\r\n"
        "Content-Type: application/json\r\n",
        method, path, ip, port
    );
    if (method[0]=='G' && method[1]=='E'){
        sprintf(req, 
            "%s"
            "Content-Length:0\r\n\r\n",
            header
        );
    } else {
        sprintf(req, 
            "%s"
            "Content-Length:%ld\r\n\r\n"
            "%s",
            header, strlen(body), body
        );
    }
    printf("[%s][%s-%d]req msg\n%s\n", gettime(),filename(__FILE__), __LINE__, req);
    char raw_resp[4096] = {0};
    writemsg(ip, port, req, raw_resp);
    char raw_body[4096] = {0};
    getbody(raw_resp, raw_body, sizeof(raw_body));
    getln(raw_body, resp, n, bodyline);
    return resp;
}