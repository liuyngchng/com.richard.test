# 1. docker
## 1.1 Download Docker Desktop
[download page](https://www.docker.com/get-started) 
for windows, just run `Docker Destop Installer.exe`  
for ubuntu, install online, just run  
```
sudo apt install docker.io
```
for ubuntu, install from package, follow [instruction](https://docs.docker.com/engine/install/ubuntu/)
and then run   
`sudo dpkg -i /path/to/package.deb`  
## 1.2 pull docker image

当执行`docker ps`提示无权限时，执行以下语句
```
sudo gpasswd -a $USER docker
newgrp docker
```

拉取ubuntu镜像，将软件包dky目录以及达梦目录deploy_tool映射至容器
```
docker pull ubuntu
docker run -dit -v /home/rd/work/dky:/home/rd/work/dky -v /home/rd/work/deploy_tool:/opt/deploy --network host --name test ubuntu
docker exec -it test bash
```

# 2. setup Dameng DB and Java in docker container
## 2.1 setup DmDB

### 2.1.1 安装service
若已经将宿主机达梦的目录映射至容器，则启动容器，进入达梦目录
```
docker exec -it test bash
cd /opt
```
否则， 拷贝达梦安装包到docker容器
```
docker cp dm.tar.gz test:/opt      # 将容器外的文件dm.tar.gz拷贝到容器test的/opt目录下
cd /opt/
```
启动 容器
```
docker exec -it test bash
tar -zxf dm.tar.gz 
```
安装达梦  
```
cd /opt/dameng_x86
./DMInstall.bin -i
```
采用定制化安装，如下所示
```
Installation Type:
1 Typical
2 Server
3 Client
4 Custom
Please Input the number of the Installation Type [1 Typical]:4
1 Server component
2 Client component
  2.1 Manager
  2.2 Monitor
  2.3 DTS
  2.4 Console
  2.5 Analyzer
  2.6 DISQL
3 DM Drivers
4 Manual component
5 DBMS Service
  5.1 Realtime Audit Service
  5.2 Job Service
  5.3 Instance Monitor Service
  5.4 Assistant Plug-In Service
Please Input the number of the Installation Type [1 2 3 4 5]:1 2 3
```
按照提示操作即可

### 2.1.2 初始化服务实例
假定输入的实例名称为test  
```
cd /opt/dmdbms/bin
./dminit
input system dir: /opt/dmdbms/data          // 数据文件存放路径
input db name: test                        //实例/数据库名
input port num: 5236                        //服务端口
input page size(4,8,16,32): 8               //数据库页大小
input extent size(16,32): 32                //扩展大小
input sec priv mode(0,1): 0                 //安全特权模式
input time zone(-12:59,+14:00): +8          //时区 ，选东8区
input case sensitive? ([Y]es,[N]o): N       //标识符是否区分大小写
which charset to use? (0[GB18030],1[UTF-8],2[EUC-KR]): 1    //字符集
length in char? ([Y]es,[N]o): N             //字符长度, N for DBXXFW.dmp
enable database encrypt? ([Y]es,[N]o): N    //是否启用数据库加密
input slice size(512,4096): 512
page check mode? (0/1/2): 0                 //是否开启页页检查模式
input elog path:                            //日志路径
auto_overwrite mode? (0/1/2): 0             //自动覆盖模式
```
### 2.1.3 启动服务实例端口监听  

注册服务并启动端口监听,  

```
cd /opt/dmdbms/script/root
./dm_service_installer.sh -t dmserver -i /opt/dmdbms/data/test/dm.ini -p TEST
cd /opt/dmdbms/data
chmod -R 777 test
cd /opt/dmdbms/bin
./DmServiceTEST start   #启动
./DmServiceTEST stop    #停止
./DmServiceTEST status  #查看服务状态
```

也可以不注册服务 启动端口监听，数据库名称为test，则启动时执行

```
cd /opt/dmdbms/bin
nohup ./dmserver ../data/test/dm.ini > dm.log 2>&1 &
```

若出现错误，想删除名称为'test'的数据库实例，执行 

```
cd dmdbms/script/root
./dm_service_uninstaller.sh -n test
cd /dmdbms/data
rm -fr test
ps -ef | grep dmserver
kill -9 xxxx
```
## 2.2 检查服务版本 
```
cd /opt/dmdbms/bin
./disql SYSDBA/SYSDBA@localhost
SQL> select * from v$version;
SQL>quit;
```
verion > 7.6.1

## 2.3 create user and grant role
```
cd /opt/dmdbms/bin
./disql SYSDBA/SYSDBA@localhost
SQL> create user DBXXFW identified by DBXXFW123456;
SQL> grant DBA to DBXXFW;
SQL> quit;
```
## 2.4 import data for service xxfw
```
cd /opt/dmdbms/bin
./dimp SYSDBA/SYSDBA IGNORE=N ROWS=Y FULL=Y file="/opt/dky/xxfw/DBXXFW.dmp"
```
整个导入过程不能报错，导入日志详见DBXXFW.dmp同级目录下文件

## 2.5 install Java

```
apt update
apt install openjdk-8-jdk
```
注意，需要安装JDK，而不是JRE，否则，xxfw会报错
```
Caused by: java.lang.RuntimeException: compiler is null maybe you are on JRE enviroment please change to JDK enviroment.
    at com.baidu.bjf.remoting.protobuf.utils.compiler.JdkCompiler.<init>(JdkCompiler.java:94)
```
验证java 版本，
`java -version` version = 1.8

## 2.5 生成docker镜像文件
导出docker 镜像
```
docker exec -it test bash
exit
docker stop test;
docker ps -a
docker commit container_id ubuntu_dm
docker save ubuntu_dm -o ./ubuntu_dm.tar
```
导入docker 镜像文件

```
docker load -i ./ubuntu_dm.tar
```

# 3. config service in or out docker 

## 3.1 config kafka and zk

config IP and log.dirs, IP采用宿主机的IP（前提：docker 采用host 网络模式），
目录也采用宿主机的目录（前提：docker 目录映射采用相同目录）
```
cd /home/rd/work/dky/subject/kafka_2.11-2.0.0/config
vi server.properties
```
change value of log.dirs to a writable dir, or else service would be shutdown.

## 3.2 config xxfw

config IP  
```
cd /home/rd/work/dky/xxfw/apache-tomcat-xxfw/config/
vi cetc-zookeeper.properties                        // 修改zk的IP
vi config.properties                                // 修改zk， solr的IP
vi db.properties                                    // 修改达梦数据库的IP，用户名和密码
```
## 3.3 config subject
config IP  
```
cd /home/rd/work/dky/subject/apache-tomcat-subject/webapps/data/WEB-INF/classes
vi cetc-kafkaConfig.properties
vi cetc-jdbc.properties
vim cetc-zookeeper.properties
cd /home/rd/work/dky/subject/apache-tomcat-subject/webapps/data/WEB-INF/classes/default_conf
vi cetc-access-core.properties
```
# 4. start up service 
启动docker容器，进行文件目录映射，指定网络模式为host(事实上也可以采用桥接等其他模式)， 
采用host网络模式的好处是，在容器内的网络和宿主机完全一样，IP一样，不引入额外的网络和IP问题  
这样docker内的IP完全采用宿主机的IP  
```
docker images
docker run -dit -v /home/rd/work/dky:/home/rd/work/dky --network host --name test image_id
docker exec -it test bash

接下来，在docker 容器中启动服务  

```
## 4.1 start up dmdb
```
cd /opt/dmdbms/bin
./DmServiceTEST start
```

## 4.2 start up kafka and zk

```
cd /home/rd/work/dky/subject/kafka_2.11-2.0.0/bin
./startZK-Kafka.sh
```
test zookeeper， 若docker 容器中没有安装相应的命令，则可以在宿主机上执行  
对于windows宿主机推荐安装轻量级Linux工具集[cmder](https://cmder.net/),mini版就够用 
```
sudo netstat -anpl | grep 2181
```
test kafka
```
sudo netstat -anpl | grep 9092
```
如果9092没有监听，则再执行一遍 `./startZK-Kafka.sh`  

## 4.3 start up solr

```
cd /home/rd/work/dky/xxfw/apache-tomcat-solr/bin
./startup.sh &
```
test
```
curl -i http://127.0.0.1:18011/solr/#/
HTTP/1.1 200 OK
```
## 4.4 start up xxfw

```
cd /home/rd/work/dky/xxfw/apache-tomcat-xxfw/bin
./startup.sh &
```
test
```
sudo netstat -anpl | grep 18088
curl http://127.0.0.1:18088/ds
```
login

[入口地址](http://127.0.0.1:18088/ds), admin, xxgxzc  

## 4.5  start up subject
```
cd /home/rd/work/dky/subject/apache-tomcat-subject/bin
./startup.sh &
```
test
```
curl http://127.0.0.1:12808/data/rest/rdb/User
```

