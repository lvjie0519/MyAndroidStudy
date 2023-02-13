
## 注意事项
    1. 准备1分钟自我介绍， 展示自己工作经验和规划，尽量与岗位相同；
    2. 离职动机尽量从工作本身、个人发展、公司客观变化角度谈；
    3. 熟悉简历内容

## 知识点
### 1. Android-LeakCanary原理解析
    1) 向application 注册 activity生命周期监听, 监听activity 的onActivityDestroyed方法；
    2）采用WeakReference 机制；创建WeakReference, 传递的参数为 activity 和 ReferenceQueue，
    在垃圾回收检测到被引用的对象的可达性更改后，垃圾回收器会将已注册的引用对象添加到ReferenceQueue对象中，等待ReferenceQueue处理,
    这里对象是WeakReference对象，而不是WeakReference中引用的要被回收的对象。
    即GC过后，WeakReference引用的对象被回收了，那么WeakReference引用的对象就是null，那么该WeakReference对象就会被加入到ReferenceQueue队列中。
    3) 支持监听activity 和 fragment, fragment监听是通过向FragmentManager 注册fragment 监听;
#### 1.1 如何判断是否存在内存泄漏？
    1）具体实现在RefWatcher.java ensureGone方法中， 每次出现activity onDestory 就会触发，并检查该activity 是否存在内存泄漏;
    2) 调用 removeWeaklyReachableReferences， 清除掉retainedKeys 中保存的key；
    3）判断当前WeakReference key是否在retainedKeys中，如果在，则调用一次gc；
    4）再次调用removeWeaklyReachableReferences， 清除掉retainedKeys 中保存的key；
    5）如果当前WeakReference key还在retainedKeys中， 则产生dump文件(调用Debug.dumpHprofData)；
    6) 用的是haha开源库分析dump文件

#### 1.2 学习所获
    1）LeakCanary 并不能绝对判断出内存泄漏；
    2）LeakCanary 实现思想，

### 2. Android-OkHttp原理解析
    1) 发起异步 和 同步网络请求；异步网络请求会添加到任务队列中，线程池去执行；同步网络请求直接到拦截器；

### 3. AMS
#### 3.1  AMS 类结构
        1) AMS 就是 aidl 的stub, 相当于是一个服务，客户端通过ActivityManager 来调用到AMS
#### 3.2 ActivityStack、TaskRecord、ActivityRecord 关系
        1）一个ActivityStack中会有多个TaskRecord，一个TaskRecord会有多个ActivityRecord，一个ActivityRecord其实就是对应了一个Activity，保存了一个Activity的所有信息；
