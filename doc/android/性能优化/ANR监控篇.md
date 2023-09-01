## FileObserver
通过FileObserver监听某个目录下文件是否发生变化，这里不言而喻了，就是/data/anr/xxx，如果当前文件夹中的文件发生变化，那么意味着ANR发生了

但是这里需要注意的就是，很多高版本的ROM已经不支持当前文件夹的查看，甚至需要Root，因此此策略暂时不能应用，那么除此之外，还可以通过WatchDog来监控线程状态，从而判断是否发生ANR。

## WatchDog
首先WatchDog是一个线程，每隔5s发送一个Message消息到主线程的MessageQueue中，主线程Looper从消息队列中取出Message，
如果没有阻塞，那么在5s内会执行这个Message任务，就没有ANR；如果超过5s没有执行，那么就有可能出现ANR。