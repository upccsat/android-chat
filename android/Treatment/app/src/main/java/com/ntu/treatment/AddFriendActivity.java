package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.adapter.FriendsAdapter;
import com.ntu.treatment.util.GetUrl;

import cz.msebera.android.httpclient.Header;

public class AddFriendActivity extends AppCompatActivity {

    private Button btnAddFriend;
    private EditText etAddFriend;
    private String userName;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);


        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        btnAddFriend=findViewById(R.id.btnAddFriend);
        etAddFriend=findViewById(R.id.etFriendName);

        ImageButtonFragment fragment = ImageButtonFragment.newInstance(userName);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("fromUserName",userName );
                params.put("toUserName",etAddFriend.getText().toString());
                url= GetUrl.url+"/user/addFriendInvitation";

                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        updateUi(new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(AddFriendActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private void updateUi(String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               if(response.equals("true")){
                   Toast.makeText(AddFriendActivity.this,"添加成功，即将返回好友列表页面,请等待确认",Toast.LENGTH_SHORT);
                   Intent intent=new Intent();
                   intent.putExtra("userName",userName);
                   intent.setClass(AddFriendActivity.this,FriendSelectActivity.class);
                   startActivity(intent);
                   finish();
               }else{
                   Toast.makeText(AddFriendActivity.this,"添加失败，请重试",Toast.LENGTH_SHORT);
               }
            }
        });
    }
}