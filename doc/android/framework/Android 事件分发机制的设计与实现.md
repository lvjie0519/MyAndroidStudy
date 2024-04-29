https://juejin.cn/post/6844903926446161927https://juejin.cn/post/6844903926446161927

## 1.架构设计
Android系统中将输入事件定义为InputEvent，而InputEvent根据输入事件的类型又分为了KeyEvent和MotionEvent，前者对应键盘事件，后者则对应屏幕触摸事件，这些事件统一由系统输入管理器InputManager进行分发。
在系统启动的时候，SystemServer会启动窗口管理服务WindowManagerService，WindowManagerService在启动的时候就会通过启动系统输入管理器InputManager来负责监控键盘消息。

InputManager负责从硬件接收输入事件，并将事件分发给当前激活的窗口（Window）处理，这里我们将前者理解为 系统服务，将后者理解为应用层级的 UI, 因此需要有一个中介负责 服务 和 UI 之间的通信，于是ViewRootImpl类应运而生。

在ActivityThread.performLaunchActivity()流程中，会创建ViewRootImpl，可参考View绘制流程；

```
public final class WindowManagerGlobal {

   public void addView(...) {
      // 3.初始化 ViewRootImpl，并执行setView()函数
      ViewRootImpl root = new ViewRootImpl(view.getContext(), display);
      root.setView(view, wparams, panelParentView);
   }
}
```

Android中Window和InputManagerService之间的通信实际上使用的InputChannel,InputChannel是一个pipe，底层实际是通过socket进行通信。在ViewRootImpl.setView()过程中，也会同时注册InputChannel

```
public final class ViewRootImpl {

  public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
      requestLayout();
      // ...
      // 创建InputChannel
      mInputChannel = new InputChannel();
      // 通过Binder在SystemServer进程中完成InputChannel的注册
      mWindowSession.addToDisplay(mWindow, mSeq, mWindowAttributes,
                            getHostVisibility(), mDisplay.getDisplayId(),
                            mAttachInfo.mContentInsets, mAttachInfo.mStableInsets,
                            mAttachInfo.mOutsets, mInputChannel);
  }
}
```

这里涉及到了WindowManagerService和Binder跨进程通信，读者不需要纠结于详细的细节，只需了解最终在SystemServer进程中，WindowManagerService根据当前的Window创建了SocketPair用于跨进程通信，
同时并对App进程中传过来的InputChannel进行了注册，这之后，ViewRootImpl里的InputChannel就指向了正确的InputChannel, 作为Client端，
其fd与SystemServer进程中Server端的fd组成SocketPair, 它们就可以双向通信了。

### 应用整体的事件分发
```java
public final class ViewRootImpl {

  final class WindowInputEventReceiver extends InputEventReceiver {
    @Override
     public void onInputEvent(InputEvent event, int displayId) {
         // 将输入事件加入队列
         enqueueInputEvent(event, this, 0, true);
     }
  }
}
```

ViewRootImpl 在这里能接收到系统层发送过来的事件，接下来就是对事件的分发了，设计者在这里使用了经典的 责任链 模式。

DecorView作为View树的根节点，接收到屏幕触摸事件MotionEvent时，应该通过递归的方式将事件分发给子View，这似乎理所当然。
但实际设计中，设计者将DecorView接收到的事件首先分发给了Activity，Activity又将事件分发给了其Window，最终Window才将事件又交回给了DecorView，形成了一个小的循环。

### UI层级事件分发

