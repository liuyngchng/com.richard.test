#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "util.h"
#include "dtparser.h"

int dispatch(char *req, char *resp) {
    char l0[100]={0};
    char method[5]={0};
    char uri[50]={0};
    char body[1024] = {0};
    getln(req, l0, sizeof(l0), 0);
    
    getbody(req, body, sizeof(body));
    getmethod(l0, method, sizeof(method));
    geturi(l0, uri, sizeof(uri));
    printf(
        "[%s][%s-%d]method [%s], uri [%s], body\n%s\n", 
        gettime(), filename(__FILE__), __LINE__, method, uri, body
    );
    char *respbd;            // response body
    
    if (strncmp(method, "GET", 3)==0){
        char tmp[512]={0}; 
        sprintf(tmp, 
            "{\"uri\":\"%s\",\"timestamp\":\"%s\",\"status\":200}",
            uri ,gettime()
        );
        respbd=tmp;
    } else {
        char *prsdt="/prs/dt";
        if(strncmp(uri, prsdt, strlen(prsdt))==0) {
            printf("[%s][%s-%d]%s matched\n", gettime(), filename(__FILE__), __LINE__, prsdt);
            respbd= prs_dt(body);
        } else {
            respbd= "{\"status\":200}";
        }
    }
    sprintf(resp,
        "HTTP/1.1 200 OK\r\n"
        "Content-Length: %ld\r\n\r\n"
        "%s",
        strlen(respbd),respbd
    );
    return strlen(resp);
}