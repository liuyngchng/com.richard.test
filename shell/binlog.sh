#!/bin/sh
  i=110
  z=`expr $i + 1`
  new=`printf "%06d\n" $i`
  sed -i "s|$i|$z|" binlog.sh
  mv /var/lib/mysql/mysql-bin.${new} /data/binlog/
  echo "${new}"

