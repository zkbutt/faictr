/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xuidemo.utils.sdkinit;

import android.app.Application;

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xormlite.XUIDataBaseRepository;
import com.xuexiang.xormlite.logs.DBLog;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xuidemo.MyApp;
import com.xuexiang.xuidemo.base.BaseActivity;
import com.xuexiang.xuidemo.base.db.InternalDataBase;
import com.xuexiang.xuidemo.utils.TokenUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.StringUtils;

/**
 * X系列基础库的初始化
 *
 * @author xuexiang
 * @since 2019-07-06 9:24
 */
public final class XBasicLibInit {

    private XBasicLibInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化基础库
     */
    public static void init(Application application) {
        initUtils(application);
        initPage(application);
        initAOP(application);
        initRouter(application);
        initDB(application);
    }

    /**
     * 初始化工具类
     *
     * @param application 应用上下文
     */
    private static void initUtils(Application application) {
        XUtil.init(application);
        XUtil.debug(MyApp.isDebug());
        TokenUtils.init(application);
    }


    /**
     * 初始化XPage页面框架
     *
     * @param application
     */
    private static void initPage(Application application) {
        //自动注册页面
        PageConfig.getInstance()
                .debug(MyApp.isDebug() ? "PageLog" : null)
                .setContainActivityClazz(BaseActivity.class)
                .init(application);
    }

    /**
     * 初始化XAOP切片框架
     *
     * @param application
     */
    private static void initAOP(Application application) {
        //初始化插件
        XAOP.init(application);
        //日志打印切片开启
        XAOP.debug(MyApp.isDebug());
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        XAOP.setOnPermissionDeniedListener(permissionsDenied -> XToastUtils.error("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ",")));
    }

    /**
     * 初始化XRouter路由
     *
     * @param application
     */
    private static void initRouter(Application application) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (MyApp.isDebug()) {
            XRouter.openLog();     // 打印日志
            XRouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        XRouter.init(application);
    }

    /**
     * 初始化数据库框架
     *
     * @param application
     */
    private static void initDB(Application application) {
        XUIDataBaseRepository.getInstance()
                //设置内部存储的数据库实现接口
                .setIDatabase(new InternalDataBase())
                .init(application);
        DBLog.debug(MyApp.isDebug());
    }

//    /**
//     * 初始化video的存放路径[xvideo项目太大，去除]
//     */
//    public static void initVideo() {
//        XVideo.setVideoCachePath(PathUtils.getExtDcimPath() + "/xvideo/");
//        // 初始化拍摄
//        XVideo.initialize(false, null);
//    }


}
