package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import com.loopj.android.http.*;
import com.ntu.treatment.util.GetUrl;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mContextSp;
    private Context mContext;
    private RelativeLayout rl_user;
    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mAccount;
    private EditText mPassword;
    private HashMap<String, String> stringHashMap;

    String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        rl_user = (RelativeLayout) findViewById(R.id.rl_user);
        mLoginButton = (Button) findViewById(R.id.login);
        mRegisterButton = (Button) findViewById(R.id.register);
        mAccount = findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);

        stringHashMap = new HashMap<>();
        mLoginButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login: {
                System.out.println("这是登录666");
                String url = GetUrl.url + "/user/login";
                Doctor_Android_Async_Http_Post(url);
            }
            case R.id.register: {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            }
        }
        System.out.println("这是登录" + mAccount.getText().toString());

    }

    //Post请求
    private void Doctor_Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", mAccount.getText().toString());
        params.put("password", mPassword.getText().toString());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                doctor_showResponse(new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doctor_showResponse(final String response){

        //Activity不允许在子线程中进行UI操作
        //通过该方法可以将线程切换到主线程，然后再更新UI元素
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tv.setText(response);*/

                System.out.println(response);
                if (response.equals("true")){
                    System.out.println("哈哈哈哈哈哈哈");
                    /**
                     * 传到ChatActivity
                     */
                    Intent intent = new Intent();
                    intent.putExtra("username",mAccount.getText().toString());
                    intent.setClass(MainActivity.this,EnterActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}