package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.im.JWebSocketClientService;
import com.ntu.treatment.util.GetUrl;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity {

    private String userName;
    private Button btnModifyInfo;
    private Button btnLogout;
    private Button btnChangeAvatar;
    private TextView textViewName;
    private TextView textViewAge;
    private TextView textViewPhoneNumber;
    private TextView textViewEmail;
    private TextView textViewCreationTime;
    private ImageView ivAvatar;
    private String url;
    private String birthday;
    private String phonenum;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnModifyInfo=findViewById(R.id.btnModifyInfo);
        btnLogout=findViewById(R.id.btnLogout);
        btnChangeAvatar=findViewById(R.id.btnChangeAvatar);
        textViewName=findViewById(R.id.textViewName);
        textViewAge=findViewById(R.id.textViewAge);
        textViewPhoneNumber=findViewById(R.id.textViewPhoneNumber);
        textViewEmail=findViewById(R.id.textViewEmail);
        textViewCreationTime=findViewById(R.id.textViewCreationTime);
        ivAvatar=findViewById(R.id.imageViewCenter);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        ImageButtonFragment fragment = ImageButtonFragment.newInstance(userName);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        btnModifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent();
                intent1.setClass(HomeActivity.this, ModifyUserInfoActivity.class);
                intent1.putExtra("userName",userName);
                intent1.putExtra("birthday",birthday);
                intent1.putExtra("phonenum",phonenum);
                intent1.putExtra("email",email);
                startActivity(intent1);
                finish();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bindIntent = new Intent(HomeActivity.this, JWebSocketClientService.class);
                stopService(bindIntent);
                Intent intent2=new Intent();
                intent2.setClass(HomeActivity.this, MainActivity.class);
                startActivity(intent2);
                finish();
            }
        });
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent();
                intent3.setClass(HomeActivity.this, ChangeAvatarActivity.class);
                intent3.putExtra("userName",userName);
                startActivity(intent3);
            }
        });
        url= GetUrl.url+ "/user/getUserInfo";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userName", userName);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showInfo(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(HomeActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
        //请求头像
        AsyncHttpClient client1 = new AsyncHttpClient();
        RequestParams params1 = new RequestParams();
        params1.put("userName", userName);

        // 替换为你的服务器地址和接口路径
        String imageUrl = GetUrl.url+"/images/getImage";

        client1.post(imageUrl, params1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    // 在这里处理服务器返回的图片字节数组
                    handleImageResponse(responseBody);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //用户未更改头像时服务器返回404
            }
        });
    }
    private void handleImageResponse(byte[] imageBytes) {
        // 将字节数组设置给 ImageView 或进行其他处理
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivAvatar.setImageBitmap(bitmap);
            }
        });
    }
    public void showInfo(String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSON.parseObject(response);
                textViewName.setText(userName);
                birthday=jsonObject.getString("birthday");
                textViewAge.setText(birthday);
                phonenum=jsonObject.getString("phonenum");
                textViewPhoneNumber.setText(phonenum);
                email=jsonObject.getString("email");
                textViewEmail.setText(email);
                textViewCreationTime.setText(jsonObject.getString("createTime"));
            }
        });
    }
}