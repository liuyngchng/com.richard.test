// g++ func1.cpp func2.cpp -o func

#include <iostream>
using namespace std;
 
// declare func1(), defined in fun1.cpp
int func1();
 
int main()
{
    int a = func1();
   cout << a << endl; // 输出 Hello World
   return 0;
}
