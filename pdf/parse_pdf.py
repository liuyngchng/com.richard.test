import pdfplumber
import pandas as pd
# 读取pdf文件，返回pdfplumber.PDF类的实例
pdf = pdfplumber.open("/Users/rd/Documents/test.pdf")
# 通过pdfplumber.PDF类的metadata属性获取pdf信息
pdf.metadata
# 通过pdfplumber.PDF类的metadata属性获取pdf页数
len(pdf.pages)
# 第一页pdfplumber.Page实例
first_page = pdf.pages[0]

# 查看页码
print('页码：',first_page.page_number)

# 查看页宽
print('页宽：',first_page.width)

# 查看页高
print('页高：',first_page.height)
# 读取文本
text = first_page.extract_text()
print(text)

#import pandas as pd

# 第二页pdfplumber.Page实例
first_page = pdf.pages[0]

# 自动读取表格信息，返回列表
table = first_page.extract_tables()

# 将列表转为df
table_df = pd.DataFrame(table[1:],columns=table[0])

table_df
