#include <stdio.h>
#include <string.h>

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

/**
 * http request 
 **/
void httpreq(char *method, char *host, char *path, char *param, char *result){



}