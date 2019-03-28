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
# 2. Docker
| CMD | NOTE |
| --- |  --- |
| yum install docker -y     | setup docker      |
| dockerd &                 | startup dockerd   |
| docker pull centos        | pull centos image |
执行
```
docker images
```
看到  

| REPOSITORY | TAG | IMAGE ID | CREATED | SIZE |
|     ---    | --- |   ---    |   ---   |  --- |
| docker.io/centos | latest | 9f38484d220f | 13 days ago | 202 MB |
执行
```
docker run -dit image_id
docker ps
```
看到

| CONTAINER ID | IMAGE | COMMAND | CREATED | STATUS | PORTS | NAMES |
|    ---       |  ---  |   ---   |  ---    |  ---   |  ---  |  ---  |
| 11bd69099a06 | 9f38484d220f | "/bin/bash" | 10 minutes ago | Up 10 minutes |  | hardcore_curie |
执行

| CMD | NOTE |
| --- | ---  |
| docker rename hardcore_curie test | 重命名容器 |
| docker exec -it test bash         | 进入容器   |
| vi /root/.bashrc                  | 配置环境变量，重新进入容器依然有效 |
| export PATH=$PATH:/opt/jre        | 配置 java 环境变量 |
| exit                              | 退出容器  |
docker cp jre.tar.gz test:/opt      # 将容器外的文件拷贝到容器里

提交 container 生成新的 image

| CMD | NOTE |
| --- | ---  |
| docker ps | 获取 CONTAINER ID |
| docker commit container_id richard/test | 提交更改，生成新的镜像 |
| docker images | 获取 IMAGE ID |
|docker rmi  image_id | 删除 image |
