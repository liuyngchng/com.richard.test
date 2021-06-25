#!/usr/local/bin/python3.7
# -*- coding:utf-8 -*-
import jiagu
import sys
import codecs

file = codecs.open(sys.argv[1], 'r','utf-8')
print(sys.argv[1])
text = str(file.readlines())
print(text)
knowledge = jiagu.knowledge(text)
print(knowledge)
