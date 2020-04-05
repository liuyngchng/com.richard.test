# 1. 创建静态库  
使用ar命令（archive）可以很容易地创建属于自己的静态库。
ar命令一般对.o的目标文件进行操作，目标文件可以由gcc -c命令得到。  
下面就以一个具体的例子来说明一下。
## 1.1 源程序文件  
首先，我们有如下两个源程序文件：  
```
ls
main.c  print.c test.h
pg print.c

#include <stdio.h>

int print()
{
    printf("Hello world\n");
    return 0;
}

pg main.c                                                        
#include "test.h"

int main()
{
    print();
    return 0;
}

pg test.h
int print();
```
## 1.2 生成目标文件    
先通过gcc -c命令将其编译成.o文件:
```
gcc -c *.c                                                        
ls
main.c  main.o  print.c  print.o  test.h
```
我们可以看到两个.o的目标文件已经成功生成。  
这时候，如果我们使用以下命令，是可以直接编译成功的：  
```
gcc -o test *.o
ls
main.c  main.o  print.c  print.o  test.h test
./test
Hello world
```  
## 1.3 创建归档文件  

但是这里由于我们是要创建静态库，所以可以使用ar命令来创建一个归档文件:

```
ar crv libtest.a print.o
a - test2.o
ls
libtest.a  main.c  main.o  print.c  print.o  test  test.h
```
可以看到，静态库libtest.a已经成功创建，  
这时，还需要使用ranlib命令来生成一个内容表，这一步在Unix系统中必不可少，  
但在Linux中，当使用的是GUN开发工具时，这一步可以省略。  
以上步骤完成后，即可以使用下面的命令来编译程序：  

```
ranlib libtest.a
```
## 1.4 链接静态库文件  
```
gcc -o testa main.o -L./ -ltest
ls
libtest.a  main.c  main.o  print.c  print.o  test  testa  test.h
./testa
Hello world
```

通过以上案例，可以发现得到的效果其实是一样的。  
当然，也可以使用以下命令，得到相同的效果（这里因为没有链接头文件，会报一个错，但是结果没有影响）：  

```
gcc -o testb main.c -L./ -ltest  
ls
libtest.a  main.c  main.o  print.c  print.o  test  testa  testb  test.h
 ./testb
Hello world
```
