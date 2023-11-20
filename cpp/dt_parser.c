#include <stdio.h>
#include <string.h>

char* prs_up_rpt_dt(char* s);
char* prs_dt_u(char* s); 
char* prs_up_rpt_dt_obj(char* id, char* s); 
char* prs_evt_ext(char* s);
char* prs_at_ext(char* s);

char* prs_up_rpt_dt(char* s) {
    static char dt[1024];
    memset(dt,0,sizeof(dt));
    int i=0;
    strcat(dt, "{\"cr\":\"");
    strncat(dt, s+i, 2);
    i+=2;
    strcat(dt, "\",\"ctrl\":\"");
    strncat(dt, s+i, 2);
    static char ctrl[3];
    memset(ctrl, 0, sizeof(ctrl));
    strncat(ctrl, s+i, 2);
    i+=2;
    strcat(dt, "\",\"l\":\"");
    strncat(dt, s+i, 2);
    i+=2;
    strcat(dt, "\",\"c\":\"");
    strncat(dt, s+i, 4);
    i+=4;
    strcat(dt, "\",\"r\":\"");
    strncat(dt, s+i, 4);
    i+=4;
    strcat(dt, "\",\"seq\":\"");
    strncat(dt, s+i, 2);
    static char seq[3];
    memset(seq,0,sizeof(seq));
    strncat(seq, s+i, 2);
    i+=2;
    printf("[seq]%s\n", seq);
    if(s[i-1]=='0') {
        strcat(dt, "\",\"appId\":\"");
        strncat(dt, s+i, 46);
        i+=46;
    }
    strcat(dt, "\",\"dt\":\"");
    strcat(dt, s+i);
    strcat(dt, "\"}");
    // return dt json
    printf("[dt]%s\n", dt);
    //return dt;
    if (ctrl[1] == '2' && seq[1] =='1') {
        static char dt_obj[512];
        memset(dt_obj,0,sizeof(dt));
        strcat(dt_obj, "{\"ext\":{\"dt\":\"");
        strcat(dt_obj, s+i);
        strcat(dt_obj, "\"}}");
        return dt_obj;
    }
    return prs_dt_u(s+i);
}

char* prs_dt_u(char* s) {
    static char dt_u[1024];
    int i=0;
    memset(dt_u,0,sizeof(dt_u));
    strcat(dt_u, "{\"ctrl\":\"");
    strncat(dt_u, s+i, 2);
    i+=2;
    strcat(dt_u, "\",\"n\":\"");
    strncat(dt_u, s+i, 2);
    i+=2;
    strcat(dt_u, "\",\"id\":\"");
    strncat(dt_u, s+i, 4);
    static char id[5];
    memset(id,0,sizeof(id));
    strncat(id, s+i, 4);
    i+=4;
    strcat(dt_u, "\",\"dt\":\"");
    strcat(dt_u, s+i);
    strcat(dt_u, "\"}");
    // return data union
    printf("[dt_u]%s\n", dt_u);
    //return dt_u;
    return prs_up_rpt_dt_obj(id, s+i);
}
    
char* prs_up_rpt_dt_obj(char* id, char* s) {
    static char dt[1024];
    int i=0;
    memset(dt,0,sizeof(dt));
    strcat(dt, "{\"time\":\"");
    strncat(dt, s+i, 12);
    i+=12;
    strcat(dt, "\",\"sts\":\"");
    strncat(dt, s+i, 8);
    i+=8;
    strcat(dt, "\",\"iVStd\":\"");
    strncat(dt, s+i, 16);
    i+=16;
    strcat(dt, "\",\"iV\":\"");
    strncat(dt, s+i, 16);
    i+=16;
    strcat(dt, "\",\"fVStd\":\"");
    strncat(dt, s+i, 16);
    i+=16;
    strcat(dt, "\",\"fV\":\"");
    strncat(dt, s+i, 16);
    i+=16;
    strcat(dt, "\",\"flwRtStd\":\"");
    strncat(dt, s+i, 8);
    i+=8;
    strcat(dt, "\",\"flwRt\":\"");
    strncat(dt, s+i, 8);
    i+=8;
    strcat(dt, "\",\"t\":\"");
    strncat(dt, s+i, 8);
    i+=8;
    strcat(dt, "\",\"p\":\"");
    strncat(dt, s+i, 8);
    i+=8;
    strcat(dt, "\",\"engSum\":\"");
    strncat(dt, s+i, 16);
    i+=16;
    strcat(dt, "\",\"engDst\":\"");
    strncat(dt, s+i, 8);
    i+=8;
    strcat(dt, "\",\"acsV\":\"");
    strncat(dt, s+i, 4);
    i+=4;
    strcat(dt, "\",\"vlt\":\"");
    strncat(dt, s+i, 4);
    i+=4;
    strcat(dt, "\",\"pwr\":\"");
    strncat(dt, s+i, 2);
    i+=2;
    strcat(dt, "\",\"nb\":\"");
    strncat(dt, s+i, 8);
    i+=8;
    strcat(dt, "\",\"ecl\":\"");
    strncat(dt, s+i, 4);
    i+=4;
    strcat(dt, "\",\"cid\":\"");
    strncat(dt, s+i, 12);
    i+=12;
    strcat(dt, "\",\"rnf\":\"");
    strncat(dt, s+i, 4);
    i+=4;
    printf("[obj_id]%s\n", id);
    strcat(dt, "\",\"ext\":");
    if (id[3] == '3') {
        strcat(dt, prs_evt_ext(s+i));
    } else if (id[3] == '2') {
        strcat(dt, prs_at_ext(s+i));
    }
    strcat(dt, "}");
    return dt;
} 
char* prs_evt_ext(char*s) {
    static char dt[1024];
    int i=0;
    memset(dt,0,sizeof(dt));
    strcat(dt, "{\"evt\":\"");
    strncat(dt, s+i, 2);
    i+=2;
    strcat(dt, "\",\"fvStdS1\":\"");
    strncat(dt, s+i, 16);
    i+=16;
    strcat(dt, "\",\"dt\":[");
    for(int j=0; j<3; j++) {
        char tmp[128];
        memset(tmp, 0, sizeof(tmp));
        if(j>0){
            strcat(tmp, ",");
        }
        strcat(tmp, "{\"date\":\"");
        strncat(tmp, s+i, 6);
        i+=6;
        strcat(tmp, "\",\"fVStd\":\"");
        strncat(tmp, s+i, 16);
        i+=16;
        strcat(tmp, "\",\"fV\":\"");
        strncat(tmp, s+i, 16);
        i+=16;
        strcat(tmp, "\"}");
        strcat(dt, tmp);
    }
    strcat(dt, "]}");
    return dt;

}
char* prs_at_ext(char *s) {
    static char dt[1024];
    int i=0;
    memset(dt, 0, sizeof(dt));
    strcat(dt, "{\"date\":\"");
    strncat(dt, s+i, 6);
    i+=6;
    strcat(dt, "\",\"dt\":\"");
    strcat(dt, s+i);
    strcat(dt, "\"}");
    return dt;
 }
int main(int argc, char* argv[]) {
    if(NULL == argv[1]) {
        printf("pls input dt\n");
        return -1;
    }
    printf("prs_dt, %s\n", argv[1]);
    printf("%s\n", prs_up_rpt_dt(argv[1]));
    return 0;
}
