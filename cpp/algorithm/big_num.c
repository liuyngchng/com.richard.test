#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/**
 * 大数类型
 * 最大值理论上可以是
 * 1e9223372036854775807
 * 99999........99999（中间9223372036854775807个9）
 */
struct bigNum {
    int *arr;  // 位数数组
    long long _arrLen;  // 数组位数的当前最大长度
    long long wei;  // 有多少位 初始是1 表示现在这个数是个位数
} typedef BigNum;

/**
 * 获取一个初始化的大数字对象，值为0
 * @return 大数对象，值为零
 */
BigNum getZero() {
    BigNum res;
    res._arrLen = 2;  // 设定一开始的位数
    res.arr = (int *) malloc(sizeof(int) * res._arrLen);
    res.wei = 1;
    res.arr[0] = 0;
    return res;
}

/**
 * 销毁一个大数对象
 * @param bigNum 大数对象地址
 */
void destroyBigNum(BigNum *bigNum) {
    free(bigNum->arr);
}

/**
 * 给大数对象中的数组扩容，扩容策略是
 * 2, 4, 8, 16 每次空间不够时，都扩充成原来的两倍
 * @param bigNum 需要扩容的大数对象地址
 */
void extendBigNum(BigNum *bigNum) {
    bigNum->_arrLen *= 2;
    bigNum->arr = (int *) realloc(bigNum->arr, sizeof(int) * bigNum->_arrLen);
}

// 整形乘方计算
int myPow(int n, int m) {
    if (m == 0) {
        return 1;
    }
    int res = n;
    for (int i = 1; i < m; i++) {
        res *= n;
    }
    return res;
}

/**
 * 判断m是多少位数
 * @param m
 * @return 如果m是 169 则返回 3
 */
int getIntWei(int m) {
    int n = 0;
    do {
        n++;
        m /= 10;
    } while (m > 0);
    return n;
}

/**
 * 将一个 int 数字转化成 BigNum类型
 * @param int 输入的int类型数
 * @return bigNum 大数对象
 */
BigNum intToBigNum(int integer) {
    int n = getIntWei(integer);
    BigNum res = getZero();
    while (res._arrLen < n) {
        extendBigNum(&res);
    }
    for (int i = 1; i <= n; i++) {
        int dig = (integer % (myPow(10, i))) / myPow(10, (i - 1));
        res.arr[i - 1] = dig;
    }
    res.wei = n;
    return res;
}

/**
 * 将数字字符串转化为大数对象
 * @param string 字符串，如 "1234535543"
 * 字符串只能是纯数字，如果出现其他字符会导致程序bug
 * @return 大数对象
 */
BigNum stringToBigNum(char *string) {
    BigNum res = getZero();
    while (res._arrLen < strlen(string)) {
        extendBigNum(&res);
    }
    for (long long i = 0; i < strlen(string); i++) {
        res.arr[strlen(string) - i - 1] = (int) (string[i]) - 48;
    }
    res.wei = (long long) strlen(string);
    return res;
}

/**
 * 将大数打印成一行打印出来
 * @param bigNum 大数对象地址
 */
void printBigNumLn(BigNum *bigNum) {
    for (long long i = bigNum->wei - 1; i >= 0; i--) {
        printf("%d", bigNum->arr[i]);
    }
    printf("\n");
}

/**
 * 打印这个大数具体的信息
 * 包含了直观表示，内部数组存储结构，位数和当前数组长度。
 * @param bigNum 大数对象地址
 */
void printBigNumObj(BigNum *bigNum) {
    for (long long i = bigNum->wei - 1; i >= 0; i--) {
        printf("%d", bigNum->arr[i]);
    }
    printf(" [");
    for (int i = 0; i < bigNum->wei; i++) {
        printf("%d ", bigNum->arr[i]);
    }
    printf("]");
    printf("arrLen:%lld;wei:%lld\n", bigNum->_arrLen, bigNum->wei);
}

/**
 * 将这个大数对象增加它自己，也就是乘以2倍
 * 输入 231 => 462
 * @param bigN 大数对象地址
 * 无返回值，函数调用直接修改大数对象自身的值
 */
void bigNumAddSelf(BigNum *bigN) {
    // 扩容检测
    if (bigN->arr[bigN->wei - 1] >= 5 && bigN->wei == bigN->_arrLen) {
        extendBigNum(bigN);
    }
    int next = 0;  // 下一位进位现象 0 无进位，1有进位
    for (long long i = 0; i < bigN->wei; i++) {
        int res = bigN->arr[i] * 2 + next;
        if (res < 10) {
            bigN->arr[i] = res;
            next = 0;
        } else {
            bigN->arr[i] = res - 10;
            next = 1;
        }
        // 判断最后是不是多出来一位
    }
    if (next == 1) {
        // printf("up wei\n");
        bigN->arr[bigN->wei] = 1;
        bigN->wei++;
    }
}

/**
 * 把b2里面的内容拷贝到b1里面去
 * 深拷贝
 * copy:  b1 <=== b2
 * @param b1 大数对象地址 接收
 * @param b2 大数对象地址 提供复制
 * 无返回值，函数调用直接修改大数对象 b1 自身的值
 */
void bigNumCopy(BigNum *b1, BigNum *b2) {
    if (b1->_arrLen < b2->_arrLen) {
        // 扩容1
        b1->_arrLen = b2->_arrLen;
        b1->arr = (int *) realloc(b1->arr, sizeof(int) * b1->_arrLen);
    }
    // 先将b1内的数据清零
    for (long long i = 0; i < b1->_arrLen; i++) {
        b1->arr[i] = 0;
    }
    // 一位一位复制
    for (long long i = 0; i < b2->wei; i++) {
        b1->arr[i] = b2->arr[i];
    }
    b1->wei = b2->wei;
}

/**
 * 大数比较大小，
 * @param b1 大数对象地址
 * @param b2 大数对象地址
 * @return 如果 b1 > b2 返回 1 ; 如果 b1 < b2 返回 -1；相等返回 0
 */
int bigNumCmp(BigNum *b1, BigNum *b2) {
    if (b1->wei > b2->wei) {
        return 1;
    } else if (b1->wei < b2->wei) {
        return -1;
    } else {
        // 从高位向低位比较
        for (long long i = b1->wei - 1; i >= 0; i--) {
            if (b1->arr[i] > b2->arr[i]) {
                return 1;
            } else if (b1->arr[i] < b2->arr[i]) {
                return -1;
            }
        }
        return 0;
    }
}

/**
 * 大数相加 有返回值版
 * @param b1 大数对象地址
 * @param b2 大数对象地址
 * @return 返回一个新的大数对象
 */
BigNum bigNumPlus(BigNum *b1, BigNum *b2) {
    // 注意左边的参数的值比右边的要大
    if (bigNumCmp(b1, b2) == -1) {
        BigNum *temp = b1;
        b1 = b2;
        b2 = temp;
    }
    BigNum res = getZero();
    if (b1->wei == b1->_arrLen) {
        extendBigNum(b1); // 最高位刚好达到数组了就直接扩容，不判断了
    }
    bigNumCopy(&res, b1);
    // b2 要扩容到和res一样的水平
    if (b2->_arrLen < res._arrLen) {
        b2->_arrLen = res._arrLen;
        b2->arr = (int *) realloc(b2->arr, sizeof(int) * b2->_arrLen);
        // 多扩容出来的数据要清零
        for (long long i = b2->wei; i < b2->_arrLen; i++) {
            b2->arr[i] = 0;
        }
    }
    while (b2->_arrLen <= res._arrLen) {
        extendBigNum(b2);
    }
    int next = 0;  // 下一位进位现象 0 无进位，1有进位
    for (long long i = 0; i < res.wei; i++) {
        int resNum = b2->arr[i] + res.arr[i] + next;
        if (resNum < 10) {
            res.arr[i] = resNum;
            next = 0;
        } else {
            res.arr[i] = resNum - 10;
            next = 1;
        }
    }
    // 判断最后是不是多出来一位
    if (next == 1) {
        res.arr[res.wei] = 1;
        res.wei++;
    }
    return res;
}

/**
 * 大数相加 无返回值版
 * 把b2加到b1上
 * @param b1 大数对象地址
 * @param b2 大数对象地址
 * 无返回值，直接通过地址修改了b1的值。
 */
void bigNumAdd(BigNum *b1, BigNum *b2) {
    if (b1->wei == b1->_arrLen) {
        extendBigNum(b1); // 最高位刚好达到数组了就直接扩容，不判断了
    }
    int flag = bigNumCmp(b1, b2);
    if (flag == -1) {
        // 后边大，前面小
        while (b1->_arrLen < b2->_arrLen) {
            extendBigNum(b1);
        }
        // 前面0化
        for (long long i = b1->wei; i < b1->_arrLen; i++) {
            b1->arr[i] = 0;
        }
    }
    int next = 0;  // 下一位进位现象 0 无进位，1有进位
    for (long long i = 0; i < b2->wei; i++) {
        int resNum = b1->arr[i] + b2->arr[i] + next;
        if (resNum < 10) {
            b1->arr[i] = resNum;
            next = 0;
        } else {
            b1->arr[i] = resNum - 10;
            next = 1;
        }
    }
    if (b2->wei > b1->wei) {
        b1->wei = b2->wei;
    }
    // 判断最后是不是多出来一位
    if (next == 1) {
        b1->arr[b1->wei]++;
        b1->wei++;
    }
}

/**
 * 将一个大数乘以多少倍数，倍数是个位数(<10)
 * @param bgn 大数对象地址
 * @param a 自觉保证是个位数
 * 无返回值，函数调用直接修改 bgn 的值
 */
void bigNumMulDig(BigNum *bgn, int a) {
    // 乘以0要特殊处理
    if (a == 0) {
        bgn->wei = 1;
        bgn->arr[0] = 0;
        return;
    }
    // 如果这个数最高位刚好顶满了数组，那么可能就会有数组溢出的风险，直接扩容
    if (bgn->wei == bgn->_arrLen) {
        extendBigNum(bgn);
    }
    int next = 0;
    for (long long i = 0; i < bgn->wei; i++) {
        int resNum = bgn->arr[i] * a + next;
        if (resNum < 10) {
            bgn->arr[i] = resNum;
            next = 0;
        } else {
            next = resNum / 10;
            bgn->arr[i] = resNum % 10;
        }
    }
    if (next) {
        bgn->arr[bgn->wei] = next;
        bgn->wei++;
    }
}

/**
 * 将一个大数对象的结尾增加n个零
 * @param bgn 大数对象
 * @param a 增加几个0，如果是1就是扩大十倍，如果是2就是扩大100倍，如果是0就是不扩大
 * 例如：
 *  (大数=1124, a=3)  => 大数变成：1124000
 * 无返回值，函数调用直接修改 bgn 的值
 */
void bigNumAppend0(BigNum *bgn, long long a) {
    if (a <= 0) {
        return;
    }
    while (bgn->wei + a > bgn->_arrLen) {
        extendBigNum(bgn);
    }
    for (long long i = bgn->wei - 1; i >= 0; i--) {
        bgn->arr[i + a] = bgn->arr[i];
    }
    for (int i = 0; i < a; i++) {
        bgn->arr[i] = 0;
    }
    bgn->wei += a;
}

/**
 * 大数乘法 b1 * b2
 * @param b1
 * @param b2
 * @return 返回大数乘法后的结果
 */
BigNum bigNumMul(BigNum *b1, BigNum *b2) {
    BigNum res = getZero();
    // 注意左边的参数的值比右边的要大
    if (bigNumCmp(b1, b2) == -1) {
        BigNum *temp = b1;
        b1 = b2;
        b2 = temp;
    }
    for (long long i = 0; i < b2->wei; i++) {
        // 遍历b2的每一位
        int w = b2->arr[i];
        BigNum newBg = getZero();
        bigNumCopy(&newBg, b1);  // 被乘数b1复制一份
        bigNumMulDig(&newBg, w);
        bigNumAppend0(&newBg, i);
        bigNumAdd(&res, &newBg);
    }
    return res;
}

/**
 * 阶乘
 * @param n 数字
 * @return 阶乘的结果
 */
BigNum jie(int n) {
    BigNum res = getZero();
    res.arr[0] = 1;
    for (int i = 1; i <= n; i++) {
        BigNum bigI = intToBigNum(i);
        res = bigNumMul(&res, &bigI);
        destroyBigNum(&bigI);
    }
    return res;
}

int main() {
    // 大数相加
    BigNum b1 = intToBigNum(1);
    printBigNumObj(&b1);
    BigNum b2 = intToBigNum(95599);
    printBigNumObj(&b2);
    BigNum b3 = bigNumPlus(&b1, &b2);
    printBigNumObj(&b3);

    // 大数乘以个位数
    BigNum bg1 = intToBigNum(11665544);
    bigNumMulDig(&bg1, 6);
    printBigNumObj(&bg1);

    // 大数乘以大数
    BigNum bg3 = intToBigNum(166655466);
    BigNum bg4 = intToBigNum(443335544);
    BigNum bg5 = bigNumMul(&bg3, &bg4);
    printBigNumObj(&bg5);

    // 字符串版 大数乘以大数
    BigNum bgn1 = stringToBigNum("8717829120915684686898899879870");
    BigNum bgn2 = stringToBigNum("1555888888564684684684684888888");
    BigNum bgn3 = bigNumMul(&bgn1, &bgn2);
    printBigNumObj(&bgn3);

    // 遍历输出阶乘结果
    // for (int i = 1; i < 1000; i++) {
    //     BigNum result = jie(i);
    //     printf("!%d\n", i);
    //     // printBigNumObj(&result);
    //     printBigNumLn(&result);
    //     destroyBigNum(&result);
    // }

    // 计算超大阶乘
    BigNum result = jie(6000);
    printBigNumLn(&result);
}

