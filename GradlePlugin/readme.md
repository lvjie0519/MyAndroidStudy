## 自定义gradle 插件
https://juejin.cn/post/7127871253414674440

由于前面的两种插件实现方式都是在gradle的“势力范围”内，所以我们不需要额外引入什么东西，但是现在独立一个模块，就得引入：implementation gradleApi()，不然Plugin<Project>就找不到了。
打包和发布插件的最简单和推荐的方法是使用Java Gradle Plugin Development Plugin，也就是Gradle插件开发插件，专门服务于插件开发的插件。
所以需要在插件模块的build.gradle中加入一下信息：




