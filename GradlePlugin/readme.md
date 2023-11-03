## 自定义gradle 插件
https://juejin.cn/post/7127871253414674440

由于前面的两种插件实现方式都是在gradle的“势力范围”内，所以我们不需要额外引入什么东西，但是现在独立一个模块，就得引入：implementation gradleApi()，不然Plugin<Project>就找不到了。
打包和发布插件的最简单和推荐的方法是使用Java Gradle Plugin Development Plugin，也就是Gradle插件开发插件，专门服务于插件开发的插件。
所以需要在插件模块的build.gradle中加入一下信息：



## 实现代码入侵案例
https://juejin.cn/post/7129381154121056292

本文旨在通过一个极其简洁的例子带大家一窥字节码插桩技术，不会对框架进行深入解读，因为涉及到的东西比较多，如果全盘托出，那么对于初学者来说极不友好，会因没有全局感也迷失方向，浪费更多的时间。

所谓字节码插桩其实就是在编译打包时对字节码进行操作，Android的编译是通过gradle，那么就需要用到gradle相关的知识，操作字节码就需要用到字节码相关的工具，功能比较强大且灵活的非ASM莫属，那就一步到位用ASM。

Android编译打包过程可以简述为：java文件-》class文件-》dex文件。我们插桩就是在class文件转dex的时候，dex文件里就是存放字节码的。

既然字节码插桩是在编译阶段，那么我们如何干预gradle的编译过程呢？这就需要用到gradle插件了，我们平时在build.gradle文件中通过classpath引入
的就是gradle插件，用于在编译时干一些事。所以我们也要自定义一个gradle插件，才能做插桩的事。

之所以我们能够进行插桩，是因为自从1.5.0-beta1版本开始, android gradle插件就包含了一个Transform API, 它允许第三方插件在编译后的类文件转换
为dex文件之前做处理操作。在项目构建阶段（.class -> .dex转换期间）用来修改.class文件的一套标准API，即把输入的.class文件转变成目标字节码文件。
通过Transform API我们可以拿到class文件的输入输出，拿到输入源后用ASM对文件进行修改，然后将修改后的文件保存替换到Transform的输出目录中，就达到了插桩的目的。

## ASM 字节码插桩 ：线程治理
https://zhuanlan.zhihu.com/p/583411501




