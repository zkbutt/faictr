<p align="center">
  <img src="https://raw.githubusercontent.com/xuexiangjys/XUI/master/art/app_logo_xui.png" width="388" height="321" alt="Banner" />
</p>

# XUI
[![](https://jitpack.io/v/xuexiangjys/XUI.svg)](https://jitpack.io/#xuexiangjys/XUI)
[![api](https://img.shields.io/badge/API-17+-brightgreen.svg)](https://android-arsenal.com/api?level=17)
[![Issue](https://img.shields.io/github/issues/xuexiangjys/XUI.svg)](https://github.com/xuexiangjys/XUI/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XUI.svg)](https://github.com/xuexiangjys/XUI)

## [英文](./README.md) | [中文](./README_ZH.md)

一个简洁而又优雅的Android原生UI框架，解放你的双手！还不赶紧点击[使用说明文档](https://github.com/xuexiangjys/XUI/wiki)，体验一下吧！

> 涵盖绝大部分的UI组件：TextView、Button、EditText、ImageView、Spinner、Picker、Dialog、PopupWindow、ProgressBar、LoadingView、StateLayout、FlowLayout、Switch、Actionbar、TabBar、Banner、GuideView、BadgeView、MarqueeView、WebView、SearchView等一系列的组件和丰富多彩的样式主题。

在提issue前，请先阅读[【提问的智慧】](https://xuexiangjys.blog.csdn.net/article/details/83344235)，并严格按照[issue模板](https://github.com/xuexiangjys/XUI/issues/new/choose)进行填写，节约大家的时间。

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XUI/wiki),重要的事情说三遍！！！

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XUI/wiki),重要的事情说三遍！！！

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XUI/wiki),重要的事情说三遍！！！

## 关于我

| 公众号   | 掘金     |  知乎    |  CSDN   |   简书   |   思否  |   哔哩哔哩  |   今日头条
|---------|---------|--------- |---------|---------|---------|---------|---------|
| [我的Android开源之旅](https://t.1yb.co/Irse)  |  [点我](https://juejin.im/user/598feef55188257d592e56ed/posts)    |   [点我](https://www.zhihu.com/people/xuexiangjys/posts)       |   [点我](https://xuexiangjys.blog.csdn.net/)  |   [点我](https://www.jianshu.com/u/6bf605575337)  |   [点我](https://segmentfault.com/u/xuexiangjys)  |   [点我](https://space.bilibili.com/483850585)  |   [点我](https://img.rruu.net/image/5ff34ff7b02dd)

## X系列库快速集成

为了方便大家快速集成X系列框架库，我提供了一个空壳模版供大家参考使用: [https://github.com/xuexiangjys/TemplateAppProject](https://github.com/xuexiangjys/TemplateAppProject)

除此之外，我还特别制作了几期[视频教程](https://space.bilibili.com/483850585/channel/detail?cid=104998)供大家学习参考.

----

## 特征

* 简洁优雅，尽可能少得引用资源文件的数量，项目库整体大小不足1M(打包后大约644k）。

* 组件丰富，提供了绝大多数我们在开发者常用的功能组件。

* 使用简单，为方便快速开发，提高开发效率，对api进行了优化，提供一键式接入。

* 样式统一，框架提供了一系列统一的样式，使UI整体看上去美观和谐。

* 兼容性高，框架还提供了3种不同尺寸设备的样式（4.5英寸、7英寸和10英寸），并且最低兼容到Android 17, 让UI兼容性更强。

* 扩展性强，各组件提供了丰富的属性和样式API，可以通过设置不同的样式属性，构建不同风格的UI。

## Star趋势

[![Stargazers over time](https://starchart.cc/xuexiangjys/XUI.svg)](https://starchart.cc/xuexiangjys/XUI)

----

## 如何使用

> 在决定使用XUI前，你必须明确的一点是，此框架给出的是一整套UI的整体解决方案，如果你只是想使用其中的几个控件，那大可不必引入如此庞大的一个UI库，Github上会有更好的组件库。如果你是想拥有一套可以定制的、统一的UI整体解决方案的话，那么你就继续往下看吧！

### 添加Gradle依赖

1.先在项目根目录的 build.gradle 的 repositories 添加:
```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.然后在应用项目(一般是app)的 `build.gradle` 的 dependencies 添加:

```
dependencies {
  ...
  //androidx项目
  implementation 'com.github.xuexiangjys:XUI:1.2.1'

  implementation 'androidx.appcompat:appcompat:1.3.1'
  implementation 'androidx.recyclerview:recyclerview:1.2.1'
  implementation 'com.google.android.material:material:1.4.0'
  implementation 'com.github.bumptech.glide:glide:4.12.0'
}
```

【注意】如果你的项目目前还未使用androidx，请使用如下配置：

```
dependencies {
  ...
  //support项目
  implementation 'com.github.xuexiangjys:XUI:1.0.9-support'

  implementation 'com.android.support:appcompat-v7:28.0.0'
  implementation 'com.android.support:recyclerview-v7:28.0.0'
  implementation 'com.android.support:design:28.0.0'
  implementation 'com.github.bumptech.glide:glide:4.8.0'
}
```

### 初始化XUI设置

1.调整应用的基础主题（必须）

> 必须设置应用的基础主题，否则组件将无法正常使用！必须保证所有用到XUI组件的窗口的主题都为XUITheme的子类，这非常重要！！！

基础主题类型：

* 大平板(10英寸, 240dpi, 1920*1200）：XUITheme.Tablet.Big

* 小平板(7英寸, 320dpi, 1920*1200）：XUITheme.Tablet.Small

* 手机（4.5英寸, 320dpi, 720*1280）：XUITheme.Phone

```
<style name="AppTheme" parent="XUITheme.Phone">
     <!-- 自定义自己的主题样式 --> 
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
 </style>

```
当然也可以在Activity刚开始时调用如下代码动态设置主题

```
@Override
protected void onCreate(Bundle savedInstanceState) {
    XUI.initTheme(this);
    super.onCreate(savedInstanceState);
    ...
}
```

2.调整字体库（对字体无要求的可省略）

（1）设置你需要修改的字体库路径（assets下）
```
//设置默认字体为华文行楷，这里写你的字体库
XUI.getInstance().initFontStyle("fonts/hwxk.ttf");
```

（2）在项目的基础Activity中加入如下代码注入字体.

注意：1.1.4版本之后使用如下设置注入
```
@Override
protected void attachBaseContext(Context newBase) {
    //注入字体
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
}
```

注意：1.1.3版本及之前的版本使用如下设置注入
```
@Override
protected void attachBaseContext(Context newBase) {
    //注入字体
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
}
```

----

## 演示（请star支持）

### 演示程序截图

![1_splash.png](./art/1_splash.png) &emsp; ![2_main.png](./art/2_main.png) &emsp; ![3_about.png](./art/3_about.png) &emsp;

![4_menu.png](./art/4_menu.png) &emsp; ![5_dialog.png](./art/5_dialog.png) &emsp; ![6_bottom_dialog.png](./art/6_bottom_dialog.png) &emsp;

![7_picker.png](./art/7_picker.png) &emsp; ![8_webview.png](./art/8_webview.png) &emsp;![9_flowlayout.png](./art/9_flowlayout.png) &emsp;

![10_ninegrid.png](./art/10_ninegrid.png) &emsp; ![11_radius_imageview.png](./art/11_radius_imageview.png) &emsp; ![12_badge_view.png](./art/12_badge_view.png) &emsp;

![13_tabview.png](./art/13_tabview.png) &emsp; ![14_citypicker.png](./art/14_citypicker.png) &emsp; ![15_refresh_layout.png](./art/15_refresh_layout.png) &emsp;

![16_spinner.png](./art/16_spinner.png) &emsp;

### Demo下载

> 演示程序大概18M（主要是demo中集成了一个小视频拍摄的库比较大，大约13M左右，而XUI库目前只有644k大小），项目比较大，推荐使用蒲公英下载。

![xui_size.png](./art/xui_size.png)

#### 蒲公英下载

> 蒲公英下载的密码: xuexiangjys

[![蒲公英](https://img.shields.io/badge/downloads-蒲公英-blue.svg)](https://www.pgyer.com/XUIDemo)

[![download_pugongying.png](./art/download_pugongying.png)](https://www.pgyer.com/XUIDemo)

#### Github下载

[![Github](https://img.shields.io/badge/downloads-Github-blue.svg)](https://github.com/xuexiangjys/XUI/blob/master/apk/xuidemo.apk?raw=true)

[![download_github.png](./art/download_github.png)](https://github.com/xuexiangjys/XUI/blob/master/apk/xuidemo.apk?raw=true)

## 贡献代码

> 由于本人精力有限，现欢迎大家踊跃贡献自己的idea，你将有机会参与到github上star过千项目的维护中，提升自己的行业影响力！

代码贡献要求：

* 请保持现有的代码样式，而不是根据您的习惯。请遵守阿里巴巴Java编码规范。

* 只需修改你确定需要优化的代码，而不是所有与你想法不同的代码。

* 在启动pull请求之前，应该充分测试提交代码。

* 请将新代码提交到dev分支，而不是主分支。

## 特别感谢

* [QMUI_Android](https://github.com/Tencent/QMUI_Android)
* [AgentWeb](https://github.com/Justson/AgentWeb)
* [Android-Iconics](https://github.com/mikepenz/Android-Iconics)
* [Android-PickerView](https://github.com/Bigkoo/Android-PickerView)
* [CityPicker](https://github.com/xuexiangjys/CityPicker)
* [ELinkageScroll](https://github.com/MFC-TEC/ELinkageScroll)
* [FlycoBanner_Master](https://github.com/H07000223/FlycoBanner_Master)
* [Linkage-RecyclerView](https://github.com/KunMinX/Linkage-RecyclerView)
* [MaterialEditText](https://github.com/rengwuxian/MaterialEditText)
* [MaterialSpinner](https://github.com/jaredrummler/MaterialSpinner)
* [MaterialProgressBar](https://github.com/DreaminginCodeZH/MaterialProgressBar)
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [PictureSelector](https://github.com/LuckSiege/PictureSelector)
* [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
* [SlideBack](https://github.com/ParfoisMeng/SlideBack)
* [SwipeRecyclerView](https://github.com/yanzhenjie/SwipeRecyclerView)


## 如果觉得项目还不错，可以考虑打赏一波

> 你的打赏是我维护的动力，我将会列出所有打赏人员的清单在下方作为凭证，打赏前请留下打赏项目的备注！

![pay.png](https://raw.githubusercontent.com/xuexiangjys/Resource/master/img/pay/pay.png)

感谢下面小伙伴的打赏：

姓名 | 金额 | 方式
:-|:-|:-
C*y | 1￥ | 微信
*流 | 1￥ | 微信
*声 | 50￥ | 微信
*宇涛 | 5￥ | 支付宝
*事 | 10￥ | 微信
优*1 | 168￥ | 微信
*、 | 20￥ | 微信
*钰晗 | 6￥ | 支付宝
*娜 | 3￥ | 微信
*米 | 20￥ | 微信
*忘 | 10￥ | 微信
*清红 | 1￥ | 支付宝
*口 | 5￥ | 微信
\* | 10.24￥ | 微信
*俊耀 | 100￥ | 支付宝
*俊杰 | 1￥ | 支付宝
*鸥 | 10.24￥ | 微信
*云 | 20.21￥ | 支付宝
*钰晗 | 66￥ | 支付宝
*杰柱 | 10￥ | 支付宝
*毛 | 6.66￥ | 微信
*凯 | 10￥ | 微信
r*o | 8.88￥ | 微信
T*8 | 7.77￥ | 微信
v*d | 20￥ | 微信
B*G | 1￥ | 微信
*舞 | 10￥ | 微信
*肉 | 2￥ | 微信
*拖 | 12.12￥ | 微信
*鱼 | 20￥ | 微信
*明 | 20￥ | 微信
*化 | 8￥ | 微信
*攀 | 16.80￥ | 支付宝
**航 | 10￥ | 支付宝
**飞 | 10.24￥ | 支付宝
*瑟 | 1￥ | 微信
*原 | 10.24￥ | 支付宝
*越 | 10.24￥ | 微信
**俊 | 80￥ | 支付宝
*尋 | 10.24￥ | 微信
爱生活 | 100￥ | QQ
*茶 | 100￥ | 微信
*头 | 2￥ | 微信
*噜 | 10.99￥ | 微信
*W*m | 10￥ | 微信
*谷 | 10￥ | 微信
*望 | 5￥ | 微信
J*o | 10.24￥ | 微信
*休 | 10.24￥ | 微信
**俊 | 80￥ | 支付宝
**伟 | 1.1￥ | 支付宝
**云 | 5￥ | 支付宝
*航 | 3￥ | 支付宝
*维 | 5￥ | 微信
*鑫 | 188.88￥ | 支付宝
*玉 | 10￥ | 支付宝
**贺 | 100￥ | 支付宝
**伟 | 65￥ | 支付宝
*G | 1￥ | 微信
M*u | 10.24￥ | 微信
S*m | 10.24￥ | 微信
T*g | 15￥ | 微信
*边 | 10.24￥ | 微信
*寻 | 20.48￥ | 微信
*凉 | 10.24￥ | 微信
S*y | 10.24￥ | 微信
M*n | 1￥ | 微信
J*e | 10.24￥ | 微信
*、 | 10.24￥ | 微信
禹*） | 1￥ | 微信
X*？ | 18.88￥ | 微信
*事 | 5￥ | 微信
*之 | 10￥ | 微信
*安 | 18.88￥ | 微信
*🎵 | 10.24￥ | 微信
*👔 | 10￥ | 微信
*洲 | 10￥ | 微信

## 联系方式

[![](https://img.shields.io/badge/XUI开源交流群-695048677-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=a2ab505862c81f1528416b585832022e835ce0abe28eefa4b0d53f8094a5691d)

[![](https://img.shields.io/badge/XUI开源交流2群-700246750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=39497f13d5e456d219be785361a282d2d9c8cd9ba7745f6170def9d90643e164)

[![](https://img.shields.io/badge/XUI开源交流3群-1090612354-blue.svg)](https://qm.qq.com/cgi-bin/qm/qr?k=nOY3GGJY-jiwzhQpR8E06G-yrOUsxCP1)

![](https://s1.ax1x.com/2022/04/27/LbGMJH.jpg)