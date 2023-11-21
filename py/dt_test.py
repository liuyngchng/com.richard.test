#!/usr/bin/python3

import ctypes

# 加载.so文件
lib = ctypes.CDLL('/home/rd/workspace/com.richard.test/cpp/libdt_parser.so')
print(lib.prs_up_rpt_dt("123"))
# 定义函数原型
lib.prs_up_rpt_dt.argtypes = ctypes.c_char_p
lib.prs_up_rpt_dt.restype = ctypes.c_char_p

# 调用函数
result = lib.prs_up_rpt_dt("88C3BB1BE26238800101000000000359201001010493125DC20D000D000300030180032311041132240102000100000000000000000000000000000000000000000000000000000000000000000000000000000000419F999A42CA999A00000000000000000000000000000DD85AFFC0000E000000016367419309CC020000000000000000231103000000000000000000000000000000002311020000000000000000000000000000000023110100000000000000000000000000000000")
print(result)