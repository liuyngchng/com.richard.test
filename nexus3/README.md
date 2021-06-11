
# 1. Install Sonatype Nexus 3.3
## 1.1 docker
```
docker pull sonatype/nexus3:3.13.0
docker run -d -p 8081:8081 --name nexus sonatype/nexus3
```
http://192.168.0.99:8081
使用 admin 登录，系统会提示密码存储的位置，  
按照提示找到文件，输入密码，修改密码	

## 1.2 bare metal

```
cd /opt
wget https://download.sonatype.com/nexus/3/nexus-3.2.0-01-unix.tar.gz
tar zxvf nexus-3.2.0-01-unix.tar.gz
```
sonatyoe-work目录，用户存放仓库数据的，可根据需要将其改为其他路径，或使用软链接的方式
修改配置  
```
vim /opt/nexus-3.2.0-01/bin/nexus.vmoptions
 
修改日志存放路径、数据存放路径以及临时存放路径：
-XX:LogFile=../sonatype-work/nexus3/log/jvm.log
-Dkaraf.data=../sonatype-work/nexus3
-Djava.io.tmpdir=../sonatype-work/nexus3/tmp
```

启动nexus
```
/opt/nexus-3.2.0-01/bin/nexus start
```
# 2. Nexus3.x 批量导入本地库

local repository:/home/$USER/.m2/repository

## 2.1 create repositoy named my_repo

Repository -> Create repository -> "+"  
```
select Recipe       : maven2(hosted)  
Version policy      : Mixed  
Layout policy       : Permissive        // important
Deployment policy   : Allow redeploy
```
click "Create repository"  

## 2.2 run mavenimport.sh  

mavenimport.sh content  

```  
#!/bin/bash
# copy and run this script to the root of the repository directory containing files
# this script attempts to exclude uploading itself explicitly so the script name is       important
	# Get command line params
while getopts ":r:u:p:" opt; do
  case $opt in
    r) REPO_URL="$OPTARG"
    ;;
    u) USERNAME="$OPTARG"
    ;;
    p) PASSWORD="$OPTARG"
    ;;
esac
done

find . -type f -not -path './mavenimport\.sh*' -not -path '*/\.*' -not -path '*/\^archetype\-catalog\.xml*' -not -path '*/\^maven\-metadata\-local*\.xml' -not -path '*/\^maven\-metadata\-deployment*\.xml' | sed "s|^\./||" | xargs -I '{}' curl -u "$USERNAME:$PASSWORD" -X PUT -v -T {} ${REPO_URL}/{} ;

```  
put mavenimport.sh in folder '/home/$USER/.m2/repository', and then  

```  
chmod +x mavenimport.sh
 ./mavenimport.sh -u admin -p admin123 -r http://172.20.10.6:8081/repository/my_repo/
```  

