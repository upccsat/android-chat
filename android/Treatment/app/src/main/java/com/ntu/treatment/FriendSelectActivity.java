package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.adapter.FriendsAdapter;
import com.ntu.treatment.util.GetUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class FriendSelectActivity extends AppCompatActivity {
    private ListView listView;
    private String userName;
    private String url;
    private List<String> userNames;
    private List<String> images;
    private List<Map<String,Object>> data;
    private Button addFriendButton;
    private Button viewInvitationButton;
    private Button findFriendButton;
    private EditText findFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_select);

        Intent intent1=getIntent();
        userName=intent1.getStringExtra("userName");
        url= GetUrl.url + "/user/getFriends";
        addFriendButton=findViewById(R.id.addFriendButton);
        viewInvitationButton=findViewById(R.id.viewInvitationButton);
        findFriendButton=findViewById(R.id.findFriendButton);
        findFriend=findViewById(R.id.findFriend);
        userNames=new ArrayList<>();
        images=new ArrayList<>();
        data=new ArrayList<>();

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("userName",userName);
                intent.setClass(FriendSelectActivity.this,AddFriendActivity.class);
                startActivity(intent);
            }
        });
        viewInvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("userName",userName);
                intent.setClass(FriendSelectActivity.this,ViewInvitationActivity.class);
                startActivity(intent);
            }
        });
        listView=findViewById(R.id.listView);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",userName );
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showFriendsList(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(FriendSelectActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showFriendsList(final String response){

        JSONObject jsonObject = JSON.parseObject(response);
        JSONArray friendsList=jsonObject.getJSONArray("friends_list");
        for(int i=0;i<friendsList.size();i++){
            JSONObject friendObject=friendsList.getJSONObject(i);
            String userName=friendObject.getString("userName");
            String image=friendObject.getString("image");
            userNames.add(userName);
            images.add(image);
        }
        for(int i=0;i<userNames.size();i++){
            Map<String,Object> map=new HashMap<>();
            map.put("userName",userNames.get(i));
            map.put("image",images.get(i));
            data.add(map);
        }
        updateUi();
    }
    private void updateUi(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] from={"userName","image"};
                Integer[] to={R.id.textView,R.id.imageView};
               FriendsAdapter friendsAdapter=new FriendsAdapter(FriendSelectActivity.this,data,R.layout.friend_list_view,from,to);
               listView.setAdapter(friendsAdapter);
               listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                   @Override
                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       View listItemView = adapterView.getChildAt(i);
                       TextView textView=listItemView.findViewById(R.id.textView);
                       String toUserName=textView.getText().toString();
                       Intent intent=new Intent();
                       intent.putExtra("userName",userName);
                       intent.putExtra("toUserName",toUserName);
                       intent.setClass(FriendSelectActivity.this, ChatFriendActivity.class);
                       startActivity(intent);
                   }
               });
            }
        });
    }
}