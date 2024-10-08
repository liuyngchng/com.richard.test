<< !
***************************************************
author      : whoami
email       : whoami@hotmail.com
create      : 2022-06-01
description :
  (1) the script need to be run with sudo
  (2) stop and disable firewall
  (3) setup iptables and start iptables.service
  (4) set iptables rules
***************************************************
!

# stop firewall
systemctl stop firewalld
systemctl disable firewalld
systemctl mask firewalld
# setup iptables
yum install iptables iptables-services -y

# 查看 iptalbls 服务状态
systemctl status iptables.service

# 启用 iptalbes 服务
systemctl start iptables.service

# 允许所有访问
iptables -P INPUT ACCEPT
# 清空所有默认规则
iptables -F INPUT
# 清空所有自定义规则
iptables -X INPUT
# 所有计数器归0
iptables -Z INPUT

# 禁止ping
iptables -A INPUT -p icmp --icmp-type 8 -s 0/0 -j DROP

### 开启ping ###
# 查看禁止ping的语句
#iptables -L INPUT --line-numbers  | grep -i icmp
# 删除语句编号
#iptables -D INPUT 14   #14代表编号
# 开启ping
#iptables -A INPUT -p icmp --icmp-type echo-request -j ACCEPT
#iptables -A OUTPUT -p icmp --icmp-type echo-reply -j ACCEPT

echo 'set rules for all app'
# docker
iptables -I INPUT -p tcp --dport 4243 -j DROP
# zookeeper
iptables -I INPUT -p tcp --dport 30001 -j DROP
iptables -I INPUT -p tcp --dport 2888 -j DROP
iptables -I INPUT -p tcp --dport 3888 -j DROP
# elastic search
iptables -I INPUT -p tcp --dport 9200 -j DROP
iptables -I INPUT -p tcp --dport 9300 -j DROP
#mysql 8.4 for app
iptables -I INPUT -p tcp --dport 33003 -j DROP

#mysql5.7 for rancher-server
iptables -I INPUT -p tcp --dport 3307 -j DROP

#redis
iptables -I INPUT -p tcp --dport 32002 -j DROP
#myapp-api
iptables -I INPUT -p tcp --dport 8080 -j DROP
#myapp-gw
iptables -I INPUT -p tcp --dport 8081 -j DROP
#myapp-plt
iptables -I INPUT -p tcp --dport 8082 -j DROP
iptables -I INPUT -p tcp --dport 19114 -j DROP
#myapp-pda
iptables -I INPUT -p tcp --dport 8083 -j DROP
#rancher
iptables -I INPUT -p tcp --dport 8084 -j DROP
iptables -I INPUT -p tcp --dport 8443 -j DROP
#kafka
iptables -I INPUT -p tcp --dport 34004 -j DROP

#prometheus
iptables -I INPUT -p tcp --dport 9090 -j DROP
#node exporter
iptables -I INPUT -p tcp --dport 9100 -j DROP
#grafana
iptables -I INPUT -p tcp --dport 3000 -j DROP
#kafka_exporter
iptables -I INPUT -p tcp --dport 9308 -j DROP


# 允许192.168.0.1-192.168.0.2 对 4243端口的访问
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 4243 -j ACCEPT

echo 'set rules for zookeeper'
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 30001 -j ACCEPT
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 2888 -j ACCEPT
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 3888 -j ACCEPT

echo 'set rules for elasticsearch'
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 9200 -j ACCEPT
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 9300 -j ACCEPT

echo 'set rules for mysqld'
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 33003 -j ACCEPT
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 3307 -j ACCEPT

echo 'set rules for app'
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 8080 -j ACCEPT
# a10
iptables -I INPUT -m iprange --src-range 192.168.11.7-192.168.11.66 -p tcp --dport 8080 -j ACCEPT

echo 'set rules for redis'
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 32002 -j ACCEPT

echo 'set rules for kafka'
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 34004 -j ACCEPT

echo 'set rules for prometheus'
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 9090 -j ACCEPT
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 9100 -j ACCEPT
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 3000 -j ACCEPT
iptables -I INPUT -m iprange --src-range 192.168.0.1-192.168.0.2 -p tcp --dport 9308 -j ACCEPT

echo 'other rules'
#rd lan
iptables -I INPUT -s 192.168.60.37 -p tcp --dport 8443 -j ACCEPT

iptables -I INPUT -m iprange --src-range 192.168.10.1-192.168.10.255 -p tcp --dport 8084 -j ACCEPT
iptables -I INPUT -s 192.168.60.37 -p tcp --dport 8443 -j ACCEPT

# prometheus

# docker
iptables -I INPUT -m iprange --src-range 172.17.0.1-172.17.0.255 -p tcp --dport 8443 -j ACCEPT


# DMZ
iptables -I INPUT -s 192.168.61.01 -p tcp --dport 8443 -j ACCEPT


iptables -I INPUT -s 10.0.0.45 -p tcp --dport 8443 -j ACCEPT



# 保存访问策略到文件
service iptables save
# 查看服务状态
systemctl is-enabled iptables.service
echo 'iptables-restore /etc/sysconfig/iptables'
iptables-restore /etc/sysconfig/iptables
echo '++++++++++++++++++++++++++++++++++++++++++++++++++++++++++'
echo '******* current host access rules as following **********'
echo '++++++++++++++++++++++++++++++++++++++++++++++++++++++++++'
# 查看当前访问策略
iptables -L -n
