#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "util.h"
#include "dtparser.h"
#include "http.h"

/**
 * decode data
 * req, request body
 * resp, response body
 **/
void decdt(char *req, char *resp);

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
        "[%s][%s-%d]curl -X%s -s --noproxy '*' 'http://%s:%d%s' "
        "-H 'Content-Type:application/json;charset=UTF-8' -d'%s'\n", 
        gettime(), filename(__FILE__), __LINE__, method, _SRV_IP_, _SRV_PORT_, uri, body
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
        char *updt="/up/dt";
        char *dndt="/dn/dt";
        if(strncmp(uri, prsdt, strlen(prsdt))==0) {
            printf("[%s][%s-%d]%s matched\n", gettime(), filename(__FILE__), __LINE__, prsdt);
            respbd= parsedt(body);
        } else if(strncmp(uri, updt, strlen(updt))==0) {
            char tmp[1024]={0};
            // decdt(body, tmp);
            respbd = tmp;
            /* code */
        } else if(strncmp(uri, dndt, strlen(updt))==0) {
            /* code */
        }
         else {
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


void decdt(char *reqbd, char *resp){
    char *ip = _CODEC_IP_;
    int port = _CODEC_PORT_;
    char *method="POST";
    char *path="/industry/api/upMsgProcess";
    char bd[1024] ={0};
    char *mid
    sprintf(bd, 
        "{\"data\":\"%s\","
            "\"transId\":\"T20211212091414-%s\","
            "\"deviceId\":\"%s\","
            "\"timestamp\":1635756025000}",
            getjsonv("payload"),
        
    );
    // char reqeust[1024]={0};
    // req(resp);
    req(ip, port, method, path, bd, resp, sizeof(resp), 2);
    printf("[%s][%s-%d]resp body\n%s\n",gettime(), filename(__FILE__), __LINE__, resp);
};