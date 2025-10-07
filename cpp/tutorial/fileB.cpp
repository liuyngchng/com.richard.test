#include <iostream>
using namespace std;

extern int i;          //声明i而非定义
extern int j;     //定义j而非声明，会报错，多重定义
//int j;                //错误，重定义，注意这里的j是在全局范围内声明
extern double max(double d1,double d2); //声明

int main()
{
    i = 10;
    j = 20;
    cout << i << ", " << j << endl;
}
