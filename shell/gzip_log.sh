#!/bin/sh
FILE_LIST=`ls *.log`;
FILE_LIST_SIZE=${#FILE_LIST[@]};
echo '*.log file list size is '$FILE_LIST_SIZE
INDEX=0
for f in $FILE_LIST
	do
		let INDEX=INDEX+1;
		echo 'start gzip '$INDEX'/'$FILE_LIST_SIZE' file '$f
		gzip $f
	done

