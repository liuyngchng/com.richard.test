#!/usr/local/bin/python3.7
# -*- coding:utf-8 -*-
import jiagu
import sys

print(sys.argv[1])
text = str(sys.argv[1])
print(text)
print(' ')
knowledge = jiagu.knowledge(text)
print(knowledge)
