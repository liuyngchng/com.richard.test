#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "util.h"
#include "dtparser.h"
#include "http.h"

// decode data
void decdt(char *resp);

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
            char tmp[512]={0};
            decdt(tmp);
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

void decdt(char *resp){
    // char resp[8096] = {0};
    // myhttpreq(resp);
    char *ip = _CODEC_IP_;
    int port = _CODEC_PORT_;
    char *method="POST";
    char *path="/industry/api/upMsgProcess";
    char *bd = "{\"data\":\"6800EB03008188C1E000E1E55B9988776655443300110101000000000013FFFFFFFF03149A0FEC0100010001010000000000000000584AD45FB958C43F878129C981AA7587AC372EF3C21116C07E8EC7A55BB0BEE5BB50467015B1A81172D66EDD9B169E0C07311B501EB6EEBA5FB0A6D86BD1E15CEF058E5EBE6E57391861501FFA388EB61EB817A691A4DC92274579EAEAE681C9DC49F83CF6C7E5CD6631291A28D15DE8144EF11806CFC5C8D965282448E1F558A68852B243A1F657B2B68FC2E7DD721B8A1EE1254C36D8C7C3CAB9E08B2F7DBF8CA378C9477FC86D08A98B480D5E62464E189361000016\",\"transId\":\"T20211212091414-89F4BE38C6964238\",\"deviceId\":\"89F4BE38C6964238\",\"timestamp\":1635756025000}";
    // char reqeust[1024]={0};
    // req(resp);
    req(ip, port, method, path, bd, resp, sizeof(resp), 2);
    printf("[%s][%s-%d]resp body\n%s\n",gettime(), filename(__FILE__), __LINE__, resp);
};