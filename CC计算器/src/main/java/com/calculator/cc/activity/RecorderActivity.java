package com.calculator.cc.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.calculator.cc.R;
import com.calculator.cc.application.App;
import com.calculator.cc.util.RecorderUtil;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cong on 20170121.
 * recorderListData数据反向在此Activity处理
 */

/**
 * 关于recorderListData的逻辑（以下简称data）
 *      1.data传来时会马上进行数据反转
 *      2.交互界面：粘贴结果(1),粘贴公式(2),求和(3),返回按钮(4),返回键(5),删除该行(6),清空(7),右滑屏幕返回(8)
 *        点击1、2、3、4、5、8会产生界面跳转,返回just系列结果码,进行不带data更新操作
 *        点击6、7时会使变量isNeedUpdate=true,此时界面跳转会返回非just系列结果码,进行带有data更新操作,此时回传时要反转一下data
 *
 */

public class RecorderActivity extends Activity {
    private Button btn_cls2;
    private Button btn_equal2;
    private Button btn_back;
    private TextView textView_null;
    private ListView recorderListView;
    private Intent backData;//回传数据
    private ArrayList<String> recorderListData;
    private ArrayAdapter<String> adapter;

    private TextView textView_formulaNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化默认字体大小,不跟随系统
        initFontScale();
        setContentView(R.layout.activity_recorder);
        //设置标题栏颜色
        StatusBarCompat.setStatusBarColor(RecorderActivity.this, Color.parseColor(App.appColor), true);

        textView_formulaNumber = (TextView) findViewById(R.id.textView_formulaNumber);
        textView_null = (TextView) findViewById(R.id.textView_null);
        recorderListData = RecorderUtil.getRecorderListData();
        Collections.reverse(recorderListData);//因为要倒序显示
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,recorderListData);
        recorderListView= (ListView) findViewById(R.id.recorderListView);
        recorderListView.setAdapter(adapter);

        textView_formulaNumber.setText(recorderListData.size()+"/30");

        //adapter不为空,有加载动画;否则显示“空”
        if (!adapter.isEmpty()) {
            textView_null.setVisibility(View.GONE);
            LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.load_recorder_list));
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            recorderListView.setLayoutAnimation(lac);
            recorderListView.startLayoutAnimation();
        }else {
            textView_null.setVisibility(View.VISIBLE);
        }

        backData = new Intent();

        btn_cls2 = (Button) findViewById(R.id.btn_cls2);
        btn_equal2 = (Button) findViewById(R.id.btn_equal2);
        btn_back = (Button) findViewById(R.id.btn_back);
        //region 设置按钮颜色
        switch (App.appColor){
            case "#ffff4444": {
                btn_cls2.setBackgroundResource(R.drawable.set_color_red);
                btn_equal2.setBackgroundResource(R.drawable.set_color_red);
                btn_back.setBackgroundResource(R.drawable.set_color_red);
                break;
            }
            case "#3CB371": {
                btn_cls2.setBackgroundResource(R.drawable.set_color_green);
                btn_equal2.setBackgroundResource(R.drawable.set_color_green);
                btn_back.setBackgroundResource(R.drawable.set_color_green);
                break;
            }
            case "#00BFFF": {
                btn_cls2.setBackgroundResource(R.drawable.set_color_blue);
                btn_equal2.setBackgroundResource(R.drawable.set_color_blue);
                btn_back.setBackgroundResource(R.drawable.set_color_blue);
                break;
            }
            case "#FF69B4": {
                btn_cls2.setBackgroundResource(R.drawable.set_color_pink);
                btn_equal2.setBackgroundResource(R.drawable.set_color_pink);
                btn_back.setBackgroundResource(R.drawable.set_color_pink);
                break;
            }
            case "#EE82EE": {
                btn_cls2.setBackgroundResource(R.drawable.set_color_violet);
                btn_equal2.setBackgroundResource(R.drawable.set_color_violet);
                btn_back.setBackgroundResource(R.drawable.set_color_violet);
                break;
            }
            case "#FFA500": {
                btn_cls2.setBackgroundResource(R.drawable.set_color_yellow);
                btn_equal2.setBackgroundResource(R.drawable.set_color_yellow);
                btn_back.setBackgroundResource(R.drawable.set_color_yellow);
                break;
            }
        }
        //endregion

        recorderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (recorderListView.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
                    showPopupMenu(view, position);
                }else {
                    if (recorderListView.isItemChecked(position)){
                        view.setBackgroundColor(Color.parseColor(App.appColor));
                    }else {
                        view.setBackgroundColor(Color.parseColor("#f7f7f7"));
                    }
                    //回到单选模式
                    if (getListCheckedItemCount() == 0){
                        recorderListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
                        recorderListView.clearChoices();
                        btn_cls2.setText("清空");
                        btn_equal2.setText("求和");
                        btn_back.setText("返回");
                    }
                }
            }
        });

        recorderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //进入多选模式
                if (recorderListView.getChoiceMode() == ListView.CHOICE_MODE_NONE){
                    recorderListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    recorderListView.clearChoices();
                    recorderListView.setItemChecked(position,true);
                    view.setBackgroundColor(Color.parseColor(App.appColor));
                    btn_cls2.setText("选中删除");
                    btn_equal2.setText("选中求和");
                    btn_back.setText("取消");
                }
                return true;//false不拦截,true拦截
            }
        });

        btn_cls2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当内容为空时,就直接返回
                if (adapter.isEmpty()){
                    return;
                }
                //内容不为空,弹出防误触对话框
                if (recorderListView.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
                    showDialog(adapter);
                }else {
                    //加载并开启删除一行动画
                    Animation animation = AnimationUtils.loadAnimation(RecorderActivity.this,R.anim.del_one_recoorderlist);
                    //删除选中计算记录数据
                    int n = 0;
                    for (int id:getListCheckedItemIds()) {
                        id-=n;
                        recorderListView.getChildAt(id).startAnimation(animation);
                        recorderListData.remove(id);
                        n++;
                    }
                    //重新设置适配器即可
                    recorderListView.setAdapter(adapter);
                    //回到单选模式
                    recorderListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    recorderListView.clearChoices();
                    btn_cls2.setText("清空");
                    btn_equal2.setText("求和");
                    btn_back.setText("返回");

                    textView_formulaNumber.setText(recorderListData.size()+"/30");
                    Collections.reverse(recorderListData);//存数据要正序存储
                    RecorderUtil.saveRecorderListData(recorderListData);
                    Collections.reverse(recorderListData);//因为还要显示,所以再倒序过来
                    if (adapter.isEmpty()){
                        textView_null.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        btn_equal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果adapter为空就不求总和
                if (adapter.isEmpty()){
                    return;
                }
                String[] strs;
                if (recorderListView.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
                    strs = recorderListData.toArray(new String[recorderListData.size()]);
                }else {
                    strs = new String[getListCheckedItemCount()];
                    int n = 0;
                    for (int id:getListCheckedItemIds()){
                        strs[n] = recorderListData.get(id);
                        n++;
                    }
                }
                double calResult=0;
                for (String str:strs){
                    int thePositionOfEqual = str.indexOf("=");
                    String temp=str.substring(thePositionOfEqual+1,str.length());
                    calResult += Double.parseDouble(temp);
                }
                String strCalResult = "";
                if (calResult == (int)calResult){
                    strCalResult = ((int)calResult) + "";
                }else {
                    strCalResult = calResult + "";
                }
                backData.putExtra("calResult",strCalResult);
                setResult(App.resultCode_CalResult,backData);

                finish();
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recorderListView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE){
                    for (int id:getListCheckedItemIds()){
                        recorderListView.getChildAt(id).setBackgroundColor(Color.parseColor("#f7f7f7"));
                    }
                    recorderListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    recorderListView.clearChoices();
                    btn_cls2.setText("清空");
                    btn_equal2.setText("求和");
                    btn_back.setText("返回");
                    return;
                }
                finish();
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
            }
        });
    }

    private int[] getListCheckedItemIds(){
        final int[] ids = new int[recorderListView.getCount()];
        int checkedCount = 0;
        for(int i = 0;i < recorderListView.getCount(); i++) {
            if (recorderListView.isItemChecked(i)) {
                ids[checkedCount++] = i;
            }
        }
        if (checkedCount == recorderListView.getCount()) {
            return ids;
        } else {
            final int[] result = new int[checkedCount];
            System.arraycopy(ids, 0, result, 0, checkedCount);
            return result;
        }
    }
    private int getListCheckedItemCount(){
        final int[] ids = new int[recorderListView.getCount()];
        int checkedCount = 0;
        for(int i = 0;i < recorderListView.getCount(); i++) {
            if (recorderListView.isItemChecked(i)) {
                ids[checkedCount++] = i;
            }
        }
        if (checkedCount == recorderListView.getCount()) {
            return ids.length;
        } else {
            final int[] result = new int[checkedCount];
            System.arraycopy(ids, 0, result, 0, checkedCount);
            return result.length;
        }
    }

    //重写返回按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (recorderListView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE){
            for (int id:getListCheckedItemIds()){
                recorderListView.getChildAt(id).setBackgroundColor(Color.parseColor("#f7f7f7"));
            }
            recorderListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
            recorderListView.clearChoices();
            btn_cls2.setText("清空");
            btn_equal2.setText("求和");
            btn_back.setText("返回");
            return true;
        }
        finish();
        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
        return super.onKeyDown(keyCode, event);
    }

    private float x1=0,x2=0,y1=0,y2=0;

    //重写滑动方法
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
                //手指向右滑动关闭当前Activtiy
                if(x2-x1>200 && (y2-y1<200 && y2-y1>-200)){
                    finish();
                    overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    //下拉菜单
    private void showPopupMenu(final View view, final int position){
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);

        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_recorder, popupMenu.getMenu());

        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_result:{
                        String str=recorderListData.get(position);
                        int thePostionOfEqual=str.indexOf("=");//返回值是以0开始的
                        String result=str.substring(thePostionOfEqual+1,str.length());
                        backData.putExtra("result",result);
                        setResult(App.resultCode_CopyResult,backData);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                        break;
                    }
                    case R.id.action_formula:{
                        String str=recorderListData.get(position);
                        int thePostionOfEqual=str.indexOf("=");//返回值是以0开始的
                        String formula=str.substring(0,thePostionOfEqual);
                        backData.putExtra("formula",formula);
                        setResult(App.resultCode_CopyFormula,backData);

                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                        break;
                    }
                    case R.id.action_delete:{
                        //加载并开启删除一行动画
                        Animation animation = AnimationUtils.loadAnimation(RecorderActivity.this,R.anim.del_one_recoorderlist);
                        view.startAnimation(animation);
                        //删除数据源对应的一行
                        recorderListData.remove(position);
                        //重新设置适配器即可
                        recorderListView.setAdapter(adapter);


                        textView_formulaNumber.setText(recorderListData.size()+"/30");
                        Collections.reverse(recorderListData);//存数据要正序存储
                        RecorderUtil.saveRecorderListData(recorderListData);
                        Collections.reverse(recorderListData);//因为还要显示,所以再倒序过来
                        if (adapter.isEmpty()){
                            textView_null.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
                return false;
            }
        });
        try{
            popupMenu.show();
        }catch (Exception e){
            e.printStackTrace();//未知异常
        }
    }

    //提示对话框
    private void showDialog(final ArrayAdapter<String> adapter){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("确定清空?");
        //builder.setMessage("提示内容");//设置内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //适配器清空的话,listView显示的数据也就清空了,即recorderListData数据是空的
                adapter.clear();
                textView_formulaNumber.setText("0/30");
                textView_null.setVisibility(View.VISIBLE);
                Collections.reverse(recorderListData);//存数据要正序存储
                RecorderUtil.saveRecorderListData(recorderListData);
                Collections.reverse(recorderListData);//因为还要显示,所以再倒序过来
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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

}
