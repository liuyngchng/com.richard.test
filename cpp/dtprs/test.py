#!/usr/bin/python3

import ctypes

# 加载 So 文件
lib = ctypes.CDLL('./libtest.so')

# 调用 So 文件中的函数
result = lib.add(2, 3)

# 打印结果
print(result)
