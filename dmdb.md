# 1. Dameng DB
## 1.1 setup dmdb in GUI mode

```
cd dameng_x86
./DMInstall.bin
cd /home/rd/Desktop/DMDBMS
```
add path for cmd

```
sudo vim /etc/profile
PATH=/home/rd/dmdbms/bin:$PATH
export PATH
source /etc/profile
```
start DmServiceDMSERVER, to run

```
DmServiceDMSERVER start
```
test you dm server
```
disql SYSDBA/SYSDBA@localhost:5236
create table t(a int,b int,c int);
insert into t values(1,2,3);
insert into t values(11,22,33);
commit;
quit
```

## 1.2 setup dmdb in terminal (no GUI)

```
cd dameng_x86
./DMInstall.bin -i
cd /opt/dmdbms/bin 
./dminit
input system dir: /opt/dmdbms/data		// 数据文件存放路径
input db name: dm5252		   //实例/数据库名
input port num: 5252			//服务端口
input page size(4,8,16,32): 8	//数据库页大小
input extent size(16,32): 32	//扩展大小
input sec priv mode(0,1): 0		//安全特权模式
input time zone(-12:59,+14:00): +8	//时区 ，选东8区
input case sensitive? ([Y]es,[N]o): Y	//标识符是否区分大小写
which charset to use? (0[GB18030],1[UTF-8],2[EUC-KR]): 1	//字符集
length in char? ([Y]es,[N]o): Y		//字符长度
enable database encrypt? ([Y]es,[N]o): N	//是否启用数据库加密
input slice size(512,4096): 512
page check mode? (0/1/2): 0		//是否开启页页检查模式
input elog path: 		//日志路径
auto_overwrite mode? (0/1/2): 0		//自动覆盖模式
```
注册服务
```
cd /opt/dmdbms/script/root
dm_service_installer.sh -t dmserver -i /opt/dmdbms/test/dm.ini -p TEST
cd /opt/dmdbms
chmod -R 777 test
cd /opt/dmdbms/bin
./DmServiceTEST start	#启动
./DmServiceTEST stop	#停止
./DmServiceTEST status	#查看服务状态

```


若初始化的数据库名称为test，则启动时执行
```
./dmserver ../data/test/dm.ini &
```

docker

```
docker network create --subnet=172.18.0.0/16 mynetwork
docker network ls
docker run -dit -p 5236:5236 --name test --net mynetwork --ip 172.18.0.2  209c24a7f1c1
```
create user
```

```
import data
```
/dimp SYSDBA/SYSDBA IGNORE=Y ROWS=Y FULL=Y
```
## 1.3 setup unixODBC

```
tar -zxf unixODBC-2.3.9.tar.gz
cd unixODBC-2.3.9/
./configure
make
sudo make install
```
add so to lib path
```
sudo vim /etc/profile
LD_LIBRARY_PATH=/usr/local/lib:$LD_LIBRARY_PATH
export LD_LIBRARY_PATH
sudo ldconfig
```
then, run
```
odbcinst -j
```
you can see
```
unixODBC 2.3.9
DRIVERS............: /usr/local/etc/odbcinst.ini
SYSTEM DATA SOURCES: /usr/local/etc/odbc.ini
FILE DATA SOURCES..: /usr/local/etc/ODBCDataSources
USER DATA SOURCES..: /home/rd/.odbc.ini
SQLULEN Size.......: 8
SQLLEN Size........: 8
SQLSETPOSIROW Size.: 8
```
## 1.4 config odbc

sudo vim /user/local/etc/odbcinst.ini,
content as following
```
[DM7 ODBC DRIVER]
Description = ODBC DRIVER FOR DM7
Driver		= /home/rd/dmdbms/bin/libdodbc.so
Setup		= /lib/libdmOdbcSetup.so
```
sudo vim /usr/local/etc/odbc.ini,
content as following
```
[dm]
Description = DM ODBC DSN
Driver		= DM7 ODBC DRIVER
SERVER		= localhost
UID			= SYSDBA
PWD			= SYSDBA
TCP_PORT 	= 5236
```
## 1.5 config LD_LIBRARY_PATH
add dm libdodbc.so to LD_LIBRARY_PATH
sudo vim /etc/profile
```
LD_LIBRARY_PATH=/home/rd/dmdbms/bin:$LD_LIBRARY_PATH
export LD_LIBRARY_PATH
```
and run
```
sudo ldconfig
```
now test your odbc config, run
 ```
 isql -v dm
 ```
you can see
```
+---------------------------------------+
| Connected!                            |
|                                       |
| sql-statement                         |
| help [tablename]                      |
| quit                                  |
|                                       |
+---------------------------------------+
```
if you see
`Can't open lib '/home/rd/dmdbms/bin/libdodbc.so' : file not found`,
but the so file really exit, that is because of LD_LIBRARY_PATH config
for dmdb lib and odbc lib is not valid or haven't be activated.
check and fix the problem,you can run
```
ldd theSoCalledMissedSoFile.so
```
to check what's wrong.

# 2. DM DB case sensitive  
# 2.1 对于表名、字段名而言  
在设置为大小写敏感的库中进行查询的时候，可能经常会遇到无效的表名或列名的问题，  
下面针对这种情况进行说明。  
# 2.2 如果使用DM管理工具图形界面建表的话建议表名和字段名都使用大写
因为使用图形界面建表相当于使用语句建表的时候加了双引号的，会固定住大小写。  
如果写成小写，那么就是小写了，在查询的时候也需要加双引号，否则就有可能报无效的表名或列名的问题，比较麻烦；
# 2.3 如果使用脚本建表的话，如果表名和字段名没有加双引号的话都会被系统自动转换成大写  
如：create table test(a int)；系统会自动把表名test，字段名a转换成大写，处理方式与Oracle数据库一致  
所以针对大小写敏感的库，在使用建表脚本或者通过查询脚本进行查询的时候建议统一不要写双引号，  
让系统统一自动都转化为大写；
## 2.4 以上两点主要针对大小写敏感的库而言，大小写不敏感的库则不存在上述问题。  
基于以上两点，在初始化数据库的过程中就可以对字符串比较大小写敏感这个参数做出合理的选择了。
## 2.5 SQL

```
select * from v$version;

-- 查看所有表空间（schema）---
select name from v$tablespace;

-- 查看所有表空间物理文件 --
select * from v$datafile;

-- 执行sql脚本 --
SQL> start /a/b/c.sql

-- 编辑SQL脚本 --
SQL> edit /a/b/c.sql


-- 查看表空间物理文件的名称及大小 --
select t.name tablespace_name,t.id file_id,d.path file_name,d.total_size*sf_get_page_size()/1024/1024||'m' total_space from v$tablespace t, v$datafile d where t.id=d.group_id;

-- 查看数据库实例信息 --
select name inst_name from v$instance;

-- 确定高负载的 SQL(慢查询) --
select * from v$long_exec_sqls;


select owner, table_name, tablespace_name from dba_tables where owner = 'sysdba';

-- 查询当前用户下所有的表名--
select NAME from sysobjects where "SUBTYPE='SCH');

-- 有开源的客户端工具可以连DM ?--
-- 基于JDBC的工具就可以，比如：SQuirrel SQL、DbVisualizer --
--  create user --
create user USNAME IDENTIFIED BY PSWORD;
```

