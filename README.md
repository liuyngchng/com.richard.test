
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
alter user 'root'@'localhost' identified
i```

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
``
docker info | grep dir -i
```
修改docker的systemd的 docker.service的配置文件
不知道 配置文件在哪里可以使用systemd 命令显示一下.  

```
systemctl disable docker
systemctl enable docker
显示结果
Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
i```
show

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

## 2.8 docker network
### 2.8.1 为容器设置固定的 IP 地址

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

### 2.8.2 使用host 网络模式
使用`docker network ls` 中的 host模式，容器的网络配置与宿主机完全一样，这样也不需要在做容器内外的端口映射了。
```
docker run -dit --name container_name --network host image_id
```

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
## 2.11 docker容器内设置ubuntu语言为中文
### 2.11.1 查看当前语言
`locale`
### 2.11.2 查看当前已安装的语言
`locale -a`
```
### 2.11.3 安装语言包
```   
apt-get install language-pack-zh-hans
locale-gen zh_CN.UTF-8
```
再次查看  
```   
locale -a
```  
### 2.11.4 添加到文件
```
echo "export LC_ALL=zh_CN.UTF-8">> /etc/profile
source /etc/profile
```
如果这里添加失败，提示没有这种语言包，退出容器，再重新进入，就可以添加了
### 2.11. 5 完成
`locale`

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
#7.1 under MacOS
如果是在Mac系统下，则 需要把下载的Ubuntu安装文件（.iso）  
转换成(.dmg)格式的文件,方便在Mac OS上面进行操作，转换命令
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
#7.2  Create the installation medium in linux
Either you can burn the image onto CD/DVD, you use usb stick for the installation.  
Under linux, you can use the dd for that:
```
dd if=<source iso> of=<target device> bs=4M; sync
```
Make sure that the device does not include partition number, so example from my machine:
```
dd if=~/Downloads/alpine-standard-3.10.2-x86_64.iso of=/dev/sdb bs=4M
```
The target device will be erased, so make sure you use something without any data you do not want to lose.
#7.3 然后移除U盘
on MacOS
```
diskutil eject /path/to/USB
```
on Linux
```
umount /path/to/USB
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

## 15.4 Create the installation medium
Either you can burn the image onto CD/DVD, you use usb stick for the installation.  
Under linux, you can use the dd for that:
```
dd if=<source iso> of=<target device> bs=4M; sync
```
Make sure that the device does not include partition number, so example from my machine:
```
dd if=~/Downloads/alpine-standard-3.10.2-x86_64.iso of=/dev/sdb bs=4M
```
The target device will be erased, so make sure you use something without any data you do not want to lose
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

run `g/^\s*$/d`

# 20. compile quant project

to debug the openssl version, download openssl new version,  
add parameter in cmake

`cmake ../ -DOPENSSL_ROOT_DIR=/usr/local/ssl -DOPENSSL_LIBRARIES=/usr/local/ssl/lib`

# 21. delete cache file after 'sudo apt-get install for ubuntu'  
run `sudo apt-get clean`
and then all file in /var/cache/apt/archives be deleted.

# 22. ubuntu support exfat disk format

run `sudo apt-get install exfat-utils`

# 23. kylin 4.0.2 source list

修改 apt 的源文件管理文件 :  `/etc/apt/sources.list`
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

run `lspci | grep Wireless`  
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

# 32. config ubuntu wifi driver, chinese and grub timeout input method after installed
# 32.1 wifi
```
sudo apt-get --reinstall install bcmwl-kernel-source
```
# 32.2 zh languge pack
```
sudo apt-get install  language-pack-zh-han*
sudo apt install $(check-language-support)
sudo apt install ibus-pinyin
sudo apt install ibus-libpinyin
```
#32.3 grub time out

```
sudo vim /etc/default/grub
```
change the value of GRUB_TIMEOUT=2
```
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

run `ldconfig -p`

# 35 ubuntu  share network between hosts (通过网线共享网络)

[url](https://blog.csdn.net/qq1187239259/article/details/80022272) 

host A：ubuntu16.04, 有两个网卡，一个接外网，一个与主机B相接  
hostB：ubuntu16.04  

## 35.1 config host A 
run `iwconfig`  
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

run  `iwconfig` 
获取网络接口卡名称 enpxxxx    
sudo vim /etc/network/interfaces 
```  
iface enpxxxx inet static
address 192.168.49.2
netmask 255.255.255.0
gateway 192.168.49.1
dns-nameservers 180.76.76.76
```
restart interface enpxxxxx
```
ifdonw enp1s0
ifup enp1s0
ifconfig  命令查看enpxxxx ip配置是否成功
```
ping host A OK `ping 192.168.49.1`

## 35.3 config NAT on host A

这一步是为了B主机能通过A主机访问外网  

```
sudo  echo 1 > /proc/sys/net/ipv4/ip_forward 
iptables -F
iptables -P INPUT ACCEPT
iptables -P FORWARD ACCEPT
iptables -t nat -A POSTROUTING -o wlp2s0 -j MASQUERADE     （wlp2s0为host A接外网的网卡）
```
## 35.4 debug

配置完以上信息后，若发现 host A 无法上网，则是默认路由导致的，
执行
```
ip route show
sudo route del default gw 192.168.49.1
```

# 36. git clone with shallow history

```
git clone xxxx.git --depth 1
```

# 37. fix the `/dev/loop* 100%` problem

run `sudo apt autoremove --purge snapd `

# 38. ubuntu 16.04 remote access desktop of windows 7/10
setup rdesktop first,
```
sudo apt-get install rdesktop libgssglue1
```
then run

```
rdesktop -g800*600 -a 16  192.168.1.112  // in a 800*600 windows
rdesktop -f -a 16  192.168.1.112		// full screen
```
run `crtl+alt+enter` to exit remote desktop
如果看到报错`ERROR:CREDSSP..... CredSSP required by Server`,则需要在windows上
开启远程桌面(我的电脑->属性->远程设置->允许远程连接到此计算机)时，  
取消勾选`仅允许使用网络级别身份认证...`
# 37. connect wifi via terminal on Ubuntu

查看可用wifi，
```
nmcli dev wifi
```
配置wifi，

```
nmcli dev wifi connect essid（网络名称） password password（密码）
```
# 39. get random number
```
dd if=/dev/urandom bs=1 count=16 | xxd -ps
```

## 40. docker permission

## 40.1 问题描述  
在终端执行"docker version"命令，出现如下报错：

```
”Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get http://%2Fvar%2Frun%2Fdocker.sock/v1.26/images/json: dial unix /var/run/docker.sock: connect: permission denied“
```
## 40.2 原因分析  

来自docker mannual：
```
Manage Docker as a non-root user

The docker daemon binds to a Unix socket instead of a TCP port. By default that Unix socket is owned by the user root and other users can only access it using sudo. The docker daemon always runs as the root user.

If you don’t want to use sudo when you use the docker command, create a Unix group called docker and add users to it. When the docker daemon starts, it makes the ownership of the Unix socket read/writable by the docker group.
```

docker进程使用 Unix Socket 而不是 TCP 端口。而默认情况下，Unix socket 属于 root 用户，因此需要 root权限 才能访问。

## 40.3 解决方法  

```
sudo groupadd docker          #添加docker用户组
sudo gpasswd -a $XXX docker   #检测当前用户是否已经在docker用户组中，其中XXX为用户名，例如我的，rd
sudo gpasswd -a $USER docker  #将当前用户添加至docker用户组
newgrp docker                 #更新docker用户组
sudo chmod a+rw /var/run/docker.sock
```

# 41. dns lookup
```
dig @114.114.114.114 registry-1.docker.io
```

# 42. network interface card up down

```
ifdown eth1  /  ifconfig eth1 down 　　　　禁用网卡

ifup eth1  / ifconfig eth1 up 　　　　　　 启用网卡
```
# 43. mvn install
```
mvn install:install-file -DgroupId=com.dm -DartifactId=dmjdbc7 -Dversion=1.7.0 -Dpackaging=jar -Dfile=Dm7JdbcDriver17.jar
```

# 44. docker group 
when you run `docker ps` in Ubuntu and it says as following
```
Got permission denied while trying to connect to the Docker daemon socket at
```
则只需要将当前用户加入到docker组中即可， 执行
```
sudo gpasswd -a $USER docker
newgrp docker
```
# 45. set root password
```
sudo passwd
```
# 46. start sshd service
```
sudo /etc/init.d/ssh start
```
# 47. kylin 开启root登录
```
cd /usr/share/lightm/ightm.conf.d
vi 50-unity-greeter.conf
add
greeter-show-manual-login=true   
allow-guest=false 

```
save and reboot

# 48. set IP use command
```
ifconfig 												//获取网卡名称，enp0
sudo ifconfig enp0 192.168.10.163 netmask 255.255.255.0	//set IP
sudo route add default gw 192.168.10.1					// set gateway
sudo /etc/init.d/networking stop
sudo /etc/init.d/networking start
```

## 49. use GUI in docker

在宿主机中运行

```
sudo apt-get install x11-xserver-utils
xhost +
```
拉取docker 镜像并运行
```
docker pull jess/libreoffice
docker run -d \
-v /etc/localtime:/etc/localtime:ro \
-v /tmp/.X11-unix:/tmp/.X11-unix \
-e DISPLAY=unix$DISPLAY \
-e GDK_SCALE \
-e GDK_DPI_SCALE \
--name libreoffice \
jess/libreoffice
```

## 50. ubuntu x11 forwarding

通过ssh X11 转发使用远程 GUI 程序 

client IP ：192.168.0.13  
server IP ：192.168.0.200

### 50.1 on server

```
sudo vim /etc/ssh/sshd_config 
修改或添加

X11Forwarding yes
X11DisplayOffset 10
X11UseLocalhost yes
```
restart sshd `sudo systemctl restart sshd.service`

### 50.2 on client

```
sudo vim /etc/ssh/ssh_config
修改或添加

ForwardAgent yes
ForwardX11 yes
ForwardX11Trusted yes
```
restart ssh `sudo systemctl restart ssh.service`


### 50.3 connect server with xhost
on client, run 
```
xhost +　　//允许服务器的的x11界面连接过来

ssh -X user@server_ip　　　　　　//-X参数表示转发X11数据， 把用户名称tsfh 以及服务器S的ip地址替换为你自己的
```

## 51. Ubuntu Linux下修改docker镜像源
## 51.1 国内亲测可用的几个镜像源
```
Docker 官方中国区：https://registry.docker-cn.com
网易：http://hub-mirror.c.163.com
中国科技大学：https://docker.mirrors.ustc.edu.cn
阿里云：https://y0qd3iq.mirror.aliyuncs.com
```
## 51.2 修改配置文件
 
增加Docker的镜像源配置文件 /etc/docker/daemon.json，  
如果没有配置过镜像该文件默认是不存的，在其中增加如下内容  
  
```
{
  "registry-mirrors": ["https://y0qd3iq.mirror.aliyuncs.com"]
}
```
## 51.3 restart service
```
service docker restart
```
查看配置是否生效  

```
docker info|grep Mirrors -A 1
```
## 52. deploy gitlab-ce in docker
```
docker pull gitlab/gitlab-ce:latest
mkdir -p /docker/gitlab/config
mkdir -p /docker/gitlab/logs
mkdir -p /docker/gitlab/data
docker run -d -p 8083:80 -p 8082:443 -p 8084:22 \
    -v /docker/gitlab/config:/etc/gitlab \
    -v /docker/gitlab/logs:/var/log/gitlab \
    -v /docker/gitlab/data:/var/opt/gitlab \ 
    --name=gitlab --privileged=true \
    gitlab/gitlab-ce:latest
```
修改默认git host地址
```
docker exec -it gitlab test
vi /opt/gitlab/embedded/service/gitlab-rails/config/gitlab.yml
```
修改 host 为部署服务器的IP，例如：host: 192.168.12.1
浏览`http://192.168.0.1:8083`， 修改密码为 psword，  
然后使用 username=root, psword=psword 进行登录

## 53. ubuntu firewall

```
sudo ufw status
sudo ufw disable
```
