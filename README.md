# KotlinMvp
##### 用于快速生成kotlin的mvp模块

### 为什么要使用该插件？

##### 方便！方便！方便！一键生成封装好的 Activity（Fragment ） + Contract + Presenter
Fragment已做懒加载，网络请求简单到爆炸
![](http://upload-images.jianshu.io/upload_images/6420758-7c637a39055c09f2.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




使用前：该插件的网络库是基于响应式的OKGO，所以要导入以下第三方库
### gradle:
```
implementation 'com.lzy.net:okrx2:2.0.2'
implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
implementation 'io.reactivex.rxjava2:rxkotlin:2.1.0'
```
别忘了在Application下初始化网络库
```
override fun onCreate() {
        super.onCreate()
        OkGo.getInstance().init(this)
    }
```
### 开始使用
1.右键项目包名，选择KtolinMvp
![右键项目](http://upload-images.jianshu.io/upload_images/6420758-6754fa3c3f613142.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2.输入base，生成基类文件夹，并且在项目下建一个view文件夹，不然无法生成mvp框架
（必须 必须 必须）
![生成基类](http://upload-images.jianshu.io/upload_images/6420758-39c91b5935b0812a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![base && view](http://upload-images.jianshu.io/upload_images/6420758-5e76c50d4cff0a91.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

3.在项目下建一个view文件夹，不然无法生成mvp框架（必须 必须 必须）
4.在view文件夹下开始愉快的创建项目
 - 如果要创建Activity，则直接输入（例如：输入Login）
![一键生成](http://upload-images.jianshu.io/upload_images/6420758-58332b39a3a60039.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 如果要创建Fragment，则输入（例如：输入HomeFragment）（Fragment不能省！！！不然得到的是Activity）
![QQ截图20171108150334.png](http://upload-images.jianshu.io/upload_images/6420758-5196e8ae77bbaf43.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

5.网络请求 简单到爆炸
![网络请求](http://upload-images.jianshu.io/upload_images/6420758-6bba37c91e3bfe5d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

[简书](http://www.jianshu.com/p/4ccb8d3175bb)

