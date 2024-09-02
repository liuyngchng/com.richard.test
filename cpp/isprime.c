#include<stdio.h>
#include<stdlib.h>

int isprime(int num);

int main()
{
     int i=0,N=3;
     while(i<2800){//生成前2800个素数
        if(!isprime(N)){
           i++;
           printf("%d\t",N);
        }
        N++;
    }
    system("pause");
    return 0;
}

int isprime(int num)
{
    int i;

    for(i=2;i<=num;i++)

       if(num%i==0){
          if(num!=i)
             return 1;//不是素数
          else
             return 0;//是素数
       }
}
