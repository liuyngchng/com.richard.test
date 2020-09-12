# 1. ubuntu18 修改mysql数据库密码
```
sudo vim /etc/mysql/my.cnf
```
添加如下内容：
```
[mysqld]
skip-grant-tables=1
```
重启mysql服务
```
sudo service mysql restart
```
登录mysql
```
mysql
```
切换到mysql数据库,并修改root用户密码
```
use mysql;
update user set plugin='mysql_native_password' where user='root';
update user set authentication_string=password('123456') where user='root';
flush privileges;
exit;
```
修改配置并重启mysql服务
```
sudo sudo vim /etc/mysql/my.cnf把skip-grant-tables删除掉
sudo service mysql restart
```
