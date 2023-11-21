#include <stdio.h>
#include <string.h>
void read_line(char *s, char *t, int n, int l) {
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
