package com.calculator.cc.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by cong on 2017/5/2.
 * 1.只有第一次打开应用程序和退出应用程序才会执行文件读写计算记录操作
 * 2.声音和主题颜色都是随设置随存文件
 * 3.关于计算记录存取,MainActivity中,按"="进行存数据操作,onCreate()、RecorderActivity返回需要数据更新时进行读数据操作;
 *              RecorderActivity中,执行删除和清空后进行存数据操作,onCreate()进行读数据操作,数据倒序操作也在此Activity
 * 4.满屏->分屏 onMultiWindowModeChanged() isInMultiWindowMode=true -> onCreate() isInMultiWindowMode=true
 *   分屏->满屏 onCreat() isInMultiWindowMode=true -> onMultiWindowModeChanged() isInMultiWindowMode=false
 * 5.屏幕旋转和分屏会销毁Activity,再执行一遍onCreate()->onStart()->onResume(),而onResume()方法中会对屏幕进行适配
 */

public class App extends Application {

    private static Context context;

    public static String textView1_temp = "";//默认为空
    public static String textView2_temp = "";//默认为空
    public static int btn_equal_NormalHeight;//其值会在第一次启动App的onResume()方法中赋予
    public static boolean isFirstStartApp = true;//这里的高度是指btn_equal的高度

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getConText(){
        return context;
    }
}
