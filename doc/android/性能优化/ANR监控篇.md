## 腾讯客户端性能分析SDK QAPM 

### 系统原理分析
ANR流程基本都是在system_server系统进程完成的，系统进程的行为我们很难监控和改变，想要监控ANR就必须找到系统进程跟我们自己的应用进程是否有交互，
如果有，两者交互的边界在哪里，边界上应用一端的行为，才是我们比较容易能监控到的，想要要找到这个边界，我们就必须要了解ANR的流程。

1、无论ANR的来源是哪里，最终都会走到ProcessRecord中的appNotResponding，这个方法包括了ANR的主要流程
2、系统会向需要dump堆栈的进程发送SIGQUIT信号
3、应用进程收到SIGQUIT后开始dump堆栈
4、应用进程dump堆栈后，会通过socket将堆栈信息发送给系统进程
5、在ANR弹窗前，会执行到makeAppNotRespondingLocked方法中，在这里会给发生ANR进程标记一个NOT_RESPONDING的flag

### ANR监控
1、hook 接收到SIGQUIT信号
2、接收到SIGQUIT信号并不能代表当前发送了ANR
3、判断当前进程是否有NOT_RESPONDING标记；

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


## ANR排查方法
https://blog.csdn.net/StramChen/article/details/135591492
### 1、logcat日志， 搜索anr in

### 2、查看trace.txt文件
trace.txt文件的整体结构: 
```
----- pid 1882 at 2022-10-02 15:37:56 -----
进程1882的详细日志
----- end 1882 -----

----- pid 1565 at 2022-10-02 15:37:57 -----
进程1565的详细日志
----- end 1565 -----

----- pid xxx at xxxx-xx-xx xx:xx:xx -----
进程xxx的详细日志
----- end xxx -----
```


文件以每个进程分块,列出了每个进程下面各个线程的活动状态, 每个进程的内容很多，关键信息如下：
```
"main" prio=5 tid=1 Blocked
  | group="main" sCount=1 dsCount=0 obj=0x75bc7c50 self=0x7a8d896a00
  | sysTid=12061 nice=0 cgrp=default sched=0/0 handle=0x7a91e23a98
  | state=S schedstat=( 401196933 229090000 643 ) utm=29 stm=10 core=4 HZ=100
  | stack=0x7ff9366000-0x7ff9368000 stackSize=8MB
  | held mutexes
```

“main”	线程名
1	prio=5	线程优先级
1	tid=1	线程标识符
1	Blocked	线程状态,表明当前线程处于阻塞状态,其它状态还有,waiting:线程等待状态,也就是挂起状态runnalbe:线程运行状态native:native是native中的线程状态,表明正在实行jni本地函数，对应java中runnable
2	group=“main”	线程所属的线程组
2	sCount=1	程被正常挂起的次数
2	dsCount=0	线程因调试而挂起次数
