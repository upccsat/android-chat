package com.ntu.treatment.util;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static String username;
    public static String ws; // websocket测试地址

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    public void setUsernameNameText(String name){
        username = name;
        ws = "ws://192.168.43.76:8080/websocket/"+username;
    }
}
