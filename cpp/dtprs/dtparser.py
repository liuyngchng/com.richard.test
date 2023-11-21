#!/usr/bin/python3

import ctypes

# 定义C函数的返回类型和参数类型
prs_dt = ctypes.CDLL("./libdtparser.so").prs_dt
prs_dt.restype = ctypes.c_char_p
prs_dt.argtype = ctypes.c_char_p
# 加载.so文件
lib = ctypes.CDLL('./libdtparser.so')
# print('lib.so file loaded')
# 定义函数原型
# lib.prs_dt.restype=ctypes.c_char_p

# 调用函数
input="88C3BB1BE26238800101000000000359201001010493125DC20D000D000300030180032311041132240102000100000000000000000000000000000000000000000000000000000000000000000000000000000000419F999A42CA999A00000000000000000000000000000DD85AFFC0000E000000016367419309CC020000000000000000231103000000000000000000000000000000002311020000000000000000000000000000000023110100000000000000000000000000000000"
result = prs_dt(input.encode("utf-8"))
# 获取结果
# print(result)
result_string = result.decode("utf-8")
print(result_string)

