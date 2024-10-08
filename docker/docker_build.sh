<< !
***************************************************
author      : whoami
email       : whoami@hotmail.com
create      : 2022-06-01
description :
  (1) compile and package java program with maven
  (2) build docker images with Dockfile
  (3) upload docker image to docker hub
  (4) delete obsolete local images.
project dir tree looked like this:
+ mvn_project_home
    + cicd
        - docker_build.sh   # current file
        - mvn_child_1_project_Dockerfile
        - mvn_child_2_project_Dockerfile
    + config
        - cfg.properties
        - log4j2.xml
        - cert.keystore
    + mvn_child_1_project_dir
        - child1_pom.xml
    + mvn_child_2_project_dir
        - child2_pom.xml
    - root_pom.xml
***************************************************
!
home_dir='cicd'
host='reg.docker.your_company:5001'
img_tag='1.0.0'
cln_cmd="clean"
DOCKER='/usr/bin/docker'

docker_build(){
  cmd="${DOCKER} build --squash --no-cache -f ${home_dir}/${app[i]}_Dockerfile ./  -t ${app_hub[i]}"
  echo -e "\e[1;32m${cmd}\e[0m"
  eval ${cmd}
  cmd="${DOCKER} images | grep rd/mt-"
  echo -e "\e[1;32m${cmd}\e[0m"
  eval ${cmd}
  cmd="${DOCKER} push ${app_hub[i]}"
  echo -e "\e[1;32m${cmd}\e[0m"
  eval ${cmd}
  url="http://${host}/v2/${app_img[i]}/tags/list"
  echo ${DOCKER}' reg url='${url}
  cmd="curl -XGET ${url} --noproxy '*' -s | jq"
  echo -e "\e[1;32m${cmd}\e[0m"
  eval ${cmd}
}

echo 'usage:'
echo -e "\t ./${home_dir}/build.sh              # build all modules"
echo -e "\t ./${home_dir}/build.sh mt-gw        # build the specified module"
dir=`pwd`
if [[ ${dir} =~ ${home_dir} ]]; then
  echo -e "\e[1;31mpls goto to parent dir to run: ./${home_dir}/build.sh\e[0m"
  exit -1
fi
set -e
#set -ex


echo -e "\e[1;32mstart build\e[0m"
echo '**** packge app'
cmd="make clean -C mt-pda/cpp"
echo -e "\e[1;32m${cmd}\e[0m"
eval ${cmd}
cmd="make -C mt-pda/cpp"
echo -e "\e[1;32m${cmd}\e[0m"
eval ${cmd}
cmd="mvn clean package"
echo -e "\e[1;32m${cmd}\e[0m"
eval ${cmd}
echo '**** package finished'
param='all'
if [ -n "$1" ]; then
  param=$1
  echo "type param: ${param}"
fi

app=(`ls | grep mt-`)
for (( i=0;i<${#app[@]};i++ )) do
  echo 'app['$i']='${app[i]}
done

for (( i=0;i<${#app[@]};i++ )) do
  app_img[i]='rd/'${app[i]}
  app_hub[i]=${host}/${app_img[i]}':'${img_tag}
  #echo 'app_img['$i']='${app_img[i]}
  echo 'app_hub['$i']='${app_hub[i]}
done
#exit 0
echo '**** docker build: '${param}
if [ "${param}" == $cln_cmd ]; then
  echo ${DOCKER}' build clean'
  #exit 0
elif [[ ${param} == 'all' ]]; then
  for (( i=0;i<${#app[@]};i++ )) do
    docker_build
  done
else
  for (( i=0;i<${#app[@]};i++ )) do
    if [[ ${param} == ${app[i]} ]]; then
      docker_build
      break;
    fi
  done
fi
echo -e "\e[1;32mstart rm untagged local imges\e[0m"
old_img=`${DOCKER} images | grep '<none>' | awk -F ' ' '{print$3}'`
  if [ "${old_img}" ];then
    cmd="${DOCKER} rmi -f ${old_img}"
    echo -e "\e[1;32m${cmd}\e[0m"
    eval ${cmd}
  else
    cmd='nothing need to be rm.'
    echo -e "\e[1;32m${cmd}\e[0m"
  fi
echo -e "\e[1;32mbuild finished\e[0m"

