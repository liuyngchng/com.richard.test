#!/usr/bin/python3
# -*- coding: utf-8 -*-
# pip3 install pandas
import sys 
import pandas as pd

# display config
pd.options.display.max_rows = 5 
pd.options.display.max_columns = 5 

#define constant
input_file = '/home/rd/work/rpa/a10.xlsx'
norm_file = 'norm_output.xlsx'
price_col = '价格'
key_col = '序号'
sum_col = '项目金额（账单金额+滞纳金）'
sum_col1 = '项目金额'
main_col = [key_col, '客户名称', '用户号', '账户类型', '燃气费率', '电话', '厂商名称', '地址','业务类型']
del_col = [ '账户类型', '电话', '厂商名称','业务类型', '账单金额', '滞纳金', '税率',  '税额', '不含税金额',
            '余额', '创建人', '创建时间', '商户订单号', 'Unnamed: 0']
output_col = ['客户名称','用户号','燃气费率','地址','办理时间','气量','计费项目','数量','价格','项目金额','合计金额','付款方式','付款方','实缴金额','营业员','终端名称']
footer = pd.DataFrame([['营业部门负责人：','', '', '','审核人：', '','', '', '','','', '', '制表人：', '', '', '']], columns=output_col)
print("[%s] input_file=%s, \r\nnorm_file=%s, \r\nprice_col=%s, \r\nkey_col=%s, \r\nsum_col=%s,\
    \r\nmain_col=%s \r\ndel_col=%s" \
    %(sys._getframe().f_lineno, \
    input_file, norm_file, price_col, key_col, sum_col, main_col, del_col))
# read excel as a dataFrame
dft=pd.read_excel(input_file, index_col=None, skiprows=3)
#print(dft)
dft=dft.iloc[0:dft[dft['客户名称']=='合计'].index[0]]
print(dft)
all_col=set(dft.columns)
other_col = list(all_col -set(main_col))    # get the remained column exinclude mail_col list from all_col
other_col.append(key_col)
print("[%s] other_col=%s" %(sys._getframe().f_lineno, other_col))
#print(dft.columns)

# selet part of the columan as another dataFrame
dft1=dft[main_col]
# fulfill the cell with value above when the merged cell be de-merged
dft4=dft1.fillna(method='ffill')
# create anotehr dataFrame

dft2=dft[other_col]
#dft1.to_excel('test.xlsx'
# merge two dataFrame, the same column be merged automatically
dft3=dft4.merge(dft2, left_on=key_col,right_on=key_col, how='left')
# output the merged dataFrame

#delete some column, rename some column and output to a excel sheet.
print("[%s] before del column, dft3.columns=%s" %(sys._getframe().f_lineno, dft3.columns))
dft3.drop(del_col, axis=1, inplace=True)
print("[%s] after del column, dft3.columns=%s" %(sys._getframe().f_lineno, dft3.columns))
dft3.rename(columns={sum_col: sum_col1, '合计费用': '合计金额'}, inplace=True)
dft3 = dft3[output_col]
dft31=pd.concat([dft3, footer])
with pd.ExcelWriter(norm_file, mode='w', engine="openpyxl") as writer:             # mode ='a' 追加
    dft31.to_excel(writer, sheet_name='A10充值记录')
#dft3.to_excel(norm_file, sheet_name='A10充值记录', merge_cells=True)

dft4=dft3.loc[dft['营业员'].isnull()]
dft41=pd.concat([dft4, footer])
with pd.ExcelWriter(norm_file, mode='a', engine="openpyxl") as writer:             # mode ='a' 追加
    dft41.to_excel(writer, sheet_name='A10终端')

# start filter data in excel
df1=pd.read_excel(norm_file, index_col = price_col)
print("[%s] price_col_uniq= %s" %(sys._getframe().f_lineno,df1.index.unique()))
#filters = ['2.58', '']
df2=df1.filter(like='2.58', axis=0)
sum1 = df2[sum_col1].sum()
print("[%s] 2.58_sum=%s" %(sys._getframe().f_lineno, sum1))
#df2.to_excel('test.xlsx')
