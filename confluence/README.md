
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
sudo mv /opt/atlassian/confluence/confluence/WEB-INF/lib/atlassian-extras-decoder-v2-x.x.x.jar /opt/atlassian-extras-2.4.jar
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
sudo mv atlassian-extras-2.4.jar atlassian-extras-decoder-v2-xxxxx.jar  
sudo mv atlassian-extras-decoder-v2-xxxxx.jar /opt/atlassian/confluence/confluence/WEB-INF/lib
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
```
jdbc:mysql://localhost/confluence?useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false
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
```
```
