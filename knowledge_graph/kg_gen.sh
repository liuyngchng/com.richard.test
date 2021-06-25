#! /bin/bash
file=$1
echo 'filename='$file
soffice --headless --convert-to txt $file --cat |  awk '{{printf"%s,",$0}}' | sed 's/\s\+/,/g' | sed 's/,\+/,/g'|xargs ./kg.py
