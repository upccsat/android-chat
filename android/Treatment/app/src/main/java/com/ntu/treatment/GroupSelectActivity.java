package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.ntu.treatment.adapter.GroupsAdapter;
import com.ntu.treatment.util.GetUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class GroupSelectActivity extends AppCompatActivity {

    private ListView listView;
    private String userName;
    private String url;
    private List<String> groupNames;
    private List<String> images;
    private List<String> owners;
    private List<Map<String,Object>> data;
    private Integer groupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);
        Intent intent1=getIntent();
        userName=intent1.getStringExtra("userName");
        url= GetUrl.url + "/getGroups";
        groupNames=new ArrayList<>();
        images=new ArrayList<>();
        owners=new ArrayList<>();
        data=new ArrayList<>();

        listView=findViewById(R.id.listView);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",userName );
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showGroupsList(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(GroupSelectActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showGroupsList(final String response){

        JSONObject jsonObject = JSON.parseObject(response);
        JSONArray groupsList=jsonObject.getJSONArray("groups_list");
        for(int i=0;i<groupsList.size();i++){
            JSONObject groupObject=groupsList.getJSONObject(i);
            String userName=groupObject.getString("userName");
            String image=groupObject.getString("image");
            String owner=groupObject.getString("owner");
            groupNames.add(userName);
            images.add(image);
            owners.add(owner);
        }
        for(int i=0;i<groupNames.size();i++){
            Map<String,Object> map=new HashMap<>();
            map.put("userName",groupNames.get(i));
            map.put("image",images.get(i));
            map.put("owner",owners.get(i));
            data.add(map);
        }
        updateUi();
    }
    private void updateUi(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] from={"userName","image","owner"};
                Integer[] to={R.id.textView1,R.id.imageView,R.id.textView2};
                GroupsAdapter friendsAdapter=new GroupsAdapter(GroupSelectActivity.this,data,R.layout.group_list_view,from,to);
                listView.setAdapter(friendsAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        View listItemView = adapterView.getChildAt(i);
                        TextView textView=listItemView.findViewById(R.id.textView1);
                        TextView textView1=listItemView.findViewById(R.id.textView2);
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("username",userName );
                        String url1=GetUrl.url + "/user/getGroupId";
                        client.post(url1, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                groupId=Integer.parseInt(new String(responseBody));
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                groupId=0;
                                Toast.makeText(GroupSelectActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
                            }
                        });

                        String toGroupName=textView.getText().toString();
                        Intent intent=new Intent();
                        intent.putExtra("userName",userName);
                        intent.putExtra("groupId",groupId.toString());
                        intent.setClass(GroupSelectActivity.this, ChatGroupActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}