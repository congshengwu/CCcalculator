package com.calculator.cc.activity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.calculator.cc.application.App;
import com.calculator.cc.util.CalculatorUtil;
import com.calculator.cc.MySharedPreferences;
import com.calculator.cc.R;
import com.calculator.cc.adapter.DrawerAdapter;
import com.calculator.cc.bean.DrawerItemBean;
import com.calculator.cc.util.RecorderUtil;
import com.githang.statusbar.StatusBarCompat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
//TODO 改变Icon快捷方式被删
/**
 * Created by 丛 on 2017/1/17
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    //region 变量声明
    private TextView textView1;
    private TextView textView2;
    private RadioGroup radiog1;
    private RadioButton radio2;
    private RadioButton radio8;
    private RadioGroup radiog2;
    private RadioButton radio10;
    private RadioButton radio16;
    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_dot;
    private Button btn_l;
    private Button btn_r;
    private Button btn_plus;
    private Button btn_minus;
    private Button btn_multiply;
    private Button btn_divide;
    private Button btn_percent;
    private Button btn_cls;
    private Button btn_del;
    private Button btn_equal;

    private Button btn_gen;
    private Button btn_xn;

    private Button btn_pi;
    private Button btn_tan;
    private Button btn_sin;
    private Button btn_cos;

    private Button btn_e;
    private Button btn_lg;
    private Button btn_ln;
    private Button btn_factorial;

    private DrawerLayout drawerLayout;
    private DrawerAdapter drawerAdapter;
    private ListView listView;

    private String inputNumber = "";//临时存储每次往editText里存的数字,比如说求三角函数时,就不用提取数字了,直接用inputNumber中的数字求
    private int isEqualPress = 0;//0为=按钮未被点击，1为=按钮被点击
    private int mode = 10;//等于10代表十进制 等于2代表2进制 等于8代表八进制

    private SoundPool sp;
    private int sound_button_mass;
    private int sound_button_equalAndBinary;
    private int sound_button_clsdel;

    public static Boolean buttonSound;//按钮声音是全局公共静态值

    private MySharedPreferences sharedPreferences;

    private ArrayList<String> recorderListData;

    private ImageView egg;

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout binary;

    private ImageView openRecorder1;
    private LinearLayout openRecorder2;

    private Button btn_color_now;
    private Button btn_color_red;
    private Button btn_color_green;
    private Button btn_color_blue;
    private Button btn_color_pink;
    private Button btn_color_violet;
    private Button btn_color_yellow;
    private List<Button> btnColorList;

    private boolean isColorListOpen = false;

    private PackageManager packageManager;
    private ComponentName componentName_red;
    private ComponentName componentName_green;
    private ComponentName componentName_blue;
    private ComponentName componentName_pink;
    private ComponentName componentName_violet;
    private ComponentName componentName_yellow;
    //endregion

    //region findViewById()方法
    private void findIdFunction() {
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        radiog1 = (RadioGroup) findViewById(R.id.radiog1);
        radiog2 = (RadioGroup) findViewById(R.id.radiog2);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio8 = (RadioButton) findViewById(R.id.radio8);
        radio10 = (RadioButton) findViewById(R.id.radio10);
        radio16 = (RadioButton) findViewById(R.id.radio16);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_l = (Button) findViewById(R.id.btn_l);
        btn_r = (Button) findViewById(R.id.btn_r);
        btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_minus = (Button) findViewById(R.id.btn_minus);
        btn_multiply = (Button) findViewById(R.id.btn_multiply);
        btn_divide = (Button) findViewById(R.id.btn_divide);
        btn_percent = (Button) findViewById(R.id.btn_percent);
        btn_dot = (Button) findViewById(R.id.btn_dot);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_cls = (Button) findViewById(R.id.btn_cls);
        btn_equal = (Button) findViewById(R.id.btn_equal);

        btn_gen = (Button) findViewById(R.id.btn_gen);
        btn_xn = (Button) findViewById(R.id.btn_xn);

        btn_pi = (Button) findViewById(R.id.btn_pi);
        btn_tan = (Button) findViewById(R.id.btn_tan);
        btn_sin = (Button) findViewById(R.id.btn_sin);
        btn_cos = (Button) findViewById(R.id.btn_cos);

        btn_e = (Button) findViewById(R.id.btn_e);
        btn_lg = (Button) findViewById(R.id.btn_lg);
        btn_ln = (Button) findViewById(R.id.btn_ln);
        btn_factorial = (Button) findViewById(R.id.btn_factorial);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        egg = (ImageView) findViewById(R.id.egg);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        binary = (LinearLayout) findViewById(R.id.binary);

        openRecorder1 = (ImageView) findViewById(R.id.openRecorder1);
        openRecorder2 = (LinearLayout) findViewById(R.id.openRecorder2);

        btn_color_now = (Button) findViewById(R.id.btn_color_now);
        btn_color_red = (Button) findViewById(R.id.btn_color_red);
        btn_color_green = (Button) findViewById(R.id.btn_color_green);
        btn_color_blue = (Button) findViewById(R.id.btn_color_blue);
        btn_color_pink = (Button) findViewById(R.id.btn_color_pink);
        btn_color_violet = (Button) findViewById(R.id.btn_color_violet);
        btn_color_yellow = (Button) findViewById(R.id.btn_color_yellow);

        btnColorList = new ArrayList<>();
        btnColorList.add(btn_color_red);
        btnColorList.add(btn_color_green);
        btnColorList.add(btn_color_blue);
        btnColorList.add(btn_color_pink);
        btnColorList.add(btn_color_violet);
        btnColorList.add(btn_color_yellow);
    }
    //endregion

    @Override
    //region onCreate()方法,设置按钮监听,调用处理侧滑菜单DrawerFunction()方法
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化默认字体大小,不跟随系统
        initFontScale();
        setContentView(R.layout.activity_main);

        packageManager = getApplicationContext().getPackageManager();
        componentName_red = new ComponentName(getBaseContext(),"com.calculator.cc.activity.MainActivity");
        componentName_green = new ComponentName(getBaseContext(),"com.calculator.cc.activity.MainActivityGreenIcon");
        componentName_blue = new ComponentName(getBaseContext(),"com.calculator.cc.activity.MainActivityBlueIcon");
        componentName_pink = new ComponentName(getBaseContext(),"com.calculator.cc.activity.MainActivityPinkIcon");
        componentName_violet = new ComponentName(getBaseContext(),"com.calculator.cc.activity.MainActivityVioletIcon");
        componentName_yellow = new ComponentName(getBaseContext(),"com.calculator.cc.activity.MainActivityYellowIcon");
        //调用实例化各控件方法
        findIdFunction();

        //实例化配置类
        sharedPreferences = new MySharedPreferences(MainActivity.this);
        //加载声音配置信息
        buttonSound = sharedPreferences.getButtonSound();
        //加载主题颜色信息
        App.appColor = sharedPreferences.getAppColor();
        //region 设置"="按钮背景颜色选择器颜色
        switch (App.appColor){
            case App.colorRed: {
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_red);
                btn_color_now.setBackgroundResource(R.drawable.set_color_red);
                break;
            }
            case App.colorGreen: {
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_green);
                btn_color_now.setBackgroundResource(R.drawable.set_color_green);
                break;
            }
            case App.colorBlue: {
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_blue);
                btn_color_now.setBackgroundResource(R.drawable.set_color_blue);
                break;
            }
            case App.colorPink: {
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_pink);
                btn_color_now.setBackgroundResource(R.drawable.set_color_pink);
                break;
            }
            case App.colorViolet: {
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_violet);
                btn_color_now.setBackgroundResource(R.drawable.set_color_violet);
                break;
            }
            case App.colorYellow: {
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_yellow);
                btn_color_now.setBackgroundResource(R.drawable.set_color_yellow);
                break;
            }
        }
        //endregion
        //设置公式和结果颜色
        textView1.setTextColor(Color.parseColor(App.appColor));
        textView2.setTextColor(Color.parseColor(App.appColor));
        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this, Color.parseColor(App.appColor), true);
        //region 加载toolbar
        //设置Toolbar标题
        toolbar.setTitle("标准");
        //设置标题文字颜色
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));//白色
        //设置标题背景颜色
        toolbar.setBackgroundColor(Color.parseColor(App.appColor));
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"程序出错",Toast.LENGTH_SHORT).show();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左上角图片可点击
        //endregion

        //region 实例化drawerToggle并设置点击打开drawerLayout监听
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(textView1.getText().toString().equals("CC")){
                    egg.setVisibility(View.VISIBLE);
                }else {
                    egg.setVisibility(View.GONE);
                }
                drawerToggle.syncState();
            }
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//            }
        };
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
        //endregion

        //加载计算记录
        recorderListData = RecorderUtil.getRecorderListData();
        //此处保证textView有文本时,屏幕旋转或分屏文本不消失
        if (!App.textView1_temp.equals("")){
            textView1.setText(App.textView1_temp);
        }
        if (!App.textView2_temp.equals("")){
            textView2.setText(App.textView2_temp);
        }
        //此处保证分屏或旋转计算模式保持不变
        switch (App.calMode){
            //region 标准
            case App.calModeStandard:{
                toolbar.setTitle("标准");

                binary.setVisibility(View.GONE);
                btn_pi.setVisibility(View.GONE);
                btn_tan.setVisibility(View.GONE);
                btn_sin.setVisibility(View.GONE);
                btn_cos.setVisibility(View.GONE);
                btn_gen.setVisibility(View.GONE);
                btn_e.setVisibility(View.GONE);
                btn_lg.setVisibility(View.GONE);
                btn_ln.setVisibility(View.GONE);
                btn_factorial.setVisibility(View.GONE);
                btn_xn.setVisibility(View.GONE);

                radio10.setChecked(true);

                break;
            }
            //endregion
            //region科学
            case App.calModeScience: {
                toolbar.setTitle("科学");

                binary.setVisibility(View.GONE);
                btn_pi.setVisibility(View.VISIBLE);
                btn_tan.setVisibility(View.VISIBLE);
                btn_sin.setVisibility(View.VISIBLE);
                btn_cos.setVisibility(View.VISIBLE);
                btn_gen.setVisibility(View.VISIBLE);
                btn_e.setVisibility(View.VISIBLE);
                btn_lg.setVisibility(View.VISIBLE);
                btn_ln.setVisibility(View.VISIBLE);
                btn_factorial.setVisibility(View.VISIBLE);
                btn_xn.setVisibility(View.VISIBLE);

                radio10.setChecked(true);

                break;
            }
            //endregion
            //region 进制转换
            case App.calModeBinary:{
                toolbar.setTitle("进制转换");

                binary.setVisibility(View.VISIBLE);
                btn_pi.setVisibility(View.GONE);
                btn_tan.setVisibility(View.GONE);
                btn_sin.setVisibility(View.GONE);
                btn_cos.setVisibility(View.GONE);
                btn_gen.setVisibility(View.GONE);
                btn_e.setVisibility(View.GONE);
                btn_lg.setVisibility(View.GONE);
                btn_ln.setVisibility(View.GONE);
                btn_factorial.setVisibility(View.GONE);
                btn_xn.setVisibility(View.GONE);

                break;
            }
            //endregion
        }
        //第一次运行APP不会执行此方法
        resizeUi();
    }
    //endregion


    @Override
    protected void onStart() {
        super.onStart();
        //放在这里是为了加快启动速度
        //region 设置控件们的监听事件
        textView1.addTextChangedListener(textWatcher);
        textView2.addTextChangedListener(textWatcher);

        radiog1.setOnCheckedChangeListener(this);
        radiog2.setOnCheckedChangeListener(this);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_dot.setOnClickListener(this);
        btn_l.setOnClickListener(this);
        btn_r.setOnClickListener(this);
        btn_percent.setOnClickListener(this);

        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_multiply.setOnClickListener(this);
        btn_divide.setOnClickListener(this);

        btn_cls.setOnClickListener(this);
        btn_del.setOnClickListener(this);

        btn_equal.setOnClickListener(this);

        openRecorder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RecorderActivity.class);
                startActivityForResult(intent,App.requestCode_openRecorder);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        openRecorder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RecorderActivity.class);
                startActivityForResult(intent,App.requestCode_openRecorder);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        //endregion

        //region 实例化声音池
        sp = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
        sound_button_mass = sp.load(this,R.raw.button_mass_sound,1);
        sound_button_equalAndBinary = sp.load(this,R.raw.button_equalandbinary_sound,1);
        sound_button_clsdel = sp.load(this,R.raw.button_clsdel_sound,1);
        //endregion

        drawerListView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity可能因为大小发生改变而销毁(非退出),因此将两个textView用户输入的内容临时保存,
        //  再次onCreate()创建Activity时,再显示出来
        String textView1_temp = textView1.getText().toString();
        String textView2_temp = textView2.getText().toString();
        if (!textView1_temp.equals("")){
            App.textView1_temp = textView1_temp;
        }
        if (!textView2_temp.equals("")){
            App.textView2_temp = textView2_temp;
        }
    }

    private void resizeUi(){
        textView1.post(new Runnable() {
            @Override
            public void run() {
                //第一次运行获取"="按钮高度
                if (App.isFirstStartApp){
                    App.textViewNormalHeight = textView1.getHeight();
                    App.isFirstStartApp = false;
                }else {
                    //正常模式
                    if (textView1.getHeight() == App.textViewNormalHeight){
                        //公式结果字体大小
                        textView1.setTextSize(60);
                        textView2.setTextSize(60);
                        //按钮们字体大小
                        btn_l.setTextSize(32);
                        btn_r.setTextSize(32);
                        btn_cls.setTextSize(25);
                        btn_del.setTextSize(25);
                        btn_0.setTextSize(32);
                        btn_1.setTextSize(32);
                        btn_2.setTextSize(32);
                        btn_3.setTextSize(32);
                        btn_4.setTextSize(32);
                        btn_5.setTextSize(32);
                        btn_6.setTextSize(32);
                        btn_7.setTextSize(32);
                        btn_8.setTextSize(32);
                        btn_9.setTextSize(32);
                        btn_plus.setTextSize(32);
                        btn_minus.setTextSize(32);
                        btn_multiply.setTextSize(32);
                        btn_divide.setTextSize(32);
                        btn_percent.setTextSize(32);
                        btn_dot.setTextSize(32);
                        btn_equal.setTextSize(40);

                        btn_pi.setTextSize(30);
                        btn_e.setTextSize(30);
                        btn_tan.setTextSize(30);
                        btn_sin.setTextSize(30);
                        btn_cos.setTextSize(30);
                        btn_lg.setTextSize(30);
                        btn_ln.setTextSize(30);
                        btn_factorial.setTextSize(30);
                        btn_gen.setTextSize(30);
                        btn_xn.setTextSize(30);
                        //单选按钮大小设置
                        radiog1.setOrientation(LinearLayout.VERTICAL);
                        radiog2.setOrientation(LinearLayout.VERTICAL);
                        radio2.setTextSize(20);
                        radio8.setTextSize(20);
                        radio10.setTextSize(20);
                        radio16.setTextSize(20);
                    }
                    //分屏或横屏模式
                    else {
                        //公式结果字体大小
                        textView1.setTextSize(25);
                        textView2.setTextSize(25);
                        //按钮们字体大小
                        btn_l.setTextSize(20);
                        btn_r.setTextSize(20);
                        btn_cls.setTextSize(18);
                        btn_del.setTextSize(18);
                        btn_0.setTextSize(20);
                        btn_1.setTextSize(20);
                        btn_2.setTextSize(20);
                        btn_3.setTextSize(20);
                        btn_4.setTextSize(20);
                        btn_5.setTextSize(20);
                        btn_6.setTextSize(20);
                        btn_7.setTextSize(20);
                        btn_8.setTextSize(20);
                        btn_9.setTextSize(20);
                        btn_plus.setTextSize(20);
                        btn_minus.setTextSize(20);
                        btn_multiply.setTextSize(20);
                        btn_divide.setTextSize(20);
                        btn_percent.setTextSize(20);
                        btn_dot.setTextSize(20);
                        btn_equal.setTextSize(25);
                        btn_pi.setTextSize(18);

                        btn_e.setTextSize(18);
                        btn_tan.setTextSize(18);
                        btn_sin.setTextSize(18);
                        btn_cos.setTextSize(18);
                        btn_lg.setTextSize(18);
                        btn_ln.setTextSize(18);
                        btn_factorial.setTextSize(18);
                        btn_gen.setTextSize(18);
                        btn_xn.setTextSize(18);
                        //单选按钮大小设置
                        radiog1.setOrientation(LinearLayout.HORIZONTAL);
                        radiog2.setOrientation(LinearLayout.HORIZONTAL);
                        radio2.setTextSize(10);
                        radio8.setTextSize(10);
                        radio10.setTextSize(10);
                        radio16.setTextSize(10);
                    }
                }
            }
        });
    }

    //region 侧滑菜单的listView
    private void drawerListView(){
        //按钮声音监听在“设置控件们的监听中”
        listView = (ListView) findViewById(R.id.listView);
        List<DrawerItemBean> drawerList = new ArrayList<>();
        DrawerItemBean drawerItemBean1 = new DrawerItemBean();
        drawerItemBean1.setDrawerItemText("标准");

        DrawerItemBean drawerItemBean2 = new DrawerItemBean();
        drawerItemBean2.setDrawerItemText("科学");

        DrawerItemBean drawerItemBean3 = new DrawerItemBean();
        drawerItemBean3.setDrawerItemText("进制转换");

        DrawerItemBean drawerItemBean4 = new DrawerItemBean();

        if (buttonSound) {
            drawerItemBean4.setDrawerItemText("按键音效开");
        }else {
            drawerItemBean4.setDrawerItemText("按键音效关");
        }
        drawerList.add(drawerItemBean1);
        drawerList.add(drawerItemBean2);
        drawerList.add(drawerItemBean3);
        drawerList.add(drawerItemBean4);

        drawerAdapter = new DrawerAdapter(MainActivity.this,drawerList,sp,sound_button_equalAndBinary);
        listView.setAdapter(drawerAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    //region 标准
                    case 0:{
                        App.calMode = App.calModeStandard;
                        toolbar.setTitle("标准");

                        binary.setVisibility(View.GONE);
                        btn_pi.setVisibility(View.GONE);
                        btn_tan.setVisibility(View.GONE);
                        btn_sin.setVisibility(View.GONE);
                        btn_cos.setVisibility(View.GONE);
                        btn_gen.setVisibility(View.GONE);
                        btn_e.setVisibility(View.GONE);
                        btn_lg.setVisibility(View.GONE);
                        btn_ln.setVisibility(View.GONE);
                        btn_factorial.setVisibility(View.GONE);
                        btn_xn.setVisibility(View.GONE);

                        radio10.setChecked(true);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    //endregion
                    //region科学
                    case 1: {
                        App.calMode = App.calModeScience;
                        toolbar.setTitle("科学");

                        binary.setVisibility(View.GONE);
                        btn_pi.setVisibility(View.VISIBLE);
                        btn_tan.setVisibility(View.VISIBLE);
                        btn_sin.setVisibility(View.VISIBLE);
                        btn_cos.setVisibility(View.VISIBLE);
                        btn_gen.setVisibility(View.VISIBLE);
                        btn_e.setVisibility(View.VISIBLE);
                        btn_lg.setVisibility(View.VISIBLE);
                        btn_ln.setVisibility(View.VISIBLE);
                        btn_factorial.setVisibility(View.VISIBLE);
                        btn_xn.setVisibility(View.VISIBLE);

                        radio10.setChecked(true);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    //endregion
                    //region 进制转换
                    case 2:{
                        App.calMode = App.calModeBinary;
                        toolbar.setTitle("进制转换");

                        binary.setVisibility(View.VISIBLE);
                        btn_pi.setVisibility(View.GONE);
                        btn_tan.setVisibility(View.GONE);
                        btn_sin.setVisibility(View.GONE);
                        btn_cos.setVisibility(View.GONE);
                        btn_gen.setVisibility(View.GONE);
                        btn_e.setVisibility(View.GONE);
                        btn_lg.setVisibility(View.GONE);
                        btn_ln.setVisibility(View.GONE);
                        btn_factorial.setVisibility(View.GONE);
                        btn_xn.setVisibility(View.GONE);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    //endregion
                }
            }
        });

    }
    //endregion

    @Override
    //region按钮们的响应事件函数onClick()
    public void onClick(View v) {
        //region按钮0~9
        if (v.getId() == R.id.btn_0) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 2 || mode == 8 || mode == 16) {
                textView1.append("0");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 000123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击0就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "0";
            textView1.append("0");
        } else if (v.getId() == R.id.btn_1) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 2 || mode == 8 || mode == 16) {
                textView1.append("1");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "1";
            textView1.append("1");
        } else if (v.getId() == R.id.btn_2) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 8 || mode == 16) {
                textView1.append("2");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "2";
            textView1.append("2");
        } else if (v.getId() == R.id.btn_3) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 8 || mode == 16) {
                textView1.append("3");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "3";
            textView1.append("3");
        } else if (v.getId() == R.id.btn_4) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 8 || mode == 16) {
                textView1.append("4");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "4";
            textView1.append("4");
        } else if (v.getId() == R.id.btn_5) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 8 || mode == 16) {
                textView1.append("5");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "5";
            textView1.append("5");
        } else if (v.getId() == R.id.btn_6) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 8 || mode == 16) {
                textView1.append("6");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "6";
            textView1.append("6");
        } else if (v.getId() == R.id.btn_7) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 8 || mode == 16) {
                textView1.append("7");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "7";
            textView1.append("7");
        } else if (v.getId() == R.id.btn_8) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 8 || mode == 16) {
                textView1.append("8");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "8";
            textView1.append("8");
        } else if (v.getId() == R.id.btn_9) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (mode == 8 || mode == 16) {
                textView1.append("9");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.length() > 0 && !inputNumber.contains(".") && inputNumber.equals("0"))//防止出现 0123 这种输入，长度不为0且不含小数点，说明输入状态还在小数点前部分,在点击这个就不好使
                return;                                                 //↑如果小数点前的数字是0开头，则不可再输入
            inputNumber += "9";
            textView1.append("9");
        }//endregion
        //region小数点
        else if (v.getId() == R.id.btn_dot) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            if (inputNumber.contains("."))//如果一个数字中含有小数点，就不能再向这个数字中输入小数点了
                return;
            String theLastChar = "";//该变量用于存textBox中的最后一个字符
            if (!textView1.getText().toString().equals(""))//下面的作用是防止出现   666.66.
            {
                if (textView1.getText().toString().length() > 0)//防止越界
                    theLastChar = textView1.getText().toString().substring(textView1.length() - 1);//首下标是0
                if (!theLastChar.equals("+") && !theLastChar.equals("-") && !theLastChar.equals("×") && !theLastChar.equals("÷") &&
                        !theLastChar.equals(".") && !theLastChar.equals("(") && !theLastChar.equals(")")) {
                    inputNumber += ".";
                    textView1.append(".");
                }
            }
        }//endregion
        //region左右括号
        else if (v.getId() == R.id.btn_l) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            ///////十六进制时 ( 变 A 模式/////////////////////
            if (mode == 16) {
                textView1.append("A");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }
            String theLastChar = "";
            if (textView1.getText().toString().length() > 0)//防止越界
                theLastChar = textView1.getText().toString().substring(textView1.length() - 1);

            if (theLastChar.equals("+") || theLastChar.equals("-") || theLastChar.equals("×") || theLastChar.equals("÷") || theLastChar.equals("")
                    || theLastChar.equals("(")){
                textView1.append("(");
            }
        } else if (v.getId() == R.id.btn_r) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            ///////十六进制时 ) 变 B 模式/////////////////////
            if (mode == 16) {
                textView1.append("B");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }

            String theLastChar = "";
            if (textView1.getText().length() > 0) {//防止越界
                theLastChar = textView1.getText().toString().substring(textView1.length() - 1);
            }

            if (!theLastChar.equals("") && !theLastChar.equals("+") && !theLastChar.equals("-") && !theLastChar.equals("×") && !theLastChar.equals("÷") &&
                    !theLastChar.equals(".") && !theLastChar.equals("("))//&& theLastChar != ")")
                textView1.append(")");
        }
        //endregion
        //region %
        else if (v.getId() == R.id.btn_percent) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            if (isEqualPress == 1)
            {
                double x1 = Double.valueOf(textView2.getText().toString());
                x1 = x1 / 100.0;//求%
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg = bg.stripTrailingZeros();
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    textView2.setText("0");
                    isEqualPress = 1;
                    return;
                }
                textView2.setText(bg.toPlainString());
                isEqualPress = 1;//将textbox2中的结果%了，相当于按 = 号了
                return;
            }
            if (!inputNumber.equals(""))//输入过程中按 % 情况
            {
                double x1 = Double.valueOf(inputNumber);
                x1 = x1 / 100.0;//求%
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg = bg.stripTrailingZeros();
                try
                {
                    //删掉原数字
                    int length = inputNumber.length();
                    textView1.setText(textView1.getText().toString().substring(0,textView1.length()-length));
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    inputNumber = "0";
                    textView1.append(inputNumber);
                    return;
                }
                inputNumber = bg.toPlainString();
                textView1.append(inputNumber);

                //inputNumber = "";//重新置空

            }
        }
        //endregion&gen
        //region +-×÷
        else if (v.getId() == R.id.btn_plus) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            ///////十六进制时  + 变 F 模式/////////////////////
            if (mode == 16) {
                textView1.append("F");
                return;
            }

            //按完等号后,提取下面文本框的数字
            if (isEqualPress == 1) {
                textView1.setText(textView2.getText().toString());
                double x1 = Double.valueOf(textView2.getText().toString());
                if (x1 < 0) {
                    textView1.setText("(0" + textView2.getText().toString() + ")");
                }
                isEqualPress = 0;
            }
            inputNumber = "";
            String theLastChar = "";//该变量用于存textBox中的最后一个字符
            if (!textView1.getText().toString().equals("")) {
                if (textView1.length() > 0)//防止越界
                    theLastChar = textView1.getText().toString().substring(textView1.length() - 1);//首下标是0
                if (!theLastChar.equals("+") && !theLastChar.equals("-") && !theLastChar.equals("×") && !theLastChar.equals("÷") && !theLastChar.equals(".") && !theLastChar.equals("("))
                    textView1.append("+");
            }
        } else if (v.getId() == R.id.btn_minus) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            ///////十六进制时  - 变 E 模式/////////////////////
            if (mode == 16) {
                textView1.append("E");
                return;
            }

            if (isEqualPress == 1) {
                try {
                    textView1.setText(textView2.getText().toString());
                    isEqualPress = 0;//////////////////////为了防止出bug，isEqualPress = 0 放在下面始终不会执行，再按 - 又会执行下面的这个 bug ,就把isEqualPress = 0;放在上面解决这个问题
                    double x1 = Double.valueOf(textView2.getText().toString());//会出现位置bug，导致程序退出

                    if (x1 < 0) {
                        textView1.setText("(0" + textView2.getText().toString() + ")");
                        //TextBox1.Text = "(0" + TextBox2.Text + ")";
                    }

                } catch (Exception ex) {
                    inputNumber = "";
                    textView1.setText("");
                    textView2.setText("");
                    return;
                }
            }
            inputNumber = "";
            String theLastChar = "";//该变量用于存textBox中的最后一个字符
            if (!textView1.getText().toString().equals("")) {
                if (textView1.length() > 0)//防止越界
                    theLastChar = textView1.getText().toString().substring(textView1.length() - 1);
                if (theLastChar.equals("("))//输入过程中 输入 - 负号情况
                {
                    textView1.append("0-");
                    //TextBox1.Text += "0-";
                    return;
                }
                if (!theLastChar.equals("+") && !theLastChar.equals("-") && !theLastChar.equals("×") && !theLastChar.equals("÷") && !theLastChar.equals("."))
                    textView1.append("-");
            }
            //开头输入 - 负号情况
            if (textView1.getText().toString().equals("")) {
                textView1.append("(0-");
                //TextBox1.Text += "(0-";
            }
            // (后输入 - 负号情况
        } else if (v.getId() == R.id.btn_multiply) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            ///////十六进制时  × 变 D 模式/////////////////////
            try {
                if (mode == 16) {
                    textView1.append("D");
                    //TextBox1.Text += "C";
                    return;
                }

                if (isEqualPress == 1) {
                    textView1.setText(textView2.getText().toString());

                    double x1 = Double.valueOf(textView2.getText().toString());
                    if (x1 < 0) {
                        textView1.setText("(0" + textView2.getText().toString() + ")");
                    }

                    isEqualPress = 0;
                }
                inputNumber = "";
                String theLastChar = "";//该变量用于存textBox中的最后一个字符
                if (!textView1.getText().toString().equals("")) {
                    if (textView1.getText().toString().length() > 0)//防止越界
                        theLastChar = textView1.getText().toString().substring(textView1.length() - 1);
                    if (!theLastChar.equals("+") && !theLastChar.equals("-") && !theLastChar.equals("×") && !theLastChar.equals("÷") && !theLastChar.equals(".") && !theLastChar.equals("(")) {
                        textView1.append("×");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.btn_divide) {
            if (buttonSound)
                sp.play(sound_button_mass,1,1,0,0,1);
            ///////十六进制时  ÷ 变 C 模式/////////////////////
            if (mode == 16) {
                textView1.append("C");
                return;
            }
            if (isEqualPress == 1) {
                textView1.setText(textView2.getText().toString());

                double x1 = Double.valueOf(textView2.getText().toString());
                if (x1 < 0) {
                    textView1.setText("(0" + textView2.getText().toString() + ")");
                }

                isEqualPress = 0;
            }
            inputNumber = "";
            String theLastChar = "";//该变量用于存textBox中的最后一个字符
            if (!textView1.getText().toString().equals("")) {
                if (textView1.length() > 0)//防止越界
                    theLastChar = textView1.getText().toString().substring(textView1.length() - 1);
                if (!theLastChar.equals("+") && !theLastChar.equals("-") && !theLastChar.equals("×") && !theLastChar.equals("÷") && !theLastChar.equals(".") && !theLastChar.equals("("))
                    textView1.append("÷");
            }
        }//endregion
        //region 删除
        else if (v.getId() == R.id.btn_del) {
            if (buttonSound)
                sp.play(sound_button_clsdel,1,1,0,0,1);
            //得出结果后,按删除，相当于修改输入计算式，回到 按 = 之前状态
            if (isEqualPress == 1){
                isEqualPress = 0;
            }
            String showAt1 = textView1.getText().toString();
            //下面是textView1显示内容的删除
            if (!showAt1.equals("")) {//防止越界 // ↓ 这个才是在textView1中删除
                if (showAt1.length()>2 && showAt1.substring(showAt1.length()-2,showAt1.length()-1).equals("^")){//这种情况是输入“ 5^( ” 此时 “ ^( ”要一起删除
                    textView1.setText(showAt1.substring(0, textView1.length() - 2));
                }else {//否则正常删除
                    textView1.setText(showAt1.substring(0, textView1.length() - 1));//删掉最后一个字符,截取从第一个字符开始之后长度为“原长-1”个长度字符
                }
            }
            showAt1 = textView1.getText().toString();

            //textView1内容改变要保持inputNumber里的数字更新
            if(!showAt1.equals("")){
                char[] temp = showAt1.toCharArray();
                if (temp[temp.length-1]=='0' || temp[temp.length-1]=='1' || temp[temp.length-1]=='2' || temp[temp.length-1]=='3' ||
                        temp[temp.length-1]=='4' || temp[temp.length-1]=='5' || temp[temp.length-1]=='6' ||
                        temp[temp.length-1]=='7' || temp[temp.length-1]=='8' || temp[temp.length-1]=='9'){
                    inputNumber = "";
                    for (int i=temp.length-1; i>=0; i--){
                        if (temp[i]!='0' && temp[i]!='1' && temp[i]!='2' && temp[i]!='3' &&
                                temp[i]!='4' && temp[i]!='5' && temp[i]!='6' &&
                                temp[i]!='7' && temp[i]!='8' && temp[i]!='9'){
                            inputNumber = showAt1.substring(i+1);
                            return;
                        }
                    }
                    inputNumber = showAt1;
                }
            }

        }
        //endregion
        //region 清空
        else if (v.getId() == R.id.btn_cls) {
            if (buttonSound)
                sp.play(sound_button_clsdel,1,1,0,0,1);
            isEqualPress = 0;
            inputNumber = "";
            textView1.setText("");
            textView2.setText("");
            App.textView1_temp = "";
            App.textView2_temp = "";
        }//endregion
        //region 等于
        else if (v.getId() == R.id.btn_equal) {
            if (buttonSound)
                sp.play(sound_button_equalAndBinary,1,1,0,0,1);
            String showAt1 =textView1.getText().toString();
            if (showAt1.equals("")) {
                return;
            }
            isEqualPress = 1;
            String temp = showAt1;
            //region  revison for 左右小括号数目判断
            if (temp.contains("(") || temp.contains(")"))//如果包含括号，判断括号是否 左右括号数相等
            {
                char[] tempChar = temp.toCharArray();
                int leftNum = 0;
                int rightNum = 0;
                for (char aTempChar : tempChar) {
                    if (aTempChar == '(') {
                        leftNum++;
                    } else if (aTempChar == ')') {
                        rightNum++;
                    }
                }
                if (leftNum != rightNum) {
                    Toast.makeText(MainActivity.this,"计算式不正确",Toast.LENGTH_SHORT).show();
                    isEqualPress = 0;//没计算成功 该变量变回0
                    return;
                }
            }
            //endregion
            //region revison for 判断最后一个字符,如果是符号或点,就不允许计算 2017.01.20
            String theLastChar = temp.substring(temp.length()-1);
            if(theLastChar.equals("+") || theLastChar.equals("-") || theLastChar.equals("×") || theLastChar.equals("÷") || theLastChar.equals(".")){
                Toast.makeText(MainActivity.this,"计算式不正确",Toast.LENGTH_SHORT).show();
                isEqualPress = 0;//没计算成功 该变量变回0
                return;
            }
            //endregion

            //region revison for 处理符号 "^" 2017.02.19
            try{
                if(temp.contains("^"))
                    temp=dealThePow(temp);//已经替换完 * /
            }catch (Exception e){//捕获已知异常 x^((数字))
                Toast.makeText(MainActivity.this,"计算式错误",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            //endregion
            try {
                temp = temp.replace("×", "*");
                temp = temp.replace("÷", "/");
                inputNumber = "";

                //↓↓↓↓↓↓↓↓↓↓↓↓↓↓核心计算部分↓↓↓↓↓↓↓↓↓↓↓↓↓
                double x1 = CalculatorUtil.calculate(temp);
                //↑↑↑↑↑↑↑↑↑↑↑↑↑↑核心计算部分↑↑↑↑↑↑↑↑↑↑↑↑↑

                //保留小数点后10位,将double转换为bg并保留10位小数
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);
                bg=bg.stripTrailingZeros();
                String showAt2 = bg.toPlainString();
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    textView2.setText("0");
                    //如果textView1为空,上面有个if语句会阻止程序往下进行,所以不用管文本框为空的情况
                    String strRecorder=showAt1+"=0";
                    recorderListData.add(strRecorder);
                    RecorderUtil.saveRecorderListData(recorderListData);
                    return;
                }
                //判断小数点后是否为10位,好弹出保留提示
                if (showAt2.contains(".")) {
                    int theDotPosition = showAt2.indexOf(".");//位置从0开始
                    int theResultLength = showAt2.length();//位置从1开始
                    if (theResultLength - (theDotPosition + 1) == 10)
                        Toast.makeText(MainActivity.this, "结果已保留10位小数", Toast.LENGTH_SHORT).show();
                }
                textView2.setText(showAt2);

                //如果textView1为空,上面有个if语句会阻止程序往下进行,所以不用管文本框为空的情况
                if (recorderListData.size() == 30) {//最大记录为30个,超出30将会把最开始存的那个移除,再添加
                    recorderListData.remove(0);
                    String strRecorder=showAt1+"="+showAt2;
                    recorderListData.add(strRecorder);
                    RecorderUtil.saveRecorderListData(recorderListData);
                }else {
                    String strRecorder=showAt1+"="+showAt2;
                    recorderListData.add(strRecorder);
                    RecorderUtil.saveRecorderListData(recorderListData);
                }

            } catch (Exception ex) {
                Toast.makeText(MainActivity.this,"错误",Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
        //endregion
    }
    //endregion

    //region单选按钮们的监听事件onCheckedChanged()方法
    private boolean changeGroup=false;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(group!=null && checkedId!=-1 && !changeGroup){//changeGroup==false
            if(group==radiog1){
                //region二进制
                if(checkedId==R.id.radio2){
                    if (buttonSound)
                        sp.play(sound_button_equalAndBinary,1,1,0,0,1);
                    btn_0.setEnabled(true);
                    btn_1.setEnabled(true);
                    btn_2.setEnabled(false);
                    btn_3.setEnabled(false);
                    btn_4.setEnabled(false);
                    btn_5.setEnabled(false);
                    btn_6.setEnabled(false);
                    btn_7.setEnabled(false);
                    btn_8.setEnabled(false);
                    btn_9.setEnabled(false);
                    btn_l.setEnabled(false);
                    btn_r.setEnabled(false);
                    btn_plus.setEnabled(false);
                    btn_minus.setEnabled(false);
                    btn_multiply.setEnabled(false);
                    btn_divide.setEnabled(false);
                    btn_dot.setEnabled(false);
                    btn_percent.setEnabled(false);
                    btn_equal.setEnabled(false);

                    btn_l.setText("(");
                    btn_r.setText(")");
                    btn_divide.setText("÷");
                    btn_multiply.setText("×");
                    btn_minus.setText("-");
                    btn_plus.setText("+");
                    try {
                        int temp;
                        if (mode == 8) {
                            if (!textView1.getText().toString().equals("")) {
                                //8转2
                                String s8 = Integer.toBinaryString(Integer.valueOf(textView1.getText().toString(), 8));
                                textView1.setText(s8);
                            }
                        }
                        if (mode == 10) {
                            //如果textBox2不为空,且数字为整数，那就转换textBox2
                            if (!textView2.getText().toString().equals("") && ((int) Double.parseDouble(textView2.getText().toString()) == Double.parseDouble(textView2.getText().toString())))
                            {
                                temp = Integer.parseInt(textView2.getText().toString());
                                if (temp < 0) {//禁止TetxBox2中的负数向二进制转换
                                    changeGroup=true;
                                    radiog2.clearCheck();
                                    changeGroup=false;
                                    return;
                                }
                                String s10 = Integer.toBinaryString(temp);//10转2
                                textView1.setText(s10);
                                textView2.setText("");
                                mode = 2;
                                changeGroup=true;
                                radiog2.clearCheck();
                                changeGroup=false;
                                return;
                            }
                            if (!textView1.getText().toString().equals("")) {
                                temp = Integer.parseInt(textView1.getText().toString());
                                String s10 = Integer.toBinaryString(temp);//10转2
                                textView1.setText(s10);
                            }
                        }
                        if (mode == 16) {
                            if (!textView1.getText().toString().equals("")) {
                                //16转2
                                String s16 = Integer.toBinaryString(Integer.valueOf(textView1.getText().toString(), 16));
                                textView1.setText(s16);
                            }
                        }
                    } catch (Exception ex) {
                        inputNumber = "";
                        textView1.setText("");//cls清屏
                        textView2.setText("");
                        if (textView1.getText().toString().length()==0){
                            Toast.makeText(MainActivity.this,"数值过大",Toast.LENGTH_SHORT).show();
                        }
                    }
                    mode = 2;
                    isEqualPress = 0;//置0
                }
                //endregion
                //region八进制
                else if(checkedId==R.id.radio8){
                    if (buttonSound)
                        sp.play(sound_button_equalAndBinary,1,1,0,0,1);
                    btn_0.setEnabled(true);
                    btn_1.setEnabled(true);
                    btn_2.setEnabled(true);
                    btn_3.setEnabled(true);
                    btn_4.setEnabled(true);
                    btn_5.setEnabled(true);
                    btn_6.setEnabled(true);
                    btn_7.setEnabled(true);
                    btn_8.setEnabled(false);
                    btn_9.setEnabled(false);
                    btn_l.setEnabled(false);
                    btn_r.setEnabled(false);
                    btn_plus.setEnabled(false);
                    btn_minus.setEnabled(false);
                    btn_multiply.setEnabled(false);
                    btn_divide.setEnabled(false);
                    btn_dot.setEnabled(false);
                    btn_percent.setEnabled(false);
                    btn_equal.setEnabled(false);

                    btn_l.setText("(");
                    btn_r.setText(")");
                    btn_divide.setText("÷");
                    btn_multiply.setText("×");
                    btn_minus.setText("-");
                    btn_plus.setText("+");
                    try {
                        int temp;
                        if (mode == 2) {
                            if (!textView1.getText().toString().equals("")) {
                                //2转8
                                String s2 = Integer.toOctalString(Integer.parseInt(textView1.getText().toString(), 2));
                                textView1.setText(s2);
                            }
                        }
                        if (mode == 10) {
                            //如果textBox2不为空,且数字为整数，那就转换textBox2
                            if (!textView2.getText().toString().equals("") && ((int) Double.parseDouble(textView2.getText().toString()) == Double.parseDouble(textView2.getText().toString())))
                            {
                                temp = Integer.parseInt(textView2.getText().toString());
                                if (temp < 0) {//禁止TetxBox2中的负数向八进制转换
                                    changeGroup=true;
                                    radiog2.clearCheck();
                                    changeGroup=false;
                                    return;
                                }
                                String s10 = Integer.toOctalString(temp);//10转8
                                textView1.setText(s10);
                                textView2.setText("");
                                mode = 8;
                                changeGroup=true;
                                radiog2.clearCheck();
                                changeGroup=false;
                                return;
                            }
                            if (!textView1.getText().toString().equals("")) {
                                temp = Integer.parseInt(textView1.getText().toString());
                                String s10 = Integer.toOctalString(temp);//10转8
                                textView1.setText(s10);
                            }
                        }
                        if (mode == 16) {
                            if (!textView1.getText().toString().equals("")) {
                                //16转8
                                String s16 = Integer.toOctalString(Integer.valueOf(textView1.getText().toString(), 16));
                                textView1.setText(s16);
                            }
                        }
                    } catch (Exception ex) {
                        inputNumber = "";
                        textView1.setText("");//cls清屏
                        textView2.setText("");
                        if (textView1.getText().toString().length()==0){
                            Toast.makeText(MainActivity.this,"数值过大",Toast.LENGTH_SHORT).show();
                        }
                    }
                    mode = 8;
                    isEqualPress = 0;//置0
                }
                //endregion
                changeGroup=true;
                radiog2.clearCheck();
                changeGroup=false;
            } else if(group==radiog2){
                //region十进制
                if(checkedId==R.id.radio10){
                    if (buttonSound)
                        sp.play(sound_button_equalAndBinary, 1, 1, 0, 0, 1);
                    btn_0.setEnabled(true);
                    btn_1.setEnabled(true);
                    btn_2.setEnabled(true);
                    btn_3.setEnabled(true);
                    btn_4.setEnabled(true);
                    btn_5.setEnabled(true);
                    btn_6.setEnabled(true);
                    btn_7.setEnabled(true);
                    btn_8.setEnabled(true);
                    btn_9.setEnabled(true);
                    btn_l.setEnabled(true);
                    btn_r.setEnabled(true);
                    btn_plus.setEnabled(true);
                    btn_minus.setEnabled(true);
                    btn_multiply.setEnabled(true);
                    btn_divide.setEnabled(true);
                    btn_dot.setEnabled(true);
                    btn_percent.setEnabled(true);
                    btn_equal.setEnabled(true);

                    btn_l.setText("(");
                    btn_r.setText(")");
                    btn_divide.setText("÷");
                    btn_multiply.setText("×");
                    btn_minus.setText("-");
                    btn_plus.setText("+");
                    try {
                        if (mode == 2) {
                            if (!textView1.getText().toString().equals("")) {
                                //2转10
                                String s2 = Integer.valueOf(textView1.getText().toString(), 2).toString();
                                textView1.setText(s2);
                            }
                        }
                        if (mode == 8) {
                            if (!textView1.getText().toString().equals("")) {
                                //8转10
                                String s8 = Integer.valueOf(textView1.getText().toString(), 8).toString();
                                textView1.setText(s8);
                            }
                        }
                        if (mode == 16) {
                            if (!textView1.getText().toString().equals("")) {
                                //16转10
                                String s16 = Integer.valueOf(textView1.getText().toString(), 16).toString();
                                textView1.setText(s16);
                            }
                        }
                    } catch (Exception ex) {
                        inputNumber = "";
                        textView1.setText("");//cls清屏
                        textView2.setText("");
                        if (textView1.getText().toString().length()==0){
                            Toast.makeText(MainActivity.this,"数值过大",Toast.LENGTH_SHORT).show();
                        }
                    }
                    mode = 10;
                    isEqualPress = 0;//置0
                }
                //endregion
                //region十六进制
                else if(checkedId==R.id.radio16){
                    if (buttonSound)
                        sp.play(sound_button_equalAndBinary,1,1,0,0,1);
                    btn_0.setEnabled(true);
                    btn_1.setEnabled(true);
                    btn_2.setEnabled(true);
                    btn_3.setEnabled(true);
                    btn_4.setEnabled(true);
                    btn_5.setEnabled(true);
                    btn_6.setEnabled(true);
                    btn_7.setEnabled(true);
                    btn_8.setEnabled(true);
                    btn_9.setEnabled(true);
                    btn_l.setEnabled(true);
                    btn_r.setEnabled(true);
                    btn_plus.setEnabled(true);
                    btn_minus.setEnabled(true);
                    btn_multiply.setEnabled(true);
                    btn_divide.setEnabled(true);
                    btn_dot.setEnabled(false);
                    btn_percent.setEnabled(false);
                    btn_equal.setEnabled(false);

                    btn_l.setText("A");
                    btn_r.setText("B");
                    btn_divide.setText("C");
                    btn_multiply.setText("D");
                    btn_minus.setText("E");
                    btn_plus.setText("F");
                    try {
                        int temp;
                        String temps;
                        if (mode == 2) {
                            if (!textView1.getText().toString().equals("")) {
                                //2转16
                                temps = Integer.toHexString(Integer.parseInt(textView1.getText().toString(), 2));
                                temps = temps.replace("a", "A");
                                temps = temps.replace("b", "B");
                                temps = temps.replace("c", "C");
                                temps = temps.replace("d", "D");
                                temps = temps.replace("e", "E");
                                temps = temps.replace("f", "F");
                                textView1.setText(temps);
                            }
                        }
                        if (mode == 8) {
                            if (!textView1.getText().toString().equals("")) {
                                //8转16
                                temps = Integer.toHexString(Integer.valueOf(textView1.getText().toString(), 8));
                                temps = temps.replace("a", "A");
                                temps = temps.replace("b", "B");
                                temps = temps.replace("c", "C");
                                temps = temps.replace("d", "D");
                                temps = temps.replace("e", "E");
                                temps = temps.replace("f", "F");
                                textView1.setText(temps);
                            }
                        }
                        if (mode == 10) {
                            if (!textView2.getText().toString().equals("") && ((int) Double.parseDouble(textView2.getText().toString()) == Double.parseDouble(textView2.getText().toString())))//如果textBox2不为空,且数字为整数，那就转换textBox2
                            {
                                temp = Integer.parseInt(textView2.getText().toString());
                                if (temp < 0) {//禁止TetxBox2中的负数向二进制转换
                                    changeGroup=true;
                                    radiog2.clearCheck();
                                    changeGroup=false;
                                    return;
                                }
                                temps = Integer.toHexString(temp);//10转16
                                temps = temps.replace("a", "A"); temps = temps.replace("b", "B"); temps = temps.replace("c", "C"); temps = temps.replace("d", "D"); temps = temps.replace("e", "E"); temps = temps.replace("f", "F");
                                textView1.setText(temps);
                                textView2.setText("");
                                mode = 16;
                                changeGroup=true;
                                radiog2.clearCheck();
                                changeGroup=false;
                                return;
                            }
                            if (!textView1.getText().toString().equals("")) {
                                //10转16
                                temp = Integer.parseInt(textView1.getText().toString());
                                temps = Integer.toHexString(temp);
                                temps = temps.replace("a", "A");
                                temps = temps.replace("b", "B");
                                temps = temps.replace("c", "C");
                                temps = temps.replace("d", "D");
                                temps = temps.replace("e", "E");
                                temps = temps.replace("f", "F");
                                textView1.setText(temps);
                            }
                        }
                    } catch (Exception ex) {
                        inputNumber = "";
                        textView1.setText("");//cls清屏
                        textView2.setText("");
                        if (textView1.getText().toString().length()==0){
                            Toast.makeText(MainActivity.this,"数值过大",Toast.LENGTH_SHORT).show();
                        }
                    }
                    mode = 16;
                    isEqualPress = 0;//置0
                }
                //endregion
                changeGroup=true;
                radiog1.clearCheck();
                changeGroup=false;
            }
        }
    }
    //endregion

    //region字体大小处理TextChanged()方法
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (textView1.getHeight() == App.textViewNormalHeight) {
                textChanged(60);
            }else {
                textChanged(25);
            }
        }
    };
    //endregion

    float theSize;
    private void textChanged(int textSize){
        //textView1文本改变后响应处理
        int lineCount=textView1.getLineCount();
        //在华为荣耀4A手机上,按π再按tan会时lineCount返回值为0,不知道原因,先这样单独处理了
        if (lineCount == 0){
            textView1.setTextSize(textSize/2);
        }

        if(lineCount == 2 && textView1.getTextSize() != theSize){//注意getTextSize()和setTextSize()对应的值不同
            textView1.setTextSize(textSize/2);
        }else if(lineCount == 3){
            textView1.setTextSize(textSize/3);
            theSize=textView1.getTextSize();
        }
        //textView2文本改变后响应处理
        int lineCount2=textView2.getLineCount();
        if (lineCount2 == 2){
            textView2.setTextSize(textSize/2);
        }

        //当文本框字符串长度小于等于8时,字体大小变回默认尺寸
        if (textView1.length()<9){
            textView1.setTextSize(textSize);
        }
        if (textView2.length()<9){
            textView2.setTextSize(textSize);
        }
    }

    //region重写屏幕滑动dispatchTouchEvent()方法,弹出侧滑菜单
    private float x1=0,x2=0,y1=0,y2=0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            //手指按下屏幕
            case MotionEvent.ACTION_DOWN:
                x1=ev.getX();
                y1=ev.getY();
                break;
            //手指滑动屏幕
            case MotionEvent.ACTION_MOVE:
                break;
            //手指从屏幕抬起
            case MotionEvent.ACTION_UP:
                x2=ev.getX();
                y2=ev.getY();
                //手指向右滑动弹出drawlayout
                if(x2-x1>200 && (y2-y1<200 && y2-y1>-200)){
                    drawerLayout.openDrawer(GravityCompat.START);
                    if(textView1.getText().toString().equals("CC")){
                        egg.setVisibility(View.VISIBLE);
                    }else {
                        egg.setVisibility(View.GONE);
                    }
                }
                //手指向左滑动打开RecorderActivity
                else if(x1-x2>200 && (y2-y1<200 && y2-y1>-200)){
                    //drawerLayout.closeDrawer(GravityCompat.START);
                    drawerLayout.closeDrawer(GravityCompat.START);

                    //在drawerLayout关闭情况下左滑才会执行如下代码,弹出历史记录Activity
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {

                        Intent intent = new Intent(MainActivity.this, RecorderActivity.class);
                        startActivityForResult(intent, App.requestCode_openRecorder);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                    }
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
    //endregion

    //region onKeyDown() 双击返回
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出CC计算器", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();//结束Activity
                //退出APP后改变图标
                switch (App.appColor){
                    case App.colorRed: changeIconRed(); break;
                    case App.colorGreen: changeIconGreen(); break;
                    case App.colorBlue: changeIconBlue(); break;
                    case App.colorPink: changeIconPink(); break;
                    case App.colorViolet: changeIconViolet(); break;
                    case App.colorYellow: changeIconYellow(); break;
                }
                System.exit(0);//完全结束整个App进程,所有资源均被释放
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //endregion

    public void onBtnLeftFourClick(View view) {
        if (buttonSound)
            sp.play(sound_button_mass,1,1,0,0,1);
        //region√
        if(view.getId()==R.id.btn_gen){
            String showAt1 = textView1.getText().toString();
            String showAt2 = textView2.getText().toString();

            //结尾非数字 按gen就不好使。比如textView1输入 3)
            //前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就不能不好使了
            if(showAt2.equals("")) {
                if (showAt1.equals("")) {
                    return;
                }
                boolean bool = Character.isDigit(showAt1.charAt(textView1.length() - 1));
                //非数字
                if (!bool) {
                    return;
                }
            }
            if (isEqualPress == 1) {
                double x1 = Double.valueOf(showAt2);//能处理带E的数字
                if(x1<0) {//负数不能开平方
                    Toast.makeText(MainActivity.this,"不能为负数",Toast.LENGTH_SHORT).show();
                    return;
                }
                x1 = Math.sqrt(x1);//求开平方值
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    textView2.setText("0");
                    isEqualPress = 1;
                    return;
                }
                textView2.setText(bg.toPlainString());
                isEqualPress = 1;
                return;
            }
            if (!inputNumber.equals(""))//输入过程中按 gen 情况
            {
                double x1 = Double.valueOf(inputNumber);
                x1 = Math.sqrt(x1);//求开平方值
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                try {
                    int length = inputNumber.length();
                    textView1.setText(showAt1.substring(0, textView1.length() - length));//删掉textBox1中需要开平方的数
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    inputNumber = "0";
                    textView1.append(inputNumber);
                    //inputNumber = "";
                    return;
                }
                inputNumber = bg.toPlainString();
                textView1.append(inputNumber);
                //inputNumber = "";//重新置空
            }
        }
        //endregion
        //region x²&x³点击响应处理事件 (已弃用)
//        else if(view.getId()==R.id.btn_x2){
//            String showAt1 = textView1.getText().toString();
//            String showAt2 = textView2.getText().toString();
//
//            //结尾非数字 按x²就不好使。比如textView1输入 3),前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就不能不好使了
//            if(showAt2.equals("")) {
//                if (showAt1.equals("")) {
//                    return;
//                }
//                boolean bool = Character.isDigit(showAt1.charAt(textView1.length() - 1));
//                //非数字
//                if (!bool) {
//                    return;
//                }
//            }
//
//            if (isEqualPress == 1) {
//                double x1 = Double.valueOf(textView2.getText().toString());//能处理带E的数字
//                x1 = Math.pow(x1,2);//求平方
//                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
//                bg=bg.stripTrailingZeros();
//                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
//                    textView2.setText("0");
//                    isEqualPress = 1;
//                    return;
//                }
//                textView2.setText(bg.toPlainString());
//                isEqualPress = 1;
//                return;
//            }
//            if (!inputNumber.equals(""))//输入过程中按 x² 情况
//            {
//                double x1 = Double.valueOf(inputNumber);
//                x1 = Math.pow(x1,2);//求平方值
//                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
//                bg=bg.stripTrailingZeros();
//                try {
//                    int length = inputNumber.length();
//                    textView1.setText(showAt1.substring(0, textView1.length() - length));//删掉textBox1中需要开平方的数
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
//                    inputNumber = "0";
//                    textView1.append(inputNumber);
//                    //inputNumber = "";
//                    return;
//                }
//                inputNumber = bg.toPlainString();
//                textView1.append(inputNumber);
//                //inputNumber = "";//重新置空
//            }
//        }else if(view.getId()==R.id.btn_x3){
//            //结尾非数字 按x³就不好使。比如textView1输入 3)
//            //前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就不能不好使了
//            if(textView2.getText().toString().equals("")) {
//                String text1Strs = textView1.getText().toString();
//                if (text1Strs.equals("")) {
//                    return;
//                }
//                boolean bool = Character.isDigit(text1Strs.charAt(textView1.length() - 1));
//                //非数字
//                if (!bool) {
//                    return;
//                }
//            }
//
//            if (isEqualPress == 1) {
//                double x1 = Double.valueOf(textView2.getText().toString());//能处理带E的数字
//                x1 = Math.pow(x1,3);//求立方
//                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
//                bg=bg.stripTrailingZeros();
//                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
//                    textView2.setText("0");
//                    isEqualPress = 1;
//                    return;
//                }
//                textView2.setText(bg.toPlainString());
//                isEqualPress = 1;
//                return;
//            }
//            if (!inputNumber.equals(""))//输入过程中按 x³ 情况
//            {
//                double x1 = Double.valueOf(inputNumber);
//                x1 = Math.pow(x1,3);//求立方
//                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
//                bg=bg.stripTrailingZeros();
//                try {
//                    int length = inputNumber.length();
//                    textView1.setText(textView1.getText().toString().substring(0, textView1.length() - length));//删掉textBox1中需要开平方的数
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
//                    inputNumber = "0";
//                    textView1.append(inputNumber);
//                    //inputNumber = "";
//                    return;
//                }
//                inputNumber = bg.toPlainString();
//                textView1.append(inputNumber);
//                //inputNumber = "";//重新置空
//            }
//        }
        //endregion
        //region x^n
        else if(view.getId()==R.id.btn_xn){
            String text1Strs=textView1.getText().toString();
            if(text1Strs.equals("")){
                return;
            }
            char theLastChar = text1Strs.charAt(textView1.length()-1);
            boolean bool=Character.isDigit(theLastChar);
            if(bool || theLastChar==')'){//是数字或 )都可以输入^
                textView1.append("^(");
            }
        }
        //endregion
        //region e
        else if(view.getId()==R.id.btn_e){
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }

            String theLastChar = "";
            if (textView1.getText().toString().length() > 0)//防止越界
                theLastChar = textView1.getText().toString().substring(textView1.length() - 1);

            if (theLastChar.equals("+") || theLastChar.equals("-") || theLastChar.equals("×") || theLastChar.equals("÷") || theLastChar.equals("")
                    || theLastChar.equals("(")){
                textView1.append(""+Math.E);
                inputNumber=""+Math.E;
            }
        }
        //endregion
        //region lg&ln
        else if(view.getId()==R.id.btn_lg){
            String showAt1 = textView1.getText().toString();
            String showAt2 = textView2.getText().toString();

            //结尾非数字 按lg就不好使。比如textView1输入 3),前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就好使了
            if(showAt2.equals("")) {
                if (showAt1.equals("")) {
                    return;
                }
                boolean bool = Character.isDigit(showAt1.charAt(textView1.length() - 1));
                //非数字
                if (!bool) {
                    return;
                }
            }

            //按等于号后textView2有结果了，isEqualPress=1; 此时再按 log 会将结果 给 log
            if (isEqualPress == 1)
            {
                double x1 = Double.valueOf(showAt2);//能处理带E的数字
                if (x1 <= 0) {// log所计算的值必须大于0
                    Toast.makeText(MainActivity.this,"不能为负数",Toast.LENGTH_SHORT).show();
                    return;
                }
                x1 = Math.log10(x1);//以10为底，x1的对数
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    textView2.setText("0");
                    isEqualPress = 1;
                    return;
                }
                textView2.setText(bg.toPlainString());
                isEqualPress = 1;//将textbox2中的结果log了，相当于按 = 号了
                return;
            }
            //输入过程中按 log 情况
            if (!inputNumber.equals("") && (Double.valueOf(inputNumber) > 0))
            {
                double x1 = Double.valueOf(inputNumber);
                if (x1 <= 0)// log所计算的值必须大于0
                    return;
                x1 = Math.log10(x1);//以10为底，x1的对数
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                try {
                    int length = inputNumber.length();
                    textView1.setText(showAt1.substring(0, textView1.length() - length));//删掉textBox1中需要log的数
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    inputNumber = "0";
                    textView1.append(inputNumber);
                    return;
                }
                if (x1 < 0){//lg可能textView1中产生负数,因此要在左右加括号
                    inputNumber = bg.toPlainString();
                    textView1.append("(0"+inputNumber+")");
                }else {
                    inputNumber = bg.toPlainString();
                    textView1.append(inputNumber);
                }
            }
        }else if(view.getId() == R.id.btn_ln){
            String showAt1 = textView1.getText().toString();
            String showAt2 = textView2.getText().toString();

            //结尾非数字 按ln就不好使。比如textView1输入 3),前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就不能不好使了
            if(showAt2.equals("")) {
                if (showAt1.equals("")) {
                    return;
                }
                boolean bool = Character.isDigit(showAt1.charAt(textView1.length() - 1));
                //非数字
                if (!bool) {
                    return;
                }
            }

            //按等于号后textView2有结果了，isEqualPress=1; 此时再按 log 会将结果 给 log
            if (isEqualPress == 1)
            {
                double x1 = Double.valueOf(showAt2);//能处理带E的数字
                if (x1 <= 0) {// log所计算的值必须大于0
                    Toast.makeText(MainActivity.this,"不能为负数",Toast.LENGTH_SHORT).show();
                    return;
                }
                x1 = Math.log(x1);//以e为底，x1的对数
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    textView2.setText("0");
                    isEqualPress = 1;
                    return;
                }
                textView2.setText(bg.toPlainString());
                isEqualPress = 1;//将textbox2中的结果log了，相当于按 = 号了
                return;
            }
            //输入过程中按 log 情况
            if (!inputNumber.equals("") && (Double.valueOf(inputNumber) > 0))
            {
                double x1 = Double.valueOf(inputNumber);
                if (x1 <= 0)// log所计算的值必须大于0
                    return;
                x1 = Math.log(x1);//以e为底，x1的对数
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                try {
                    int length = inputNumber.length();
                    textView1.setText(showAt1.substring(0, textView1.length() - length));//删掉textBox1中需要log的数
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    inputNumber = "0";
                    textView1.append(inputNumber);
                    return;
                }
                if (x1 < 0){//ln可能textView1中产生负数,因此要在左右加括号
                    inputNumber = bg.toPlainString();
                    textView1.append("(0"+inputNumber+")");
                }else {
                    inputNumber = bg.toPlainString();
                    textView1.append(inputNumber);
                }
            }
        }
        //endregion
        //region !
        else if(view.getId()==R.id.btn_factorial){
            String showAt1 = textView1.getText().toString();
            String showAt2 = textView2.getText().toString();

            //结尾非数字 按!就不好使。比如textView1输入 3),前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就得好使了
            if(showAt2.equals("")) {
                if (showAt1.equals("")) {
                    return;
                }
                boolean bool = Character.isDigit(showAt1.charAt(textView1.length() - 1));
                //非数字
                if (!bool) {
                    return;
                }
            }
            //对结果进行操作
            if (isEqualPress == 1) {
                double x1 = Double.valueOf(textView2.getText().toString());
                if (x1>440){//超出此范围,fact方法就崩了
                    Toast.makeText(MainActivity.this,"数值过大",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(x1<0) {//负数没有阶乘
                    Toast.makeText(MainActivity.this,"不能为负数",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(x1 != (int)x1){//是小数暂时返回不求阶乘
                    Toast.makeText(MainActivity.this,"暂不能求小数阶乘",Toast.LENGTH_SHORT).show();
                    return;
                }
                x1 = fact(x1);//求阶乘
                int x1i = (int) x1;
                String x1s = "" + x1i;
                textView2.setText(x1s);
                isEqualPress = 1;
                return;
            }
            //输入过程中按 ! 情况
            if (!inputNumber.equals(""))
            {
                double x1 = Double.valueOf(inputNumber);
                if (x1>440){//超出此范围,fact方法就崩了
                    Toast.makeText(MainActivity.this,"数值过大",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(x1 != (int)x1){//是小数暂时返回不求阶乘
                    Toast.makeText(MainActivity.this,"暂不能求小数阶乘",Toast.LENGTH_SHORT).show();
                    return;
                }
                x1 = fact(x1);//求阶乘
                try {
                    //删除原来TextView1的数
                    int length = inputNumber.length();
                    textView1.setText(textView1.getText().toString().substring(0, textView1.length() - length));//删掉textBox1中需要开平方的数
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                int x1i = (int) x1;
                inputNumber = "" + x1i;
                textView1.append(inputNumber);
                //inputNumber = "";//重新置空
            }
        }
        //endregion
        //region π&tan&sin&cos
        else if(view.getId()==R.id.btn_pi){
            if (isEqualPress == 1) {
                textView1.setText("");
                isEqualPress = 0;
            }

            String theLastChar = "";
            if (textView1.getText().toString().length() > 0)//防止越界
                theLastChar = textView1.getText().toString().substring(textView1.length() - 1);

            if (theLastChar.equals("+") || theLastChar.equals("-") || theLastChar.equals("×") || theLastChar.equals("÷") || theLastChar.equals("")
                    || theLastChar.equals("(")){
                textView1.append(""+Math.PI);
                inputNumber=""+Math.PI;
            }
        } else if(view.getId()==R.id.btn_tan){
            String showAt1 = textView1.getText().toString();
            String showAt2 = textView2.getText().toString();

            //结尾非数字 按tan就不好使。比如textView1输入 3),前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就得好使了
            if(showAt2.equals("")) {
                if (showAt1.equals("")) {
                    return;
                }
                boolean bool = Character.isDigit(showAt1.charAt(textView1.length() - 1));
                //非数字
                if (!bool) {
                    return;
                }
            }

            if (isEqualPress == 1)//按等于号后textBox2有结果了，isEqualPress=1；此时再按 tan 会将结果 给 tan
            {
                double x1 = Double.valueOf(showAt2);
                x1 = Math.tan(x1 * Math.PI / 180);//求tan值
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                //超过此值视为无穷tan90位无穷,但是取不到tan90
                if (bg.doubleValue()>999999999){
                    Toast.makeText(MainActivity.this,"无效",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    textView2.setText("0");
                    isEqualPress = 1;
                    return;
                }
                textView2.setText(bg.toPlainString());
                isEqualPress = 1;//将textbox2中的结果tan了，相当于按 = 号了
                return;
            }
            if (!inputNumber.equals(""))//输入过程中按 tan 情况
            {
                double x1 = Double.valueOf(inputNumber);
                x1 = Math.tan(x1 * Math.PI / 180);//求tan值
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                //超过此值视为无穷tan90位无穷,但是取不到tan90
                if (bg.doubleValue()>999999999){
                    Toast.makeText(MainActivity.this,"无效",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int length = inputNumber.length();
                    textView1.setText(showAt1.substring(0, textView1.length() - length));//删掉textBox1中需要求tan的数
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    inputNumber = "0";
                    textView1.append(inputNumber);
                    return;
                }
                if (x1 < 0){//tan可能textView1中产生负数,因此要在左右加括号
                    inputNumber = bg.toPlainString();
                    textView1.append("(0"+inputNumber+")");
                }else {
                    inputNumber = bg.toPlainString();
                    textView1.append(inputNumber);
                }
            }
        }else if(view.getId()==R.id.btn_sin){
            String showAt1 = textView1.getText().toString();
            String showAt2 = textView2.getText().toString();

            //结尾非数字 按sin就不好使。比如textView1输入 3),前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就得好使了
            if(showAt2.equals("")) {
                if (showAt1.equals("")) {
                    return;
                }
                boolean bool = Character.isDigit(showAt1.charAt(textView1.length() - 1));
                //非数字
                if (!bool) {
                    return;
                }
            }

            if (isEqualPress == 1)//按等于号后textBox2有结果了，isEqualPress=1；此时再按 tan 会将结果 给 tan
            {
                double x1 = Double.valueOf(showAt2);
                x1 = Math.sin(x1 * Math.PI / 180);//求tan值
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    textView2.setText("0");
                    isEqualPress = 1;
                    return;
                }
                textView2.setText(bg.toPlainString());
                isEqualPress = 1;//将textbox2中的结果tan了，相当于按 = 号了
                return;
            }
            if (!inputNumber.equals(""))//输入过程中按 tan 情况
            {
                double x1 = Double.valueOf(inputNumber);
                x1 = Math.sin(x1 * Math.PI / 180);//求tan值
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                try {
                    int length = inputNumber.length();
                    textView1.setText(showAt1.substring(0, textView1.length() - length));//删掉textBox1中需要求tan的数
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    inputNumber = "0";
                    textView1.append(inputNumber);
                    return;
                }
                if (x1 < 0){//sin可能textView1中产生负数,因此要在左右加括号
                    inputNumber = bg.toPlainString();
                    textView1.append("(0"+inputNumber+")");
                }else {
                    inputNumber = bg.toPlainString();
                    textView1.append(inputNumber);
                }
            }
        }else if(view.getId()==R.id.btn_cos){
            String showAt1 = textView1.getText().toString();
            String showAt2 = textView2.getText().toString();

            //结尾非数字 按cos就不好使。比如textView1输入 3),前提当然是textView2不能有数字啦！比如textView1:2^(2) textView2:4,此时就得好使了
            if(showAt2.equals("")) {
                if (showAt1.equals("")) {
                    return;
                }
                boolean bool = Character.isDigit(showAt1.charAt(textView1.length() - 1));
                //非数字
                if (!bool) {
                    return;
                }
            }

            if (isEqualPress == 1)//按等于号后textBox2有结果了，isEqualPress=1；此时再按 tan 会将结果 给 tan
            {
                double x1 = Double.valueOf(showAt2);
                x1 = Math.cos(x1 * Math.PI / 180);//求tan值
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    textView2.setText("0");
                    isEqualPress = 1;
                    return;
                }
                textView2.setText(bg.toPlainString());
                isEqualPress = 1;//将textbox2中的结果tan了，相当于按 = 号了
                return;
            }
            if (!inputNumber.equals(""))//输入过程中按 tan 情况
            {
                double x1 = Double.valueOf(inputNumber);
                x1 = Math.cos(x1 * Math.PI / 180);//求tan值
                BigDecimal bg = new BigDecimal(x1).setScale(10, BigDecimal.ROUND_HALF_UP);//保留小数点后10位
                bg=bg.stripTrailingZeros();
                try {
                    int length = inputNumber.length();
                    textView1.setText(showAt1.substring(0, textView1.length() - length));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if(bg.doubleValue() == 0){//bg.stripTrailingZeros()处理不了0.0000000000的零
                    inputNumber = "0";
                    textView1.append(inputNumber);
                    return;
                }
                if (x1 < 0){//cos可能textView1中产生负数,因此要在左右加括号
                    inputNumber = bg.toPlainString();
                    textView1.append("(0"+inputNumber+")");
                }else {
                    inputNumber = bg.toPlainString();
                    textView1.append(inputNumber);
                }
            }
        }
        //endregion
    }

    //region 阶乘计算函数
    /**
     * @param n 需要进行阶乘计算的整数,因为全局计算数都是用double,所以这里也用double代替整数
     * @return  返回阶乘计算结果
     * function 正整数和0阶乘计算
     * note     传进来的数字超过440将报错
     */
    private double fact(double n) {
        if(n <= 1) {
            return 1;
        }
        else {
            return n * fact(n - 1);
        }
    }
    //endregion

    //region乘方符号处理函数
    /**
     * @param str   带^的字符串计算式
     * @return      返回将^处理到的字符串
     * note         此方法会将÷×替换为/*,此方法用到cc.calculateFormula(),用到BigDecimal
     */
    public String dealThePow(String str){
        //注:这些下标都是基于0开始的
        char[] chars;
        int thePositionOfPow;// ^ 位置
        int index;//char数组移动下标
        int theMostLeftPosition,theMostRightPosition;//最左和最右“当事符”下标,为截取字符串用
        String theLeftPart;// 符号 "^" 左边的数字或计算式,单纯数字或计算式
        String theRightPart;// 符号 "^" 右边的数字或计算式,单纯数字或计算式
        double theLeftPartResult;//theLeftPart的计算结果
        double theRightPartResult;//theRightPart的计算结果
        double theFinalResult;
        int theLeftBracketCount;
        int theRightBracketCount;

        while(str.contains("^")){//用while是因为有可能有多个 ^
            chars=str.toCharArray();//把计算公式str转换为char数组
            thePositionOfPow=0;
            index=0;
            theMostLeftPosition=theMostRightPosition=0;
            theLeftPart = "";
            theRightPart = "";
            theLeftPartResult=0;
            theRightPartResult=0;
            theFinalResult=0;
            theLeftBracketCount=0;
            theRightBracketCount=0;
            for (int i = 0; i < chars.length; i++) {//找到 ^ 的数组下标位置
                if (chars[i]=='^'){
                    thePositionOfPow=i;
                    break;
                }
            }
            index=thePositionOfPow-1;//此时index是 ^ 前一个位置

            //region左面不带括号的简单情况
            if(chars[index] != ')') {
                while (Character.isDigit(chars[index]) || chars[index] == '.') {//是数字和点都通过
                    theLeftPart += chars[index];
                    index--;
                    if(index==-1){
                        break;
                    }
                }
                //反转theLeftPart并完成左部分的存储
                char[] theLeftChars=theLeftPart.toCharArray();
                theLeftPart="";
                for (int i = theLeftChars.length-1; i >= 0; i--) {
                    theLeftPart+=""+theLeftChars[i];
                }

                theMostLeftPosition=index+1;
                index=thePositionOfPow+1;//此时index是 ^ 后一个位置,也就是 (
                for (int i = index; i < chars.length; i++) {
                    if (chars[i]=='('){
                        theLeftBracketCount++;
                    }else if(chars[i]==')'){
                        theLeftBracketCount--;
                    }
                    if (theLeftBracketCount==0){
                        theMostRightPosition=i;
                        break;
                    }
                }
                //完成右部分的存储
                for (int i = thePositionOfPow+1; i <= theMostRightPosition; i++) {
                    theRightPart+=""+chars[i];
                }

                //准备工作完成,开始手术
                theRightPart=theRightPart.replace('×','*');
                theRightPart=theRightPart.replace('÷','/');

                theRightPart=theRightPart.substring(1,theRightPart.length()-1);

                theRightPartResult = CalculatorUtil.calculate(theRightPart);

                theLeftPartResult=Double.parseDouble(theLeftPart);

                theFinalResult=Math.pow(theLeftPartResult,theRightPartResult);
                //防止出现E
                BigDecimal bg=new BigDecimal(theFinalResult);
                bg=bg.stripTrailingZeros();

                str="";//重新定义计算公式字符串
                //获取左面剩余字符串
                for (int i = 0; i < theMostLeftPosition; i++) {
                    str+=chars[i];
                }
                str+=bg.toPlainString();//拼接中间^的处理部分
                //获取右面剩余字符串
                for (int i = theMostRightPosition+1; i < chars.length; i++) {
                    str+=chars[i];
                }

            }
            //endregion
            //region 左面带括号稍复杂情况
            else if (chars[index] == ')'){
                for (int i = index; i >= 0; i--) {
                    if (chars[i]==')'){
                        theRightBracketCount++;
                    }else if(chars[i]=='('){
                        theRightBracketCount--;
                    }
                    if (theRightBracketCount==0){
                        theMostLeftPosition=i;
                        break;
                    }
                }
                //完成左面部分存储
                for (int i = theMostLeftPosition; i < thePositionOfPow; i++) {
                    theLeftPart+=""+chars[i];
                }

                index=thePositionOfPow+1;//此时index是 ^ 后一个位置,也就是 (
                for (int i = index; i < chars.length; i++) {
                    if (chars[i]=='('){
                        theLeftBracketCount++;
                    }else if(chars[i]==')'){
                        theLeftBracketCount--;
                    }
                    if (theLeftBracketCount==0){
                        theMostRightPosition=i;
                        break;
                    }
                }
                //完成右部分的存储
                for (int i = thePositionOfPow+1; i <= theMostRightPosition; i++) {
                    theRightPart+=""+chars[i];
                }
                //准备工作完成,开始手术
                theLeftPart=theLeftPart.replace('×','*');
                theLeftPart=theLeftPart.replace('÷','/');
                theRightPart=theRightPart.replace('×','*');
                theRightPart=theRightPart.replace('÷','/');

                theLeftPart=theLeftPart.substring(1,theLeftPart.length()-1);
                theRightPart=theRightPart.substring(1,theRightPart.length()-1);

                theLeftPartResult = CalculatorUtil.calculate(theLeftPart);
                theRightPartResult = CalculatorUtil.calculate(theRightPart);

                theFinalResult=Math.pow(theLeftPartResult,theRightPartResult);
                //防止出现E
                BigDecimal bg=new BigDecimal(theFinalResult);
                bg=bg.stripTrailingZeros();

                str="";//重新定义计算公式字符串
                //获取左面剩余字符串
                for (int i = 0; i < theMostLeftPosition; i++) {
                    str+=chars[i];
                }
                str+=bg.toPlainString();//拼接中间^的处理部分
                //获取右面剩余字符串
                for (int i = theMostRightPosition+1; i < chars.length; i++) {
                    str+=chars[i];
                }
            }
            //endregion

        }
        return str;
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == App.requestCode_openRecorder){
            switch (resultCode){
                case App.resultCode_CopyResult:{
                    String result = data.getStringExtra("result");
                    textView1.append(result);
                    textView2.setText("");
                    inputNumber = result;
                    isEqualPress = 0;
                    break;
                }
                case App.resultCode_CopyFormula:{
                    String formula = data.getStringExtra("formula");
                    textView1.append(formula);
                    textView2.setText("");
                    inputNumber = "";
                    isEqualPress = 0;
                    break;
                }
                case App.resultCode_CalResult:{
                    String calResult = data.getStringExtra("calResult");
                    textView1.append(calResult);//追加内容
                    textView2.setText("");
                    inputNumber = calResult;
                    isEqualPress = 0;
                    Toast.makeText(MainActivity.this,"总和为:"+calResult,Toast.LENGTH_LONG).show();
                    break;
                }
            }
            recorderListData = RecorderUtil.getRecorderListData();
        }
    }

    //回调函数
    public void setColor(View v){
        switch (v.getId()){
            case R.id.btn_color_now:{
                if (!isColorListOpen) {
                    StartSetColorAnim();

                    isColorListOpen = true;

                }else {
                    CloseSetColorAnim();

                    isColorListOpen = false;
                }
                break;
            }
            case R.id.btn_color_red:{
                btn_color_now.setBackgroundResource(R.drawable.set_color_red);
                CloseSetColorAnim();
                isColorListOpen = false;

                App.appColor = App.colorRed;
                //设置公式和结果颜色
                textView1.setTextColor(Color.parseColor(App.appColor));
                textView2.setTextColor(Color.parseColor(App.appColor));
                //设置状态栏颜色
                StatusBarCompat.setStatusBarColor(MainActivity.this, Color.parseColor(App.appColor), true);
                //设置标题栏颜色
                toolbar.setBackgroundColor(Color.parseColor(App.appColor));
                //设置等于号按钮颜色
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_red);
                //将用户颜色信息存起来
                sharedPreferences.setAppColor(App.appColor);
                break;
            }
            case R.id.btn_color_green:{
                btn_color_now.setBackgroundResource(R.drawable.set_color_green);
                CloseSetColorAnim();
                isColorListOpen = false;

                App.appColor = App.colorGreen;
                //设置公式和结果颜色
                textView1.setTextColor(Color.parseColor(App.appColor));
                textView2.setTextColor(Color.parseColor(App.appColor));
                //设置状态栏颜色
                StatusBarCompat.setStatusBarColor(MainActivity.this, Color.parseColor(App.appColor), true);
                //设置标题栏颜色
                toolbar.setBackgroundColor(Color.parseColor(App.appColor));
                //设置等于号按钮颜色
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_green);
                //将用户颜色信息存起来
                sharedPreferences.setAppColor(App.appColor);
                break;
            }
            case R.id.btn_color_blue:{
                btn_color_now.setBackgroundResource(R.drawable.set_color_blue);
                CloseSetColorAnim();
                isColorListOpen = false;

                App.appColor = App.colorBlue;
                //设置公式和结果颜色
                textView1.setTextColor(Color.parseColor(App.appColor));
                textView2.setTextColor(Color.parseColor(App.appColor));
                //设置状态栏颜色
                StatusBarCompat.setStatusBarColor(MainActivity.this, Color.parseColor(App.appColor), true);
                //设置标题栏颜色
                toolbar.setBackgroundColor(Color.parseColor(App.appColor));
                //设置等于号按钮颜色
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_blue);
                //将用户颜色信息存起来
                sharedPreferences.setAppColor(App.appColor);
                break;
            }
            case R.id.btn_color_pink:{
                btn_color_now.setBackgroundResource(R.drawable.set_color_pink);
                CloseSetColorAnim();
                isColorListOpen = false;

                App.appColor = App.colorPink;
                //设置公式和结果颜色
                textView1.setTextColor(Color.parseColor(App.appColor));
                textView2.setTextColor(Color.parseColor(App.appColor));
                //设置状态栏颜色
                StatusBarCompat.setStatusBarColor(MainActivity.this, Color.parseColor(App.appColor), true);
                //设置标题栏颜色
                toolbar.setBackgroundColor(Color.parseColor(App.appColor));
                //设置等于号按钮颜色
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_pink);
                //将用户颜色信息存起来
                sharedPreferences.setAppColor(App.appColor);
                break;
            }
            case R.id.btn_color_violet:{
                btn_color_now.setBackgroundResource(R.drawable.set_color_violet);
                CloseSetColorAnim();
                isColorListOpen = false;

                App.appColor = App.colorViolet;
                //设置公式和结果颜色
                textView1.setTextColor(Color.parseColor(App.appColor));
                textView2.setTextColor(Color.parseColor(App.appColor));
                //设置状态栏颜色
                StatusBarCompat.setStatusBarColor(MainActivity.this, Color.parseColor(App.appColor), true);
                //设置标题栏颜色
                toolbar.setBackgroundColor(Color.parseColor(App.appColor));
                //设置等于号按钮颜色
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_violet);
                //将用户颜色信息存起来
                sharedPreferences.setAppColor(App.appColor);
                break;
            }
            case R.id.btn_color_yellow:{
                btn_color_now.setBackgroundResource(R.drawable.set_color_yellow);
                CloseSetColorAnim();
                isColorListOpen = false;

                App.appColor = App.colorYellow;
                //设置公式和结果颜色
                textView1.setTextColor(Color.parseColor(App.appColor));
                textView2.setTextColor(Color.parseColor(App.appColor));
                //设置状态栏颜色
                StatusBarCompat.setStatusBarColor(MainActivity.this, Color.parseColor(App.appColor), true);
                //设置标题栏颜色
                toolbar.setBackgroundColor(Color.parseColor(App.appColor));
                //设置等于号按钮颜色
                btn_equal.setBackgroundResource(R.drawable.button_equal_selector_yellow);
                //将用户颜色信息存起来
                sharedPreferences.setAppColor(App.appColor);
                break;
            }
        }
    }

    public void StartSetColorAnim(){
        for (int i = 0; i < btnColorList.size(); i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(btnColorList.get(i),"translationY",btn_color_red.getMeasuredHeight()*(i+1));
            animator.setDuration(500);
            animator.setInterpolator(new OvershootInterpolator());
            animator.start();
        }
    }
    public void CloseSetColorAnim(){
        for (int i = 0; i < btnColorList.size(); i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(btnColorList.get(i),"translationY",-btn_color_red.getMeasuredHeight()*(i+1));
            animator.setDuration(500);
            animator.start();
        }
    }
    //初始化字体,不跟随系统字体大小
    private void initFontScale(){
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = 1f;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale*metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration,metrics);
    }


    public void changeIconRed() {
        enableComponent(componentName_red);
        disableComponent(componentName_green);
        disableComponent(componentName_blue);
        disableComponent(componentName_pink);
        disableComponent(componentName_violet);
        disableComponent(componentName_yellow);
    }
    public void changeIconGreen() {
        try {
            enableComponent(componentName_green);
            disableComponent(componentName_red);
            disableComponent(componentName_blue);
            disableComponent(componentName_pink);
            disableComponent(componentName_violet);
            disableComponent(componentName_yellow);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void changeIconBlue() {
        enableComponent(componentName_blue);
        disableComponent(componentName_red);
        disableComponent(componentName_green);
        disableComponent(componentName_pink);
        disableComponent(componentName_violet);
        disableComponent(componentName_yellow);
    }
    public void changeIconPink() {
        enableComponent(componentName_pink);
        disableComponent(componentName_red);
        disableComponent(componentName_blue);
        disableComponent(componentName_green);
        disableComponent(componentName_violet);
        disableComponent(componentName_yellow);
    }
    public void changeIconViolet() {
        enableComponent(componentName_violet);
        disableComponent(componentName_red);
        disableComponent(componentName_blue);
        disableComponent(componentName_green);
        disableComponent(componentName_pink);
        disableComponent(componentName_yellow);
    }
    public void changeIconYellow() {
        enableComponent(componentName_yellow);
        disableComponent(componentName_red);
        disableComponent(componentName_blue);
        disableComponent(componentName_pink);
        disableComponent(componentName_violet);
        disableComponent(componentName_green);
    }

    private void enableComponent(ComponentName componentName) {
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableComponent(ComponentName componentName) {
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
