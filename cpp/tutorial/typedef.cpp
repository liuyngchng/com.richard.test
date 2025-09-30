# include <iostream>

using namespace std;

int main()
{
    typedef int feet;			// 告诉编译器，feet 是 int 的另一个名称
    feet distance;				// 创建了一个整型变量 distance
    distance = 1;
    cout << distance << endl;
}
