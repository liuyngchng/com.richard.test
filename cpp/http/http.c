#include <stdio.h>
#include "tcp.h"

void readline(char *s, char *t, int n, int l) {
    int i=0;    // s index
    int j=0;    // l index
    int k=0;    // t index
    for (int i=0;i<strlen(s);i++) {
        if (j == l) {
            if (s[i] == '\n' || s[i] == '\r') {
                break;
            } else {
                if (k < n) {
                    t[k++]=s[i];
                } else {
                    break;
                }
            }
        } else if (s[i] == '\n' || s[i] == '\r') {
            j++;
        }
    }
    t[k]='\0';
}

void getmethod(char *s, char *t, int n) {
    int i = 0;
    for (; i < strlen(s); i++){
        if(i>= n) {
            break;
        }
        if (s[i] == '\n' || s[i] == '\r' || s[i] == ' ') {
                break;
        } else {
            t[i]=s[i];
        }
    }
    t[i]='\0';
}


void geturi(char *s, char *t, int n) {
    int i=0;    // index for s
    int j=0;    // count for space
    int k=0;    // index for t
    for (; i < strlen(s); i++){
        if (j==1) {
            if (s[i]==' ') {
                break;
            } else {
                if (k < n) {
                    t[k++]=s[i];
                } else {
                    break;
                }
            }
        } else if (s[i]==' ') {
            j++;
        }
    }
    t[k]='\0';
}


// HTTP/1.1 200 
// Set-Cookie: JSESSIONID=FCC697ED16FE18D3FA21F951BF39E0AE; Path=/industry; HttpOnly
// Content-Type: application/json;charset=UTF-8
// Transfer-Encoding: chunked
// Date: Thu, 23 Nov 2023 06:00:57 GMT

// 206
// {"code":200,"message":"test"}
void readbody(char *s, char *t, int n) {
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


char *req(char *ip, int port, char *method, char *path, char *body, char *resp, int n) {
    char req[1024] = {0};
    sprintf(req, 
        "%s %s HTTP/1.1\r\nHost: %s:%d\r\nContent-Type: application/json\r\nContent-Length:%ld\r\n\r\n%s",
        method,path,ip, port, strlen(body),body
    );
    printf("[%s-%d] req_msg\n%s\n",filename(__FILE__), __LINE__, req);
    char raw_resp[4096] = {0};
    writemsg(ip, port, req, raw_resp);
    char raw_body[4096] = {0};
    readbody(raw_resp, raw_body, sizeof(raw_body));
    readline(raw_body, resp, n, 2);
    return resp;
}