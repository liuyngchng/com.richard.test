#include <string.h>
#include <stdio.h>
#include <time.h>
#include "util.h"

void getmethod(char *s, char *t, int n) {
    int i = 0;
    for (; i < (int)strlen(s); i++){
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
    for (; i < (int)strlen(s); i++){
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

void getln(char *s, char *t, int n, int l) {
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

char *gettime()
{
    struct tm *ptr; 
    time_t lt;
    lt =time(NULL);
    ptr = localtime(&lt);
    char* tmp = asctime(ptr);
    tmp[strlen(tmp)-1]=0;
    return tmp;
}

