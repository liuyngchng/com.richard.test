
# 1. setup
```
chmod +x atlassian-confluence-6.3.1-x64.bin
sudo ./atlassian-confluence-6.3.1-x64.bin
```
# 2. browse web page
```
http://localhost:8090/
```
get server Id from `http://localhost:8090/setup/setuplicense.action` as BUDX-QIN5-ECQ1-MSZY  
# 2. crack
```
cd /opt
sudo mv /opt/atlassian/confluence/confluence/WEB-INF/lib/atlassian-extras-decoder-v2-3.2.jar /opt/atlassian-extras-2.4.jar
cd /crack_file_dir
sudo ./key_gen.sh
```
click 'patch', choose `/opt/atlassian-extras-2.4.jar` in GUI, and then click 'gen'  
you can see two file in /opt as   
```
atlassian-extras-2.4.bak  atlassian-extras-2.4.jar
```
run  
```
cd /opt
sudo mv atlassian-extras-2.4.jar atlassian-extras-decoder-v2-3.2.jar  
sudo mv atlassian-extras-decoder-v2-3.2.jar /opt/atlassian/confluence/confluence/WEB-INF/lib
```

# 3. add DB driver
```
sudo cp mysql-connector-java-8.0.12.jar /opt/atlassian/confluence/confluence/WEB-INF/lib/
```

# 4. restart service
```
sudo sh /opt/atlassian/confluence/bin/stop-confluence.sh
sudo sh /opt/atlassian/confluence/bin/start-confluence.sh

```
# 5. add license
copy key text to web page textbox named `confluence` in `http://localhost:8090`  
click 'Next', choose a External DB(for production) or Embedded DB(for demonstartion)  

# 6. External MySQL DB 5.7
## 6.1 mysql 5.7 jdbc
```
jdbc:mysql://localhost/confluence?useUnicode=true&characterEncoding=utf8&useSSL=false
```
## 6.2 mysql 5.7 config
```
create database confluence character set UTF8 collate utf8_bin;
grant all on confluence.* to your_user@"%" identified by 'your_password';
flush privileges;

```
## 6.3 mysql 8 jdbc
```
url=jdbc:mysql://localhost/confluence?useSSL=false&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT
username=root
password=0000000
driver-class-name=com.mysql.cj.jdbc.Driver

```
## 6.4 mysql 8 config
```
create database confluence character set UTF8 collate utf8_bin;
use mysql;
select user, host from user;
update user set host = '%' where user = 'root';
commit;
grant all privileges on *.* to 'root'@'%' ;
SET GLOBAL transaction_isolation='READ-COMMITTED';
```

config
```
show variables like '%max_allowed_packet%';
set global max_allowed_packet = 34*1024*1024;
```
关闭连接，重新连接查看变量'max_allowed_packet' 的值  

```
alter database confluence character set utf8 collate utf8_bin;
```

# 7. Remove service
```
sudo /opt/atlassian/confluence/uninstall
sudo rm -fr /opt/atlassian
sudo rm -fr /var/atlassian/
```
# 8. Reconfigre confluence

```
cd /var/atlassian/application-data/confluence
vi confluence.cfg.xml
```
