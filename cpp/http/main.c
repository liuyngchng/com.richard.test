
#include<stdio.h>
#include "tcp.h"
#include "http.h"
#include "util.h"

// http request test
char *myhttpreq(char *response);

void httpreq();
void httpreq1();

int main(int argc, char* argv[]) {
    char t[64]={0};
    gettime(t);
    printf("%s\n", t);
    // char *s = "GET / HTTP/1.1";
    // char *s = "key1=value1&key2=value2&key3=value3";
    // char t[20] = {0};
    // readline(s, t, sizeof(t), 2);
    // getmethod(s, t, 10);
    // geturi(s, t, 10);
    // printf("t, %s\n", t);
    //  startsrv();
    // char resp[8096]={0};
    // myhttpreq(resp);
    httpreq1();
//    httpreq();
    
    return 0;
}

void httpreq1(){
    char resp[8096] = {0};
    // myhttpreq(resp);
    char *ip = "127.0.0.1";
    int port = 8083;
    char *method="POST";
    char *path="/";
    char *bd = "123";
    // char reqeust[1024]={0};
    // req(resp);
    req(ip, port, method, path, bd, resp, sizeof(resp), 2);
    printf("[%s][%s-%d]read body\n%s\n",gettime(), filename(__FILE__), __LINE__, resp);
}

void httpreq(){
    char resp[8096] = {0};
    // myhttpreq(resp);
    char *ip = "11.10.36.2";
    int port = 19061;
    char *method="POST";
    char *path="/industry/api/upMsgProcess";
    char *bd = "{\"data\":\"6800EB03008188C1E000E1E55B9988776655443300110101000000000013FFFFFFFF03149A0FEC0100010001010000000000000000584AD45FB958C43F878129C981AA7587AC372EF3C21116C07E8EC7A55BB0BEE5BB50467015B1A81172D66EDD9B169E0C07311B501EB6EEBA5FB0A6D86BD1E15CEF058E5EBE6E57391861501FFA388EB61EB817A691A4DC92274579EAEAE681C9DC49F83CF6C7E5CD6631291A28D15DE8144EF11806CFC5C8D965282448E1F558A68852B243A1F657B2B68FC2E7DD721B8A1EE1254C36D8C7C3CAB9E08B2F7DBF8CA378C9477FC86D08A98B480D5E62464E189361000016\",\"transId\":\"T20211212091414-89F4BE38C6964238\",\"deviceId\":\"89F4BE38C6964238\",\"timestamp\":1635756025000}";
    // char reqeust[1024]={0};
    // req(resp);
    req(ip, port, method, path, bd, resp, sizeof(resp), 2);
    printf("[%s][%s-%d]read body\n%s\n",gettime(), filename(__FILE__), __LINE__, resp);
}

char *myhttpreq(char *response) {
    // char *ip = "127.0.0.1";
    // int port = 8083;
    // char *req = "GET / HTTP/1.1\r\nHost: localhost:8083\r\nAccept:application/json\r\n\r\n";

    char *ip = "11.10.36.2";
    int port = 19061;
    char *body = "{\"data\":\"6800EB03008188C1E000E1E55B9988776655443300110101000000000013FFFFFFFF03149A0FEC0100010001010000000000000000584AD45FB958C43F878129C981AA7587AC372EF3C21116C07E8EC7A55BB0BEE5BB50467015B1A81172D66EDD9B169E0C07311B501EB6EEBA5FB0A6D86BD1E15CEF058E5EBE6E57391861501FFA388EB61EB817A691A4DC92274579EAEAE681C9DC49F83CF6C7E5CD6631291A28D15DE8144EF11806CFC5C8D965282448E1F558A68852B243A1F657B2B68FC2E7DD721B8A1EE1254C36D8C7C3CAB9E08B2F7DBF8CA378C9477FC86D08A98B480D5E62464E189361000016\",\"transId\":\"T20211212091414-89F4BE38C6964238\",\"deviceId\":\"89F4BE38C6964238\",\"timestamp\":1635756025000}";
    char *header = "POST /industry/api/upMsgProcess HTTP/1.1\r\nHost: 11.10.36.2:19061\r\nContent-Type: application/json\r\n";
    char req[1024] = {0};
    char len[8] = {0};
    sprintf(len, "%ld", strlen(body));
    strcat(req, header);
    strcat(req, "Content-Length:");
    strcat(req, len);
    strcat(req, "\r\n\r\n");
    strcat(req, body);
    printf("write msg\n%s\n", req);
    writemsg(ip, port, req, response);
    printf("\nread msg\n%s\n", response);
    return response;
}


