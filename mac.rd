# 1. mount ext4 file system
## 1.1 查看分区
```
diskutil list
mkdir ubuntu
```
## 1.2 install fuse-ext2  
see `https://github.com/alperakcan/fuse-ext2`  
## 1.2 mout  
```
sudo mount -t fuse-ext2 /dev/disk0s3 ./ubuntu/
```
