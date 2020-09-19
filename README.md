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
grep password /var/log/mysqld.log
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
docker run -dit --name test image_id
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
| docker tag img_id name            | 重命名镜像(rename image) |
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
| docker commit container_id richard/test(repository column) | 提交更改，生成新的镜像 |
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
| `docker load < ~/images/aaa.img` | load img file |


## 2.5 端口和目录映射
执行端口映射时，会调用 docker-proxy 命令，为操作系统创建软链  

| CMD | NOTE |
| --- |  --- |
| cat /usr/lib/systemd/system/docker.service \ grep proxy | 查找安装目录 |
| ln -s /usr/libexec/docker/docker-proxy-current /usr/bin/docker-proxy | 建立软链 |
| docker run -dit -p 9088:9088 image bash | 启动 |
| docker run -dit -v /hostdir:/containerdir --name test repository_id | 目录映射 |
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
# 12. TCP control info
## 12.1 TCP info in ubuntu
```
cd /proc/sys/net/ipv4
ls -al | grep tcp
```
## 12.2 use TCP congestion algorithm BBR
update kernel if necessary and config bbr as tcp congestion algorithm

```
wget --no-check-certificate \    'https://github.com/teddysun/across/raw/master/bbr.sh' \   && chmod +x bbr.sh && ./bbr.sh  
```
查看是否开启
```
sysctl net.ipv4.tcp_available_congestion_control
```
显示以下即已开启：
```
sysctl net.ipv4.tcp_available_congestion_control
net.ipv4.tcp_available_congestion_control = bbr cubic reno
```
```
sysctl net.ipv4.tcp_congestion_control
net.ipv4.tcp_congestion_control = bbr
```
查看BBR是否启动
```
lsmod | grep bbr
```
显示以下即启动成功：
```
lsmod | grep bbr
tcp_bbr 20480 14
```
## 12.3 停止BBR  

依次执行下面命令就可以了。   

```
sed -i '/net.core.default_qdisc/d' /etc/sysctl.conf
sed -i '/net.ipv4.tcp_congestion_control/d' /etc/sysctl.conf
sysctl -p       
reboot
```
# 13. mac terminal hostname
```
sudo scutil --set HostName my_host_name
```
# 14. some git things
## 14.1 git中文文件名变数字
修改配置
git config --global core.quotepath false
即可解决

## 14.2 use vim diff as git diff visual tool
```
sudo apt-get install vim
git config --global diff.tool vimdiff
git config --global difftool.prompt false   // 不再弹出 Launch  vimdiff ?
git config --global alias.d difftool        // 为输入方便，difftool输入实在太长， 用别名 d 来替代 difftool
git d your_file                             // enjoy your coding.
```

# 15. create ISO file in ubuntu
## 15.1  create ISO file from CD-ROM
```
sudo umount /dev/cdrom
dd if=/dev/cdrom of=file.iso bs=1024
```
## 15.2 add file or directory to ISO file
需要使用mkisofs这个工具,你想改的参数都可以修改，而且还有-gui这个参数。最简单的用法如下：
```
mkisofs -r -o file.iso your_folder_name/
```
生成一个MD5文件，执行
```
md5sum file.iso > file.iso.md5
```
## 15.3 burn ISO file to CD-ROM
 右键, write to disc...),点击这个选项

# 16. install app in ubuntu docker container
```
apt-get update
apt-get install xxx
```
# 17. convert GBK(gb18030 gbk) text file to readable file in ubuntu (UTF-8 format)

```
 iconv -f gbk -t utf8 gbk.txt > utf8.txt
```
# 18. network traffic monitoring/网络流量监控  
```
iftop -i interface
```
# 19. replace tab  

## 19.1 TAB替换为空格  
```
:set ts=4
:set expandtab
:%retab!
```

## 19.2 空格替换为TAB  

```
:set ts=4
:set noexpandtab
:%retab!
```
## 19.3 删除空白行（delete all blank lines)

```
g/^\s*$/d
```

# 20. compile quant project

to debug the openssl version, download openssl new version,  
add parameter in cmake

```

 cmake ../ -DOPENSSL_ROOT_DIR=/usr/local/ssl -DOPENSSL_LIBRARIES=/usr/local/ssl/lib
```

# 21. delete cache file after 'sudo apt-get install for ubuntu'  
run
```
sudo apt-get clean
```
and then all file in /var/cache/apt/archives be deleted.

# 22. ubuntu support exfat disk format

```
sudo apt-get install exfat-utils
```
# 23. kylin 4.0.2 source list

修改 apt 的源文件管理文件 :  
```
/etc/apt/sources.list
```
， 修改为
```
deb http://archive.kylinos.cn/yhkylin juniper main restricted universe multiverse
deb http://cz.archive.ubuntu.com/ubuntu trusty main
deb [arch=amd64] https://mirrors.ustc.edu.cn/docker-ce/linux/ubuntu trusty stable
```

保存后， 更新源：

apt-get update

# 24. gitlab ce 

## 24.1 docker image

```
docker pull gitlab/gitlab-ce
```

## 24.2
install gitlab
```
https://about.gitlab.com/install/#ubuntu
```
# 25. ubuntu wifi driver setup (wireless card driver)  
```
lspci | grep Wireless
```  
看到无线网卡类型为 
```
Broadcom Inc. and subsidiaries BCM4360 802.11ac Wireless Network Adapter (rev 03)
```
执行
```
apt install firmware-b43-installer
apt-get install bcmwl-kernel-source
```

# 26. rar and unrar in ubuntu with password
```
sudo apt-get install rar unrar -y
rar -p a test.csv.rar test.csv   // input password
unrar -x test.csv.rar 			// input password
```
# 27. disk IO
[userbenchmark](https://ssd.userbenchmark.com)
SSD write speed 1000~2000 MByte per second.
HDD,Hard Disk Drive write speed 100 MByte per second. 

# 28. 设置ubuntu默认登录为非图形化界面

如果想让系统默认不进入图形界面，只需编辑文件  
/etc/default/grub
把原来的  
GRUB_CMDLINE_LINUX_DEFAULT=”quiet splash”
改成  
GRUB_CMDLINE_LINUX_DEFAULT=”quiet splash text”
然后再运行  
sudo update-grub 

即可。

如果想进入图形界面，输入命令：   
sudo lightdm 

# 29. after install ubuntu on Mac and then delete ubuntu, efi boot is redundant.

在Mac安装ubuntu后开机默认进入Grub引导，删除ubuntu后Grub引导依旧存在，  
导致每次开机都要按住option才能进入Mac系统

打开终端，挂载EFI分区
```
mkdir /mnt
sudo mount -t msdos /dev/disk0s1 /mnt
```
查看当前EFI分区

```
ls /mnt/
ls /mnt/EFI/
cd /mnt/EFI/
ls
```

```
rd@mba: ls /mnt/
BOOTLOG     EFI     FSCK0000.REC
rd@mba: ls /mnt/EFI/
APPLE
rd@mba: cd /mnt/EFI/
rd@mab: ls
APPLE BOOT ubuntu
```
run
```
rm -rf ubuntu
sudo reboot
```
# 30. setup manpage

```
sudo apt-get update
sudo apt-get install manpages-posix

```
安装 C语言 库函数基本帮助文档:  
```
sudo apt-get install libc-dev
sudo apt-get install glibc-doc
sudo apt-get install manpages
sudo apt-get install manpages-zh
sudo apt-get install manpages-zh-dev
sudo apt-get install manpages-dev
```
安装 POSIX 函数帮助文档:  
```
sudo apt-get install manpages-posix
sudo apt-get install manpages-posix-dev
```
安装内核函数文档：
```
sudo apt-get install linux-doc
sudo apt-get install libcorelinux-dev
```
安装 C++ 帮助文档:
```
sudo apt-get install libstdc++-7-dev
sudo apt-get install libstdc++-7-doc
```
对于manpage可以直接一条命令：
```
sudo apt-get install manpages*
```

# 31. use smb to connect with windows doc sharing

ios version > 13  
file -> browser -> ...(right upper conner icon) -> connect server  
server: smb://192.168.1.123  
config user as guest or registed user.  
have fun!

# 32. 设置linux免密码登录 login linux without password      

client:192.168.0.1  
server:192.168.0.2  
on client   
```
ssh-keygen  
cd ~/.ssh/
scp id_pub.rsa user@192.168.0.2:/home/user/
```

on server
```
cat /home/user/id_pub.rsa >> ~/.ssh/authroized_keys
```
hava fun!

# 32. config ubuntu wifi driver and chinese input method after installed
wifi
```
sudo apt-get --reinstall install bcmwl-kernel-source
```
zh languge pack
```
sudo apt-get install  language-pack-zh-han*
sudo apt install $(check-language-support)
sudo apt install ibus-pinyin
sudo apt install ibus-libpinyin
```
grub time out

```
sudo vim /etc/default/grub
sudo update-grub
```
close bluetooth when sys boot
```
sudo gedit /etc/rc.local
rfkill block bluetooth
```
# 33. Fn key in ubuntu
make F1 work as F1, Fn+F1 work as something else.  

```
sudo vim /etc/modprobe.d/hid_apple.conf
options hid_apple fnmode=2
sudo update-initramfs -u
```
# 34. 查看动态库so文件所在的目录

```
ldconfig -p 
```  
# 35 ubuntu 通过网线共享网络

[url](https://blog.csdn.net/qq1187239259/article/details/80022272) 

host A：ubuntu16.04, 有两个网卡，一个接外网，一个与主机B相接  
hostB：ubuntu16.04  

## 35.1 config host A 
run  
```
iwconfig
```  
wlp2s0 :这个是无线网卡。
enp1s0 :有线网卡，与B主机通过网线相连的网卡  

config ip  
sudo vim /etc/network/interfaces  
为接口enp1s0配置静态IP地址， 
```
iface enp1s0 inet static
address 192.168.49.1
netmask 255.255.255.0
gateway 192.168.49.1
```
restart interface enp1s0
```
ifdonw enp1s0
ifup enp1s0
ifconfig  命令查看enp1s0 ip配置是否成功
```
## 35.2 config host B
run  
```
iwconfig
```
获取网络接口卡名称 enpxxxx    
sudo vim /etc/network/interfaces 
```  
iface enpxxxx inet static
address 192.168.49.2
netmask 255.255.255.0
gateway 192.168.49.1
dns-nameservers 186.76.76.76
```
restart interface enpxxxxx
```
ifdonw enp1s0
ifup enp1s0
ifconfig  命令查看enpxxxx ip配置是否成功
```
ping host A OK
```
ping 192.168.49.1
```

## 35.3 config NAT on host A

这一步是为了B主机能通过A主机访问外网  
```
sudo  echo 1 > /proc/sys/net/ipv4/ip_forward 
iptables -F
iptables -P INPUT ACCEPT
iptables -P FORWARD ACCEPT
 iptables -t nat -A POSTROUTING -o wlp2s0 -j MASQUERADE     （wlp2s0为host A接外网的网卡）
```
