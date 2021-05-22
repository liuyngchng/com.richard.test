# 1. setup Dameng DB and Java
## 1.1 setup DmDB
verion > 7.6.1, check db version

```
/home/rd/dmdbms/bin/DmServiceDMSERVER start
select * from v$version;
```
## 1.2 create user and grant role

```
cd /home/rd/dmdbms/tool
./manager
```
使用GUI界面，进入 Users-> Administrate User,  
点击右键，'New User', 在General 选项卡输入用户名(XX)和密码()，  
在Owner选项卡中勾选"DBA", "PUBLIC", "RESOURCE","VTI"三个角色，点击确定

## 1.3 import data for service xxfw  

```
cd /home/rd/dmdbms/tool
./manager
```
使用GUI界面，选择Schema->DBXXFW,点击右键，选择”Import“，选择  
DBXXFW.dmp 文件所在的目录， 选择 DBXXFW.dmp 文件， dmp 文件所在路径为
`/home/rd/work/dky/xxfw/`目录 
点击开始  

## 1.4 Java
`java -version` version = 1.8

# 2. config service

## 2.1 config kafka and zk

config IP and log.dirs
```
cd /home/rd/work/dky/subject/kafka_2.11-2.0.0/config
vi server.properties
```
change value of log.dirs to a writable dir, or else service would be shutdown.

## 2.2. config xxfw

config IP  
```
cd /home/rd/work/dky/xxfw/apache-tomcat-xxfw/config/
vi cetc-zookeeper.properties                        // 修改zk的IP
vi config.properties                                // 修改zk， solr的IP
vi db.properties                                    // 修改达梦数据库的IP，用户名和密码
```
## 2.3 config subject
config IP  
```
cd /home/rd/work/dky/subject/apache-tomcat-subject/webapps/data/WEB-INF/classes
vi cetc-kafkaConfig.properties
vi cetc-jdbc.properties
vim cetc-zookeeper.properties
cd /home/rd/work/dky/subject/apache-tomcat-subject/webapps/data/WEB-INF/classes/default_conf
vi cetc-access-core.properties
```
# 3. start up service

## 3.1 start up kafka and zk

```
cd /home/rd/work/dky/subject/kafka_2.11-2.0.0/bin
./startZK-Kafka.sh
```
test zookeeper  
```
sudo netstat -anpl | grep 2181
```
test kafka
```
sudo netstat -anpl | grep 9092
```
如果9092没有监听，则再执行一遍 `./startZK-Kafka.sh`  

## 3.2 start up solr

```
cd /home/rd/work/dky/xxfw/apache-tomcat-solr/bin
./startup.sh
```
test
```
curl -i http://127.0.0.1:18011/solr/#/
HTTP/1.1 200 OK
```
## 3.3 start up xxfw

```
cd /home/rd/work/dky/xxfw/apache-tomcat-xxfw/bin
./startup.sh
```
test
```
sudo netstat -anpl | grep 18088
```
login

[入口地址]( http://127.0.0.1:18088/ds), admin, xxgxzc  

## 3.4  start up
```
cd /home/rd/work/dky/subject/apache-tomcat-subject/bin
./startup.sh
```
test
```
curl http://127.0.0.1:12808/data/rest/rdb/User
```

#4. create RESTful API can be accessed by 'access.client'

## 4.1 data API
### 4.1.1 data in format JSON
```
curl -H 'Accept:application/json' -i http://127.0.0.1:12808/data/rest/rdb/User?_pi=1&_ps=5
HTTP/1.1 200 OK
Date: Wed, 31 Mar 2021 02:19:44 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 171

{
  "dataset":
    {"data":
      [
        {"a":"a", "b": "b"}
      ],
      "totalCount": "1"
    },
  "currentPage": "1",
  "pageSize": "5",
  "subjectId": "12345"
}
```
note
```
_pi: pageIndex
_ps: pageSize
```
data element can be a JSONObject or a JSONArray,   
data element can be deserialized to a Java class returned from the API  
```
A affiliated API must be created as illustrated by chapter `class name API`
```
### 4.1.2 data in format XML
```
curl -H'Accept:application/xml' -i http://127.0.0.1:12808/data/rest/rdb/User
HTTP/1.1 200 OK
```
### 4.1.3 data in format PB 
```
curl -H'Accept:application/x-protobuf' -i http://127.0.0.1:12808/data/rest/rdb/User
HTTP/1.1 200 OK

```
## 4.2 class name API 
数据接口能被 `access.client`正常访问，必须提供类名辅助接口，  

```
curl -i http://127.0.0.1:12808/data/rest/rdb/User/_CLASS
HTTP/1.1 200 OK
Date: Wed, 31 Mar 2021 02:21:10 GMT
Content-Type: text/plain;charset=UTF-8
Content-Length: 43

packageName.className
```
同时在`access.client`项目中必须有该接口返回的类名对应的java类.
## 4.3 `access.client` client project

class.forName("packageName.className") can be found.   
class properties name characters must be in lowercase.  

## 5. DBsubject Demo
## 5.1 create table

```
CREATE TABLE "DBXXFW"."XXZT_RZ_CZRZ"
(
"CZSJ" VARCHAR(50) NOT NULL,
"YWLXNM" VARCHAR(50),
"YWLXMC" VARCHAR(50),
"CZLXNM" VARCHAR(50),
"CZLXMC" VARCHAR(50),
"CZTSXX" VARCHAR(50),
"XWBS" VARCHAR(50),
"YHBS" VARCHAR(50),
"YHIP" VARCHAR(50),
"YWNR" VARCHAR(50),
"ZHSBS" VARCHAR(50),
CLUSTER PRIMARY KEY("CZSJ")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "DBXXFW"."XXZT_RZ_CZRZ" IS 'a test for DBSubject';
```
insert data
```

insert into "DBXXFW"."XXZT_RZ_CZRZ"("CZSJ", "YWLXNM", "YWLXMC", "CZLXNM", "CZLXMC", "CZTSXX", "XWBS", "YHBS", "YHIP", "YWNR", "ZHSBS") 
VALUES('a4', 'b4', 'c4', 'd', 'e','f', 'h', 'i', 'j', 'k', 'l');

```
# 6. publish a REST API 

Java project named `publish.subject`

## 6.1 create DAO class 

create a java class like `DBSubject.java` in package com.cetc28.access.business.domain.db  
note: property name character must be in lowercase.  

## 6.2 create mybatis xml config file

create a XML config file in /src/main/resources/mapping/db, named abcde.xml

## 6.3 publish content to xxfw

create a xml file in src/main/resources/subjectConfig/, named abcde.xml to publish API

## 6.4 config 
### 6.4.1 config IP

config server ip in file src/main/resources/default_conf/cetc-access-core.properties  
and src/main/resources/default_conf/cetc-cluster.properties

### 6.4.2 config DB jdbc

config database jdbc in file src/main/resources/cetc-jdbc.properties  

### 6.4.3 config kafka server

config kafka server IP and port in file src/main/resources/cetc-kafkaConfig.properties  

### 6.4.4 config memcache server

config memcached server IP and port in file src/main/resources/cetc-memcached.properties  

### 6.4.5 config zk address  

config zookeeper server IP and port in file src/main/resources/cetc-zookeeper.properties  

## 6.5 build project and package a war  
build project and package as a war file, copy it to directory `apache-tomcat-subject/webapp`  
and run xx-tomat/bin/startup.sh  
you can see text like 
```
2021-xx-xx 15:45:38.393 INFO  [***Executor-9-thread-1] (*.java:288) - Succeed publish subject[User],resId is[149617]
```
in log file, that means all of the things are OK, have fun!
