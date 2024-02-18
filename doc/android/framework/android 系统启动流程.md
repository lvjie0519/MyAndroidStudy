## 系统启动流程
引导加载程序（Bootloader）：
当设备开机时，首先执行的是固化在硬件中的引导加载程序。这个程序负责初始化硬件，并加载内核映像到内存中。

Linux 内核启动：
引导加载程序加载 Linux 内核后，内核开始执行。内核的任务包括初始化各种硬件驱动、设置虚拟文件系统、创建必要的进程等。

init 进程启动：
当内核完成基本的初始化工作后，它会启动 init 进程。init 是 Android 系统中的第一个用户空间进程，它的主要任务是执行 /init.rc 文件中的指令来启动其他系统服务。

Zygote 启动：
Zygote进程是由init进程通过解析init.rc文件后fork生成的。Zygote 是一个特殊的进程，它负责孵化新的应用程序进程。Zygote 会在启动时预加载一些核心库和资源，以便于快速地创建新进程。

System Server 启动：
Zygote进程fork出System Server进程，System Server是Zygote孵化的第一个进程，地位非常重要。这是一个包含了 Android 系统中许多核心服务的进程。
这些服务包括 Activity Manager Service (AMS)、Window Manager Service (WMS)、Package Manager Service (PMS) 等。

应用程序启动：
当用户启动一个应用程序时，AMS 会通过 socket 向 Zygote 发送请求，要求其 fork 出一个新的子进程。新进程继承了 Zygote 的内存空间，并开始执行指定的应用程序代码。

应用程序初始化：
新创建的进程会执行一系列的初始化操作，包括加载应用程序的代码、解析应用程序的资源等。一旦初始化完成，该进程就可以开始处理用户界面和应用程序逻辑了。

启动 Launcher 应用：
在大多数情况下，当系统启动完成后，最后一个步骤是启动 Launcher 应用，也就是主屏幕应用。Launcher 应用显示可供用户使用的应用程序图标和其他快捷方式。
需要注意的是，上述描述是基于 Android 系统的一般工作原理，具体实现细节可能因 Android 版本的不同而有所差异。此外，随着系统的不断更新和发展，启动流程可能会发生变化。

#### 1、Zygote进程与AMS 为什么通过socket发送消息而不是Binder?
1) 时间顺序问题
Binder 由 server、client、驱动三部分组成，server都会向ServerManager 进程 进行注册， client 从ServerManager 进程中获取server proxy， ServerManager 进程和Zygote进程都是init进程启动的，
如果使用binder，可能ServerManager 进程还没启动完成；
2）多线程问题
Linux中，fork进程其实并不是完美的fork，linux设计之初只考虑到了主线程的fork，也就是说如果主进程中存在子线程，那么fork进程中，
其子线程的锁状态，挂起状态等等都是不可恢复的，只有主进程的才可以恢复。

## AMS启动流程
它负责管理应用程序的生命周期，包括应用的启动、切换、销毁等操作。
同时，AMS还负责进程调度，根据系统资源的使用情况来决定应用程序的优先级，以提高系统的整体性能。
此外，AMS还负责任务栈的管理，用于管理应用程序的界面显示和切换。

简单代码时序：
SystemServer.java
-->startBootstrapServices()
    ActivityManagerService.Lifecycle.java
    -->new ActivityManagerService(context)
    -->onStart()
        ActivityManagerService.java
        -->start()
    -->getService()  // 返回ActivityManagerService对象
    // 接下来startBootstrapServices方法中会继续调用ActivityManagerService如下方法
    ActivityManagerService.java
    -->setSystemServiceManager(mSystemServiceManager)
    -->setInstaller(installer)
    -->initPowerManagement()
    -->setSystemProcess()
 
SystemServer.java   
-->startOtherServices()
    ActivityManagerService.java
    -->installSystemProviders()
    -->setWindowManager(wm)
    -->startObservingNativeCrashes()


以Android8.0 为例：
ActivityManagerService 是在SystemServer 中调用ActivityManagerService的内部类Lifecycle 来创建的。
```java
public static final class Lifecycle extends SystemService {
        private final ActivityManagerService mService;

        public Lifecycle(Context context) {
            super(context);
            mService = new ActivityManagerService(context);
        }

        @Override
        public void onStart() {
            mService.start();
        }
        ...
}
```
创建ActivityManagerService
```
public ActivityManagerService(Context systemContext) {
    LockGuard.installLock(this, LockGuard.INDEX_ACTIVITY);
    mInjector = new Injector();
    mContext = systemContext;

    mHandlerThread = new ServiceThread(TAG,
    THREAD_PRIORITY_FOREGROUND, false /*allowIo*/);
    mHandlerThread.start();
    mHandler = new MainHandler(mHandlerThread.getLooper());
    mUiHandler = mInjector.getUiHandler(this);

    ...
}
```
主要内容包括：
    1>.主线程mHandlerThread创建并启动(Android UI线程)
    2>.创建了一个工作目录：/data/system  (AMS的活动目录)
    3>.ActiveService,ActivityStackSupervisor的创建(专门用于管理Activity)
    4>.AMS的守护进程Watchdog（俗称 “看门狗”）
    


## WMS启动流程
https://zhuanlan.zhihu.com/p/648953292

简单代码时序：
SystemServer.java
-->startOtherServices()
    WindowManagerService.java
     // 1. 创建了一个 WMS 对象
    -->main(...)
    // 初始化非常多的变量，例如 包含显示相关的窗口容器 RootWindowContainer、刷新相关的 Surface、Activity 相关的ActivityManager
    -->new WindowManagerService(...)
// 返回WindowManagerService对象，接下来在startOtherServices会继续调用WindowManagerService中的方法
WindowManagerService.java
-->onInitReady()
// 初始化显示器大小
-->displayReady()
-->systemReady()



## PMS启动流程
https://juejin.cn/post/7208188047707177015
https://juejin.cn/post/7209201396846854199

它负责了系统种应用的安装，卸载，解析，查询等等功能。
MS 启动的时候会对系统和安装的应用进行扫描并对扫描的应用进行解析，把他们的四大组件添加到 ComponentResolver 中对应的集合，后续启动的时候，就会从 ComponentResolver 中取。

简单代码时序：
SystemServer.java
-->startBootstrapServices()
    // PackageManagerService.java  构造PackageManagerService对象，添加到ServiceManager中
    -->PackageManagerService.main(...)
        // 1. 扫描frameworkDir、
        -->new PackageManagerService(...)
    
