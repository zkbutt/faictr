package top.feadre.faictr.flib;

/**
 *
 * get任务数
 *
 *
 *
 *
 * 带单选项的DIALOG + 复杂嵌套滑动 +当前网络异常，点击重试
 * 拓展 - Material Design - BottomSheetDialog 播放列表
 * dialog + 刷表刷新 - SwipeMenuItem
 * SwipeDragMoveFragment
 *
 * top.feadre.faictr
 * com.xuexiang.xuidemo
 *
 * buildTypes {
 *         ...
 *     }
 *
 *     sourceSets {
 *         main {
 *             res.srcDirs = [
 *                     'src/main/res/layout/feadre',
 *                     'src/main/res/layout',
 *
 *                     'src/main/res/drawable/feadre',
 *                     'src/main/res/drawable',
 *                     'src/main/res']
 *         }
 *     }
 * 新建 android resource 选类型
 *
 *
 * package top.feadre.fctr.flib.help;
 * /**
 *  *
 *  *
 *  * Jar包制作：
 *  * 选定版本： java -version
 *  * 生成class文件： D:\zhuanye\Java\jdk11\bin/javac -encoding UTF-8 F:\android\BYD\code\fctr\app\src\main\java\top\feadre\fctr\ftest\FJarT002.java
 *  *    例   javac -source 1.8 -target 1.8 -encoding UTF-8 lib/commons-cli-1.5.0.jar F:\android\BYD\code\fctr\app\src\main\java\top\feadre\fctr\ftest\FJarT001.java
 *  * 生成到jar包到运行目录：D:\zhuanye\Java\jdk11\bin/jar -cfm F:\android\BYD\code\fctr\app\src\main\java\top\feadre\fctr\ftest\FJarT002.jar F:\android\BYD\code\fctr\app\src\main\java\top\feadre\fctr\ftest\FJarT002.mf F:\android\BYD\code\fctr\app\src\main\java\top\feadre\fctr\ftest\*.class
 *  * //源 .java 同目录生成文件
 *  * 生成class文件： D:\zhuanye\Java\jdk11\bin/javac -encoding UTF-8 F:\android\BYD\code\fctr\app\src\main\java\top\feadre\fctr\ftest\FJarT002.java
 *  *
 *
 *
 *
 *  124.223.79.8
 *
 *  Android 11（API 级别 30）   Android 13（API 级别 33）
 *
 *
 * 调用小爱不行
 * 如何使用日志
 *
 *
 * --------------------------------- ABD命令 -----------------------------------
 *  adb pair 192.168.137.238:40579 \\ip地址和端口
 *  Enter pairing code: 433265 \\配对码
 *  Successfully paired to 192.168.137.238:40579 [guid=adb-f41e6023-  4ykY6T]
 *
 *
 *  F:\android\BYD\sdk_byd\platform-tools\adb version
 *     Android Debug Bridge version 1.0.36
 *     Revision f302a90aaaff-android
 *
 * D:\zhuanye\HBuilderX\plugins\launcher\tools\adbs\1.0.31\adb.exe
 *
 *  adb tcpip 5555
 *  adb connect 192.168.22.117:5555
 *  adb devices
 *  adb disconnect
 *
 *
 * F:\android\BYD\sdk_byd\platform-tools\adb.exe start-server
 * F:\android\BYD\sdk_byd\platform-tools\adb.exe connect 192.168.219.179:5555
 * F:\android\BYD\sdk_byd\platform-tools\adb.exe connect 192.168.137.23:5555
 * F:\android\BYD\sdk_byd\platform-tools\adb.exe connect 192.168.137.250:5555
 * F:\android\sdk\platform-tools\adb.exe connect 192.168.219.179:5555
 * adb.exe connect 192.168.137.1:5555
 * F:\android\BYD\sdk_byd\platform-tools\adb.exe devices
 *
 * F:\android\BYD\sdk_byd\platform-tools\adb.exe disconnect 192.168.219.179
 *
 * 查看安装的第三方app的包名 : F:\android\sdk\platform-tools\adb.exe shell pm list packages -3
 * 查看当前界面的app的包名 : F:\android\sdk\platform-tools\adb shell dumpsys window windows | findstr mFocusedApp
 * 查看启动的app的包名 : F:\android\sdk\platform-tools\adb shell dumpsys activity top | find "ACTIVITY"
 *
 * --- 安装APK ---
 * F:\android\sdk\platform-tools\adb install F:\android\BYD\code\fbydapp\app\debug\app-debug.apk
 * 覆盖安装：F:\android\sdk\platform-tools\adb install -r xxxx.apk
 * F:\android\sdk\platform-tools\adb uninstall 安装包包名
 *
 * 签名
 * key store password: android
 * key alias: androiddebugkey
 * key password: android
 * */