## 概况

### 如何判断目标应用进程是否启动？
AMS保存了APP进程信息，进程名即为应用包名；

### AMS是如何与Zygote进行通信的？
在Android系统中，Activity Manager Service (AMS) 与 Zygote 进程之间的通信通常是通过 Socket 来进行的。

Zygote 启动：
当 Android 系统启动时，Zygote 进程会被第一个加载和运行。Zygote 是一个特殊的进程，它负责孵化（fork）新的应用程序进程。Zygote 在启动时会预加载一些核心库和资源，以便于快速地创建新进程。
Socket 绑定：
Zygote 在启动过程中会打开一个本地套接字（socket），并监听特定的端口。这个 socket 是用来接收来自 AMS 的请求的。
AMS 请求创建新进程：
当需要启动一个新的应用程序进程时，AMS 会向 Zygote 发送一个请求，要求其 fork 出一个新的子进程。这个请求是通过之前绑定的 socket 发送的。
Zygote 创建新进程：
Zygote 收到 AMS 的请求后，会执行 fork() 系统调用，创建一个新的子进程。这个新进程将继承 Zygote 进程的内存空间，并开始执行指定的应用程序代码。
新进程初始化：
新创建的子进程会执行一系列的初始化操作，包括加载应用程序的代码、解析应用程序的资源等。一旦初始化完成，该进程就可以开始处理用户界面和应用程序逻辑了。

使用 Socket 而不是 Binder 作为通信方式的主要原因在于，当 Zygote fork 新进程时，Binder 线程可能会持有锁，这会导致 fork 操作阻塞，从而影响系统的响应速度。
而通过 Socket 通信，则可以避免这个问题，因为 Zygote 可以在一个单独的线程上处理 AMS 的请求，不会干扰到 fork 操作。

需要注意的是，上述描述是基于 Android 系统的一般工作原理，具体实现细节可能因 Android 版本的不同而有所差异。


### Activity 生命周期方法启动
ActivityThread.java
-->handleLaunchActivity()
    -->performLaunchActivity(ActivityClientRecord r, Intent customIntent)
        // Instrumentation.java  调用Instrumentation newActivity 构造一个Activity对象并返回
        -->newActivity(ClassLoader cl, String className,Intent intent)
        // Activity.java
        // 1. 会创建一个PhoneWindow， 
        -->attach(...)