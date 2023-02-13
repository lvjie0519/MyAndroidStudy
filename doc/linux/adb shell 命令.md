## adb shell 命令笔记

### 查看activity
```
// 查看当前activity 任务栈
adb shell dumpsys activity activities | grep Hist

// 查看当前应用的activity 信息
adb shell dumpsys activity top
```

### run-as 查看debug包 沙盒数据
```
run-as com.sangfor.atrust
```

### 清空应用数据
```
pm clear com.sangfor.atrust
或
adb shell pm clear com.sangfor.atrust
```

### adb 相关
```
// 清空日志
adb logcat -c
// 输出日志到文件
adb logcat > log.txt
// 查看某个应用程序有多少进程
adb shell ps | grep sogou.mobile.explorer
// 进入某个应用的data/data 目录
run-as com.example.hostappdemo
// 获取包 的base.apk
adb shell pm list package -f ali

// 获取日志
adb pull /sdcard/Android/data/com.sangfor.sdk.demo/files/sangforlogs ./

adb pull /sdcard/Android/data/com.sangfor.sdkdemo.main/files/sangforlogs ./

// 获取手机已安装应用的安装包
adb shell dumpsys window | adb shell grep mCurrentFocus  // 获取当前界面显示的activity，也就知道了包名
adb shell pm path com.sangfor.vpn.client.awork.std    // 打印base.apk的位置
adb pull /data/app/com.sangfor.vpn.client.awork.std-gg2jklqcC8dVBy-o5Cc_gw==/base.apk   // 拉取base.apk 到电脑上
```

### 查看某一进程占用的fd
错误信息：Abort message: 'FORTIFY: FD_ISSET: file descriptor 1025 >= FD_SETSIZE 1024'

查看进程的fd情况：
adb shell ls -l /proc/27212/fd