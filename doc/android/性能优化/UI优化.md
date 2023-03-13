## Android性能优化系列篇（一）：UI优化

### 参考文档
Android性能优化系列篇（一）：UI优化
https://zhuanlan.zhihu.com/p/573295773

### 系统做的优化
#### 硬件加速
参考文献： https://juejin.cn/post/7134257487682732045
1. 硬件加速是将CPU不擅长的图形计算转换成GPU专用指令，让更擅长图形计算的GPU来完成渲染。硬件加速改变了android的绘图模型，能提高绘图的性能;
2. API 级别为 14 及更高级别，则硬件加速默认处于启用状态;

#### 黄油计划
参考文献： https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650263098&idx=1&sn=2f760a3934d30fd3bf5ac16d5a774cc0&chksm=88633f55bf14b643aa3fa2df27faad38a68fc0120f38648eaa1bd729d466d066b0508d492e37&scene=27
#### RenderThread
当CPU处理数据给GPU后，就不需要等GPU渲染完毕了，而是将一些绘制任务交给RenderThread，这样就能减少主线程的工作，保证画面的流畅。同时还提供了RenderNode，用来做view的属性封装，渲染帧的信息等等；

### 开发优化



