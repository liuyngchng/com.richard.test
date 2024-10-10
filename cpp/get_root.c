/*
 * get_root.c
 *
 *  Created on: Oct 10, 2024
 *      Author: rd
 */
#include <stdio.h>
#define ACCURACY 0.000001

/**
 * 计算一个数值的n次方根
 * n, 待开方的数
 * pow, 将开的次方
 */
double get_root(double n, int pow)
{
	double low, high, mid, tmp;
	// 获取上下界
	if (n > 1) {
		low = 1;
		high = n;
	} else {
		low = n;
		high = 1;
	}

	// 二分法求开方

	while (low <= high) {
		mid = (low + high) / 2.000;
		tmp = 1;
		for (int i=0; i< pow; i++) {
			tmp = tmp*mid;
		}
		if (tmp - n <= ACCURACY && tmp -n >= ACCURACY * -1) {
			return mid;
		} else if (tmp > n) {
			high = mid;
		} else {
			low = mid;
		}
	}
	return -1.000;
}

int main(void)
{
	double n, res;
	while (scanf("%lf", &n) != EOF) {
		res = get_root(n, 12);
		printf("%lf\n", res);
	}
	return 0;
}



