## 自定义gradle 插件
https://juejin.cn/post/7127871253414674440

在项目的根目录下新建个buildSrc文件夹，在里面去写插件内容，这种方式的插件只能项目内使用。也称为buildSrc project方式。

首先需要创建项目根目录/buildSrc/src/main/java目录，这目录是存放Java代码的，当然，如果你不想用Java写，用Kotlin或者Groovy写也可以，
创建对应的目录就行了（项目根目录/buildSrc/src/main/groovy或项目根目录/buildSrc/src/main/kotlin根据你喜欢的语言）。
创建文件夹之后sync同步，gradle就会对buildSrc做一些处理，生成一些gradle相关的文件。


