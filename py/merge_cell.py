# -*- coding: utf-8 -*-
"""
created on 20170301
 
@author: ark-z
"""
import xlsxwriter
import pandas as pd
 
class my_dataframe(pd.dataframe):
  def __init__(self, data=none, index=none, columns=none, dtype=none, copy=false):
    pd.dataframe.__init__(self, data, index, columns, dtype, copy)
 
  def my_mergewr_excel(self,path,key_cols=[],merge_cols=[]):
    # sheet_name='sheet1', na_rep='', float_format=none, columns=none, header=true, index=true, index_label=none, startrow=0, startcol=0, engine=none, merge_cells=true, encoding=none, inf_rep='inf', verbose=true):
    self_copy=my_dataframe(self,copy=true)
    line_cn=self_copy.index.size
    cols=list(self_copy.columns.values)
    if all([v in cols for i,v in enumerate(key_cols)])==false:   #校验key_cols中各元素 是否都包含与对象的列
      print("key_cols is not completely include object's columns")
      return false
    if all([v in cols for i,v in enumerate(merge_cols)])==false: #校验merge_cols中各元素 是否都包含与对象的列
      print("merge_cols is not completely include object's columns")
      return false  
 
    wb2007 = xlsxwriter.workbook(path)
    worksheet2007 = wb2007.add_worksheet()
    format_top = wb2007.add_format({'border':1,'bold':true,'text_wrap':true})
    format_other = wb2007.add_format({'border':1,'valign':'vcenter'})
    for i,value in enumerate(cols): #写表头
      #print(value)
      worksheet2007.write(0,i,value,format_top)
     
    #merge_cols=['b','a','c']
    #key_cols=['a','b']
    if key_cols ==[]:  #如果key_cols 参数不传值，则无需合并
      self_copy['rn']=1
      self_copy['cn']=1
    else:
      self_copy['rn']=self_copy.groupby(key_cols,as_index=false).rank(method='first').ix[:,0] #以key_cols作为是否合并的依据
      self_copy['cn']=self_copy.groupby(key_cols,as_index=false).rank(method='max').ix[:,0]
    #print(self)
    for i in range(line_cn):
      if self_copy.ix[i,'cn']>1:
        #print('该行有需要合并的单元格')
        for j,col in enumerate(cols):
          #print(self_copy.ix[i,col])
          if col in (merge_cols):  #哪些列需要合并
            if self_copy.ix[i,'rn']==1: #合并写第一个单元格，下一个第一个将不再写
              worksheet2007.merge_range(i+1,j,i+int(self_copy.ix[i,'cn']),j, self_copy.ix[i,col],format_other) ##合并单元格，根据line_set[7]判断需要合并几个
              #worksheet2007.write(i+1,j,df.ix[i,col])
            else:
              pass
            #worksheet2007.write(i+1,j,df.ix[i,j])
          else:
            worksheet2007.write(i+1,j,self_copy.ix[i,col],format_other)
          #print(',')
      else:
        #print('该行无需要合并的单元格')
        for j,col in enumerate(cols):
          #print(df.ix[i,col])
          worksheet2007.write(i+1,j,self_copy.ix[i,col],format_other)
         
         
    wb2007.close()
    self_copy.drop('cn', axis=1)
    self_copy.drop('rn', axis=1)
