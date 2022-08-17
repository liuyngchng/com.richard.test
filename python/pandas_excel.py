#!/usr/bin/python3
# -*- coding: utf-8 -*-
# pip3 install pandas
import sys 
import pandas as pd

# display config
pd.options.display.max_rows = 999
pd.options.display.max_columns =100 

#define constant
original_file= '/home/rd/work/rpa/a10.xlsx'
norm_file = 'norm_output.xlsx'
main_col = ['序号', '客户名称', '用户号', '账户类型', '燃气费率', '电话', '厂商名称', '地址','业务类型']
# read excel as a dataFrame
dft=pd.read_excel(original_file, index_col=None, skiprows=3, skipfooter=28)
all_col=set(dft.columns)
other_col = list(all_col -set(main_col))    # get the remained column exinclude mail_col list from all_col
other_col.append('序号')
print(sys._getframe().f_lineno, end = '_')
print(other_col)
#print(dft.columns)

# selet part of the columan as another dataFrame
#dft1=dft[['序号', '客户名称', '用户号', '账户类型', '燃气费率', '电话', '厂商名称', '地址','业务类型']]
dft1=dft[main_col]
# fulfill the cell with value above when the merged cell be de-merged
dft1.fillna(method='ffill',inplace=True)
# create anotehr dataFrame

dft2=dft[other_col]
#dft1.to_excel('test.xlsx')
# merge two dataFrame, the same column be merged automatically
dft3=dft1.merge(dft2, left_on='序号',right_on='序号', how='left')
# output the merged dataFrame
dft3.to_excel(norm_file)
#print(dft)

# start filter data in excel
df1=pd.read_excel(norm_file, index_col='价格')
print(sys._getframe().f_lineno, end = '_')
print(df1.index.unique())
#filters = ['2.58', '']
df2=df1.filter(like='2.58', axis=0)
sum1 = df2['合计费用'].sum()
print(sys._getframe().f_lineno, end = '_')
print('2.58_sum=', end=' ')
print(sum1)
#df2.to_excel('test.xlsx')
