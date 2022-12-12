## 卡顿、ANR、死锁 如何排查
### 1. 卡顿、ANR
一般来说，主线程有耗时操作会导致卡顿，卡顿超过阈值，触发ANR。

主要原因集中在： handler  dispatchMessage
调用方层面主要集中在： handleMessage

### 2. 如何监控App卡顿
方法一：查看 handler 源码， 可以在 Looper.java 类的loop方法中， 看到有调用 logging.println, 因此，我们可以调用
Looper.getMainLooper().setMessageLogging(printer)，即可从回调中拿到Handler处理一个消息的前后时间。

方法二: 字节码插桩技术
微信的Matrix 使用的卡顿监控方案就是字节码插桩。

### 3. 如何分析ANR
1. app 出现anr, 会生成/data/anr/traces.txt 文件