#include "global_var.h"
#include <iostream>

extern int a;
using namespace std;

int def()
{
    a=10;
    //cout << a << endl;
    return 0;
}
