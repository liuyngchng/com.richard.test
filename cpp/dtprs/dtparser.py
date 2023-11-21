#!/usr/bin/python3

import ctypes
import sys

def prs_dt(dt):
    '''
    调用 C 的 lib.so文件中的函数
    :param dt: a hex string
    :return: a json string
    '''
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
    result = prs_dt(dt.encode("utf-8"))
    # 获取结果
    # print(result)
    result_string = result.decode("utf-8")
    return result_string

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("pls input dt")
    else:
        dt = prs_dt(sys.argv[1])
        print(dt)
