py begin
from datetime import date, timedelta
c = (date.today() + timedelta(days=-1)).strftime("%Y-%m-%d")
print(c)
py finish
x = py_result
echo `x`
