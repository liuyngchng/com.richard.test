
# 1.Introduction
The Robot Operating System (ROS) is a flexible framework for writing robot software. It is a collection of tools, libraries, and conventions that aim to simplify the task of creating complex and robust robot behavior across a wide variety of robotic platforms. 

# 2. Tutorial
http://wiki.ros.org/kinetic/Installation/Ubuntu

# 3. 解决rosdep update一直timeout

```
cd /usr/lib/python2.7/dist-packages/rosdep2/
sudo vi sources_list.py
sudo vi gbpdistro_support.py
sudo vi rep3.py
```
修改 DOWNLOAD_TIMEOUT =50.0值

# 4. CMD

```
rospack = ros + pack(age)
roscd = ros + cd
rosls = ros + ls 
```

