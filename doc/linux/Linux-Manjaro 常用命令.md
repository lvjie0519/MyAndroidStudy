## Manjaro命令笔记
### 1. 同步本地的包数据库和远程的软件仓库
```
// 安装和升级软件包前，先让本地的包数据库和远程的软件仓库同步是个好习惯。
pacman -Syy
// 谨慎操作-同步软件库并更新系统到最新状态
pacman -Syu 
// 更新已安装软件
sudo pacman -Su
```
### 2.查找软件
```
sudo pacman -Ss leafpad
```
### 3.安装软件
```
sudo pacman -S leafpad
```
### 4.卸载软件包
```
// 删除单个软件包，保留其全部已经安装的依赖关系
pacman -R package_name
// 删除指定软件包，及其所有没有被其他已安装软件包使用的依赖关系：
pacman -Rs package_name

### 5.查看文件夹下文件及文件夹占用大小
sudo du -h --max-depth=1
```


### 6. android studio 缓存目录
```
// 打开 idea.properties 文件  https://www.jianshu.com/p/2d99644007da
idea.system.path=/run/media/lvjie/DATA/lvjie/android/android-studio/AndroidStudio4.1
```

### 7. 文件软链接命令
ln -s [源文件或目录] [目标文件或目录]

### 8. 解压缩文件
// 将file文件夹压缩为file.tar
tar -cvf file.tar file  

// 解压file.tar
tar -xvf file.tar

### 9. 文件拷贝
cp -r src  des
sudo cp -r UEM_CUSTOM_AUEM230_yhj/*  UEM_CUSTOM_lvjie

### 10. find
```
// 将当前目录及其子目录下所有文件后缀为 .c 的文件列出来:
find . -name "*.c"

```

### 11. ip route
```
// 查看某个ip 走的是那个路由 
ip route get xxx.xxx.xxx

example:
[lvjie@lvjie-hp ~]$ ping www.baidu.com
PING www.a.shifen.com (14.215.177.39) 56(84) 比特的数据。
ip route get 14.215.177.39 
14.215.177.39 via 172.22.21.254 dev enp2s0 src 172.22.21.219 uid 1000 


// 查看路由表
ip route show
example:
[lvjie@lvjie-hp ~]$ ip route show
default via 172.22.21.254 dev enp2s0 proto static 
default via 192.168.42.129 dev enp0s20f0u8 proto dhcp metric 101 
10.98.0.0/16 via 172.22.21.254 dev enp2s0 
10.240.0.0/16 via 172.22.21.254 dev enp2s0 
10.242.0.0/16 via 172.22.21.254 dev enp2s0 
172.22.0.0/16 via 172.22.21.254 dev enp2s0 


// 删除路由表某一项
ip route delete xxx.xxx.xxx
[lvjie@lvjie-hp ~]$ ip route delete default via 172.22.21.254 

// 添加路由表某一项
[lvjie@lvjie-hp ~]$ ip route add default via 192.168.42.129 dev enp0s20f0u5 proto dhcp metric 101 


default via 172.22.21.254 dev enp2s0 proto static 

```

### 12. 重启网络服务
```
// https://blog.csdn.net/Singlepledge/article/details/107561708
sudo systemctl restart NetworkManager.service
```


第一步
查看端口信息

ip a
得知端口名:ens33

第二步
关闭NetworkManager服务

sudo systemctl disable --now NetworkManager.service
第三步
在/etc/systemd/network/文件夹下创建ens33.network文件

sudo vim /etc/systemd/network/ens33.network
第四步
修改文件内容,配置端口

[Match]
Name=enp0s3
 
[Network]
Address=192.168.1.10/24
Gateway=192.168.1.1
DNS=8.8.8.8
DNS=8.8.4.4
第五步
保存文件后退出,开启服务

sudo systemctl enable --now systemd-networkd.service
改回dhcp:
sudo mv /etc/systemd/network/ens33.network /etc/systemd/network/ens33.network.bak
sudo systemctl restart NetworkManager.service


### 13. 杀掉某个端口对应的进程
1、查看占用端口进程的PID
lsof -i:8090
2、根据PID kill掉相关进程
kill -9 880078

### 查看密钥信息
```
// keystore 转 pem
keytool -export -rfc -keystore sangfor-test.keystore -alias sangfor-vpn -file sangfor-test.pem

// 查看证书签名信息
keytool -list -v -keystore sangfor-test.keystore

// 查看pem文件签名信息 或  apk 解压后的 rsa文件也可以用这个命令
keytool -printcert -file sangfor-test.pem
```

### git 常用命令

```
// 基于本地当前分支再创建一个本地分支
git checkout -b branch_b

// 代码行统计
git log --author="16251@sangfor.com" --pretty=tformat: --since=2023-01-31 --until=2023-02-03 --numstat | awk '{ add += $1; subs += $2; loc += $1 - $2 } END { printf "added lines: %s, removed lines: %s, total lines: %s\n", add, subs, loc }' -
```
### 查询崩溃堆栈
```
adb logcat | /home/lvjie/Android/Sdk/ndk/23.1.7779620/ndk-stack -sym /home/lvjie/sxf/projects/mobile_client/project/android/sdk/sdk/build/intermediates/cmake/debug/obj/arm64-v8a

adb logcat | /run/media/lvjie/DATA/lvjie/android/ndk/21.1.6352462/ndk-stack -sym /home/lvjie/sxf/projects/sdk_next/build/SDK/android/SangforSDK/sdk/build/intermediates/cmake/debug/obj/arm64-v8a


adb logcat | /run/media/lvjie/DATA/lvjie/android/ndk/23.1.7779620/ndk-stack -sym /home/lvjie/下载/temp/build/intermediates/cmake/partv8aRelease/obj/arm64-v8a

adb logcat | /run/media/lvjie/DATA/lvjie/android/ndk/23.1.7779620/ndk-stack -sym /run/media/lvjie/DATA/lvjie/sxf/projects/sslatrust/mobile_client/project/android/sdk/sdk/build/intermediates/cmake/partv8aDebug/obj/arm64-v8a



adb logcat | /run/media/lvjie/DATA/lvjie/android/ndk/23.1.7779620/ndk-stack -sym /home/lvjie/下载/td/build/intermediates/cmake/partv8aRelease/obj/arm64-v8a/


dmp2symbol.sh xx.so xx.dmp xx.txt
dmp2symbol.sh /run/media/lvjie/DATA/lvjie/sxf/projects/sslatrust/mobile_client/project/android/sdk/sdk/build/intermediates/cmake/partv8aDebug/obj/arm64-v8a/libsangforsdk.so


/home/xxb/Android/Sdk/ndk/21.4.7075529/toolchains/aarch64-linux-android-4.9/prebuilt/linux-x86_64/bin/aarch64-linux-android-addr2line -C -f -e obj/arm64-v8a/libsangforsdk.so 0x2bc8754

/run/media/lvjie/DATA/lvjie/android/ndk/21.1.6352462/toolchains/aarch64-linux-android-4.9/prebuilt/linux-x86_64/bin/aarch64-linux-android-addr2line -C -f -e /run/media/lvjie/DATA/lvjie/sxf/版本发布/20220706/build/intermediates/cmake/partv8aRelease/obj/arm64-v8a/libsangforsdk.so  0x2bc8754

// 自己
/run/media/lvjie/DATA/lvjie/android/ndk/21.1.6352462/toolchains/aarch64-linux-android-4.9/prebuilt/linux-x86_64/bin/aarch64-linux-android-addr2line -C -f -e /run/media/lvjie/DATA/lvjie/sxf/projects/sslatrust/mobile_client/project/android/sdk/sdk/build/intermediates/cmake/partv8aDebug/obj/arm64-v8a/libsangforsdk.so 0x108e46c 

```

### wireshark 使用
```
1. 设备控制台webconsole可以输入：tcpdump -i any port 50000 or 51004 or 50004 or 50202 -nve -s0 -w result.pcap
tcpdump -i any port 443 -nve -s0 -w result.pcap

2. 设备控制台下载zip包解压后是 result.pcap文件
3. wireshark 打开pcap文件
4. 搜索栏以字符串搜索
5. 选中请求项，右键-->追踪流-->tcp流或http流
```

