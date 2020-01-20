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
在 docker 容器中启动 MySQL
```
docker run --privileged -dit --name test1  centos /usr/sbin/init
docker exec -it centos bash
systemctl start mysqld
A temporary password is generated for root@localhost: eiDIxJi8s1)h
mysql -uroot -p
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
## 2.1 Install docker
| CMD | NOTE |
| --- |  --- |
| yum install docker -y     | setup docker      |
| dockerd &                 | startup dockerd   |
| docker pull centos        | pull centos image |
## 2.2 start
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

## 2.3 生成新的 image
提交 container 生成新的 image

| CMD | NOTE |
| --- | ---  |
| docker ps | 获取 CONTAINER ID |
| docker commit container_id richard/test | 提交更改，生成新的镜像 |
| docker images | 获取 IMAGE ID |
|docker rmi  image_id | 删除 image |

## 2.4 导出及导入 image
### 2.4.1 导出tar
| CMD | NOTE |
| --- |  --- |
| docker images | 获取 REPOSITORY |
| docker save richard/test -o ./test.tar | 导出为 tar 包 |
| docker load -i ./test.tar              | 导入 tar 包 |

### 2.4.2 导出img文件
| CMD | NOTE |
| --- | ---  |
| docker images | get image id |
| docker save 62cfce4d2e9a > /opt/aaa.img | output img file |
| docker load < ~/images/aaa.img | load img file |

## 2.5 端口映射
执行端口映射时，会调用 docker-proxy 命令，为操作系统创建软链  

| CMD | NOTE |
| --- |  --- |
| cat /usr/lib/systemd/system/docker.service \ grep proxy | 查找安装目录 |
| ln -s /usr/libexec/docker/docker-proxy-current /usr/bin/docker-proxy | 建立软链 |
| docker run -dit -p 9088:9088 image bash | 启动 |
## 2.6 修改默认镜像存储目录
CentOS 下 docker 默认的存储路径在 /var/lib/docker下面。  
```
docker info | grep dir -i
```
修改docker的systemd的 docker.service的配置文件                                             
不知道 配置文件在哪里可以使用systemd 命令显示一下.  
```
systemctl disable docker
systemctl enable docker
#显示结果
Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
```
| CMD | NOTE |
| --- |  --- |
| vim /usr/lib/systemd/system/docker.service | 修改配置文件 |
| ExecStart=/usr/bin/dockerd --graph /data/docker | 在里面的EXECStart的后面增加 --graph /data/docker |
| systemctl disable docker | disable |
| systemctl enable docker  | enable |
| systemctl daemon-reload  | reload |
| systemctl start docker   | start |
## 2.7 限制 container 使用的 CPU 和 内存
| CMD | NOTE |
| --- | ---  |
| docker run -dit --rm --cpuset-cpus="1,3" -m=2g 9a5f12155efd bash | 限制使用编号为1，3的 CPU， 内存限制使用 2GB |
| yum install -y stress | 安装压力工具 |
| stress -c 8 | 启动 8 个任务不停地执行 sqrt() |
| top           | 按 1 键，查看各个 CPU 的利用率，验证 CPU 限制是否生效 |
| docker stats | 查看  MEM USAGE / LIMIT ，验证配置是否生效 |

## 2.8 为容器设置固定的 IP 地址
```
启动Docker容器的时候，使用默认的网络是不支持指派固定IP的，如下
docker run -itd --net bridge --ip 172.17.0.10 centos:latest /bin/bash
6eb1f228cf308d1c60db30093c126acbfd0cb21d76cb448c678bab0f1a7c0df6
docker: Error response from daemon: User specified IP address is supported on user defined networks only.
```
| CMD | NOTE |
| --- |  --- |
| docker network create --subnet=172.18.0.0/16 mynetwork | 创建自定义网络 |
| docker network ls | 查看自定义网络 |
| docker run -itd --name networkTest1 --net mynetwork --ip 172.18.0.2 centos:latest /bin/bash | 启动容器 |

## 2.9 gdb in docker
linux 内核为了安全起见，采用了Seccomp(secure computing)的沙箱机制来保证系统不被破坏   
它能使一个进程进入到一种“安全”运行模式，该模式下的进程只能调用4种系统调用（system calls），  
即read(), write(), exit()和sigreturn()，否则进程便会被终止。
docker只有以--security-opt seccomp=unconfined的模式运行container才能利用GDB调试  
```
docker run --security-opt seccomp=unconfined -dit image_id
```  
## 2.10 "No manual entry for xx" in docker
By default the centos containers are built using yum's nodocs  
注释掉这个选项，重新安装 rpm 包即可  
```
docker exec -it container_id bash
vim /etc/yum.conf
```
注释掉 tsflags=nodocs  
```
#tsflags=nodocs
```
重新安装 rpm 包  
```
rpm -qa | xargs yum reinstall -y
```
退出容器, 提交修改到镜像  
```
exit
docker commit bbb046a8fefe image_repository
```
# 3. Setup Redis
```
yum install -y epel-release
yum install -y redis

```
# Install MySQL in windows
```
unzip mysql-8.0.16-winx64.zip
config ENV for ./mysql-8.0.16-winx64/bin/
mysqld.exe --initialize-insecure
mysqld.exe --install
net start mysql
```
# 4. Setup MySQL on ubuntu
```
sudo apt-get install mysql-server
mysqld
cd /etc/mysql
cat debian.cnf
```
user = debian-sys-maint
password = xedvSNKdLavjuEWV
```
mysql -udebian-sys-maint -pxedvSNKdLavjuEWV
show databases;
use mysql;
update user set authentication_string=PASSWORD("自定义密码") where user='root';
update user set plugin="mysql_native_password";
flush privileges;
quit;
```
restart MySQL
```
/etc/init.d/mysql restart
```
# 5. 查看挂载的硬盘
```
fdisk -l
lsblk
```
# 6. yun
download rpm package only
```
yum install --downloadonly --downloaddir=/opt/rpms mysql
```
# 7. make a iso start up flash disk  

需要把下载的Ubuntu安装文件（.iso）转换成(.dmg)格式的文件,方便在Mac OS上面进行操作，转换命令
```
cd Downloads/
hdiutil convert -format UDRW -o ubuntu.dmg ubuntu-14.04.5-desktop-amd64.iso
```
hdiutil转换的文件后缀名为.dmg,所以需要把文件重命名为.iso，在安装的时候系统才能够更好的识别
```
mv ubuntu.dmg ubuntu.iso
```

打开终端，输入
```
diskutil list
```
记录下U盘的地址
然后卸载U盘命令
```
diskutil unmountDisk [硬碟位置]
```
开始刻录
```
sudo dd if=/path/to/xxx.iso of=/path/to/USB bs=1m; sync
```
输入密码后开始等待刻录完成
然后移除U盘
```
diskutil eject /path/to/USB
```
# 8. WebService Client Generation Error with JDK8
```$xslt
java.lang.AssertionError: org.xml.sax.SAXParseException;
systemId: jar:file:/path/to/glassfish/modules/jaxb-osgi.jar!/com/sun/tools/xjc/reader/xmlschema/bindinfo/binding.xsd;
lineNumber: 52; columnNumber: 88; schema_reference:
Failed to read schema document 'xjc.xsd',
because 'file' access is not allowed due to restriction set by the accessExternalSchema property.
```
Create a file named jaxp.properties (if it doesn't exist) under /path/to/jdk1.8.0/jre/lib and then write this line in it:
```$xslt
javax.xml.accessExternalSchema = all
```
# 9. use iphone as usb internet modem in ubuntu

```
sudo apt-get install ipheth-utils libimobiledevice-dev libimobiledevice-utils
```
# 10. setup atom in ubuntu 16.04
## 10.1 add source
```
sudo add-apt-repository ppa:webupd8team/atom  
sudo apt-get update  
sudo apt-get install atom
```
## 10.2 deb package
```
wget https://github.com/atom/atom/releases/download/v1.43.0/atom-amd64.deb
wget https://github.com/atom/atom/releases/download/v1.7.4/atom-amd64.deb
sudo dpkg -i atom-amd64.deb
```
# 11. pandoc
```
sudo apt-get install pandoc
sudo apt-get install texlive-lang-cjk texlive-latex-extra texlive-xetex
pandoc test.md -o test.docx
pandoc test.md -o test.pdf 
```
# 12. tcp info is OS
```
cd /proc/sys/net/ipv4
ls -al | grep tcp
```
