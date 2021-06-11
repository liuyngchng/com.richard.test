# 1. deploy gitlab-ce
## 1.1 delploy in docker
```
// 创建数据目录
docker pull gitlab/gitlab-ce:latest
mkdir -p /docker/gitlab/config
mkdir -p /docker/gitlab/logs
mkdir -p /docker/gitlab/data

// docker启动，直接安装镜像，这里外部访问端口使用82， ssh端口为2222
docker run -d -p 5443:443 -p 82:82 -p 2222:22 \
-v /docker/gitlab/config:/etc/gitlab \
-v /docker/gitlab/logs:/var/log/gitlab \
-v /docker/gitlab/data:/var/opt/gitlab \ 
--name=gitlab --privileged=true \
gitlab/gitlab-ce:latest
```
修改配置
```
vim /docker/gitlab/config/gitlab.rb

// 访问地址
external_url 'http://192.168.0.1:82'

// redirect http to https
nginx['redirect_http_to_https_port'] = 82

//host listern port
nginx['listen_port'] = 82

// config ssh port for ssh login
gitlab_rails['gitlab_shell_ssh_port'] = 2222
docker restart gitlab
```
浏览`http://192.168.0.1:82`， 修改密码为 psword，  
然后使用 username=root, psword=psword 进行登录

测试git ssh 是否可以正常登录，查看 gitlab ssh登录日志
```
ssh -vT git@192.168.0.1 -p 2222
gitlab-ctl tail | grep ssh
```
## 1.2 bare metal
install gitlab
```
https://about.gitlab.com/install/#ubuntu
```
