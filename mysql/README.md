#1. install mysql on ubuntu 16.04 LTS
```
sudo apt-get install mysql-server mysql-client
sudo apt-get install mysql-workbench
```
#2. install mysql8 on Ubuntu20.04 LTS
## 2.1 install
```
sudo apt-get install mysql-server
```
## 2.2 log in with no password
```
sudo  mysql -uroot -p
```
## 2.3 set password
```
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'your_password';
FLUSH PRIVILEGES;
exit;
```
## 2.4 login with your password
```
mysql -uroot -p

```
input your password
