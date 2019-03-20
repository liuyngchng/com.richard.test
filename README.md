# 1. Setup MySQL on CentOS
## 1.1 Setup
```
卸载  先停掉mysql进程   没有安装过的可以直接跳过
pkill -9 mysqld
rpm -qa|grep -i mysql
卸载
yum -y remove mysql-community-client-5.6.38-2.el7.x86_6
下载mysql的repo源 这个安装的mysql5.7.20  /**纠正一下，这源下载的是最新的版本  ****/
cd /usr/local/src/
wget http://repo.mysql.com/mysql57-community-release-el7-8.noarch.rpm
rpm -ivh mysql57-community-release-el7-8.noarch.rpm
yum -y install mysql-server 
```
## 1.2 默认配置文件路径 
```
配置文件：/etc/my.cnf 
日志文件：/var/log/var/log/mysqld.log 
服务启动脚本：/usr/lib/systemd/system/mysqld.service 
socket文件：/var/run/mysqld/mysqld.pid
```
## 1.3 StartUp
启动mysql服务
```
service mysqld restart
```
重置密码
```
grep "password" /var/log/mysqld.log 
```
A temporary password is generated for root@localhost: ab*******m1
```
mysql -hlocalhost -uroot -p
alter user 'root'@'localhost' identified by '1$%4!Qw*;,';
```
