# 1. network monitor
```
iftop
```
# 2. network config

把网卡eth0的传输设置为：延时100ms(上下误差10ms)、丢包6%、包重复0.8%、包损坏0.5%的网络环境
```
tc qdisc add dev eth0 root netem delay 100ms 10ms loss 6% duplicate 0.8% corrupt 0.5%
```
移除对网卡eth0的传输设置，恢复正常网络
```
tc qdisc del dev eth0 root netem
```
