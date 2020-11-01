syntax on                   "自动语法高亮
set number                  "显示行号
set tabstop=4               "设定 tab 长度为 4
set paste
set expandtab
"set softtabstop=4          "使得按退格键时可以一次删掉 4 个空格
set autoindent
set smartindent             "依据上面的对起格式，智能的选择对起方式，对于类似C语言编写上很有用
set shiftwidth=4
set hlsearch                "搜索时高亮显示被找到的文本
set incsearch               "查询时非常方便，如要查找book单词，当输入到/b时，会自动找到第一
                            "个b开头的单词，当输入到/bo时，会自动找到第一个bo开头的单词，依
                            "次类推，进行查找时，使用此设置会快速找到答案，当你找要匹配的单词
                            "时，别忘记回车
filetype on                 "检测文件的类型
set history=1000            "记录历史的行数

