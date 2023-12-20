package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.ntu.treatment.adapter.FriendsCheckBoxAdapter;
import com.ntu.treatment.util.GetUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SelectFriendsActivity extends AppCompatActivity {
    private ListView listView;
    private String userName;
    private String url;
    private List<String> userNames;
    private List<String> images;
    private List<Map<String,Object>> data;
    private Button btnConfirm;
    private FriendsCheckBoxAdapter friendsCheckBoxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friends);

        Intent intent1=getIntent();
        userName=intent1.getStringExtra("userName");
        url= GetUrl.url + "/user/getFriends";
        btnConfirm=findViewById(R.id.btnConfirm);
        userNames=new ArrayList<>();
        images=new ArrayList<>();
        data=new ArrayList<>();

        ImageButtonFragment fragment = ImageButtonFragment.newInstance(userName);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        listView=findViewById(R.id.listView);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putStringArrayListExtra("userNamesToInvite", (ArrayList<String>) friendsCheckBoxAdapter.getCheckedUserNames());
                setResult(2,intent);
                finish();
            }
        });

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
                Toast.makeText(SelectFriendsActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showFriendsList(final String response){

        JSONObject jsonObject = JSON.parseObject(response);
        JSONArray friendsList=jsonObject.getJSONArray("friends_list");
        for(int i=0;i<friendsList.size();i++){
            JSONObject friendObject=friendsList.getJSONObject(i);
            String userName=friendObject.getString("userName");
            userNames.add(userName);
            images.add(userName);
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
                friendsCheckBoxAdapter =new FriendsCheckBoxAdapter(SelectFriendsActivity.this,data,R.layout.friends_check_box_list_view,from,to);
                listView.setAdapter(friendsCheckBoxAdapter);

            }
        });
    }
}