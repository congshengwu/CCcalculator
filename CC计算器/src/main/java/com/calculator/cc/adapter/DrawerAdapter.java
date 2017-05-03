package com.calculator.cc.adapter;

import android.content.Context;
import android.media.SoundPool;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.calculator.cc.activity.MainActivity;
import com.calculator.cc.MySharedPreferences;
import com.calculator.cc.R;
import com.calculator.cc.bean.DrawerItemBean;

import java.util.List;

/**
 * Created by cong on 20170121.
 */

public class DrawerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<DrawerItemBean> drawerList;
    private int type;
    private MySharedPreferences sharedPreferences;
    private SoundPool sp;
    private int sound;
    public DrawerAdapter(Context context, List<DrawerItemBean> drawerList, SoundPool sp, int sound){
        inflater = LayoutInflater.from(context);
        this.drawerList = drawerList;
        sharedPreferences = new MySharedPreferences(context);
        this.sp = sp;
        this.sound = sound;
    }

    @Override
    public int getCount() {
        return drawerList.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1;
        View view2;
        type = getItemViewType(position);

        if (type == 1){
            ViewHolder1 viewHolder1 = null;
            if (convertView == null){
                viewHolder1 = new ViewHolder1();
                view1 = inflater.inflate(R.layout.draweritem1,null);
                viewHolder1.textView_drawer1 = (TextView) view1.findViewById(R.id.textView_drawer1);
                view1.setTag(viewHolder1);
                convertView = view1;

            }else {
                viewHolder1 = (ViewHolder1) convertView.getTag();
            }
            DrawerItemBean bean = drawerList.get(position);
            viewHolder1.textView_drawer1.setText(bean.getDrawerItemText());
        }
        else if (type == 2){
            ViewHolder2 viewHolder2 = null;
            if (convertView == null){

                viewHolder2 = new ViewHolder2();
                view2 = inflater.inflate(R.layout.draweritem2,null);
                viewHolder2.textView_drawer2 = (TextView) view2.findViewById(R.id.textView_drawer2);
                viewHolder2.switchCompat = (SwitchCompat) view2.findViewById(R.id.switchCompat_drawer2);
                final ViewHolder2 finalViewHolder = viewHolder2;
                viewHolder2.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            finalViewHolder.textView_drawer2.setText("按键音效开");
                            sharedPreferences.setButtonSound(true);
                            MainActivity.buttonSound = true;
                            sp.play(sound,1,1,0,0,1);
                        }else {
                            finalViewHolder.textView_drawer2.setText("按键音效关");
                            sharedPreferences.setButtonSound(false);
                            MainActivity.buttonSound = false;
                        }
                    }
                });

                view2.setTag(viewHolder2);
                convertView = view2;
            }else {
                viewHolder2 = (ViewHolder2) convertView.getTag();
            }
            //设置switch前文字,开或关
            DrawerItemBean bean = drawerList.get(position);
            viewHolder2.textView_drawer2.setText(bean.getDrawerItemText());
            //设置switch默认check状态
            if (MainActivity.buttonSound){
                viewHolder2.switchCompat.setChecked(true);
            }else {
                viewHolder2.switchCompat.setChecked(false);
            }
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >=0 && position <=2){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return drawerList.size();
    }
    private class ViewHolder1{
        private TextView textView_drawer1;
    }
    private class ViewHolder2{
        private TextView textView_drawer2;
        private SwitchCompat switchCompat;
    }

}
