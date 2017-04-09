package com.calculator.cc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by cong on 20170121.
 */

class MySharedPreferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    MySharedPreferences(Context context){
        preferences = context.getSharedPreferences("CCconfiguration", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    boolean getButtonSound(){
        return preferences.getBoolean("buttonSound",false);
    }

    void setButtonSound(Boolean buttonSound){
        editor.putBoolean("buttonSound",buttonSound);
        editor.apply();
    }

    String getAppColor(){
        return preferences.getString("appColor", "#ffff4444");//获取颜色配置失败默认使用red_light
    }

    void setAppColor(String appColor){
        editor.putString("appColor",appColor);//十六进制颜色字符串
        editor.apply();
    }

}
