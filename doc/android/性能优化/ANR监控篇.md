## FileObserver
通过FileObserver监听某个目录下文件是否发生变化，这里不言而喻了，就是/data/anr/xxx，如果当前文件夹中的文件发生变化，那么意味着ANR发生了

但是这里需要注意的就是，很多高版本的ROM已经不支持当前文件夹的查看，甚至需要Root，因此此策略暂时不能应用，那么除此之外，还可以通过WatchDog来监控线程状态，从而判断是否发生ANR。

## WatchDog
首先WatchDog是一个线程，每隔5s发送一个Message消息到主线程的MessageQueue中，主线程Looper从消息队列中取出Message，
如果没有阻塞，那么在5s内会执行这个Message任务，就没有ANR；如果超过5s没有执行，那么就有可能出现ANR。

## 利用loop循环时的消息分发前后的日志打印（matrix使用了这个）
替换主线程Looper的Printer，监控dispatchMessage的执行时间（大部分主线程的操作最终都会执行到这个dispatchMessage中）。
这种方案在微信上有较大规模使用，总体来说性能不是很差，matrix目前的EvilMethodTracer和AnrTracer就是用这个来实现的。

```
//Looper.java
for (;;) {
  //这里可能会block，Printer无法监控到next里面发生的卡顿
    Message msg = queue.next(); // might block
    
    // This must be in a local variable, in case a UI event sets the logger
    final Printer logging = me.mLogging;
    if (logging != null) {
        logging.println(">>>>> Dispatching to " + msg.target + " " +
                msg.callback + ": " + msg.what);
    }

    msg.target.dispatchMessage(msg);

    if (logging != null) {
        logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
    }
}

public void setMessageLogging(@Nullable Printer printer) {
   mLogging = printer;
}
```

## singler 信号监听


## 排查方法
https://blog.csdn.net/StramChen/article/details/135591492
1、logcat日志， 搜索anr in
2、查看trace.txt文件

