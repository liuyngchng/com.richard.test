#!/usr/bin/python3
# -*- coding: utf-8 -*-
# pip3 install pandas
import pandas as pd

# display config
pd.options.display.max_rows = 999
pd.options.display.max_columns =100 

# 使用dtype，指定某一列的数据类型
df1=pd.read_excel('test.xls', index_col='姓名', sheet_name='成绩',dtype={'姓名': str, '成绩': float}) # 使用index_col=0，指定第1列作为索引列
df2=pd.read_excel('test.xls', index_col=None, sheet_name='汇总') # 使用index_col=0，指定第1列作为索引列
print("original")
print(df1)
print("type(df1)=", end='')
print(type(df1))
df1.drop('我多余', axis=1, inplace =True)
df1['成绩'] = df1['成绩'].map(lambda x: 60 if x=='59.0' else x)
print('after changed')
print(df1)
print('begin filter')
df3=df1.filter(like='王', axis=0)
print(df3)
df2['总成绩']=df2.类型.apply(lambda x: df1.成绩.loc[df1.类型== x].sum())
print("type(df2)=", end='')
print(type(df2))
with pd.ExcelWriter('output.xlsx') as writer:             # mode ='a' 追加 
    df2.to_excel(writer, engine='xlsxwriter',sheet_name='Sheet_name_3')
#df2['平均成绩']=df2.类型.apply(lambda x: df1.成绩.loc[df1.类型== x].average())
print(df2)
