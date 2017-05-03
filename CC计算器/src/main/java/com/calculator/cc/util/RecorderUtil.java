package com.calculator.cc.util;

import android.util.Log;
import com.calculator.cc.application.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by cong on 20170121.
 */

public class RecorderUtil {

    public static void saveRecorderListData(ArrayList<String> recorderListData){
        try {
            File file = new File(App.getConText().getFilesDir(),"recorderListData.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(recorderListData);

            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getRecorderListData() {
        try {
            File file = new File(App.getConText().getFilesDir(), "recorderListData.txt");

            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<String> recorderListData = (ArrayList<String>) objectInputStream.readObject();

            fileInputStream.close();

            return recorderListData;

        } catch (Exception e) {
            //异常说明recorderListData.txt中没有recorderListData对象,
            //所以可能是第一次启动App,new ArrayList对象并返回
            return new ArrayList<>();
        }

    }
}
