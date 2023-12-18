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
    private List<Map<String, Object>> data;
    private Integer groupId;
    private Button addGroupButton;
    private Button toFriendsSelect;
    private Button btnFindGroup;
    private EditText etFindGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);



        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("userName");
        url = GetUrl.url + "/user/getGroups";
        groupNames = new ArrayList<>();
        images = new ArrayList<>();
        owners = new ArrayList<>();
        data = new ArrayList<>();

        ImageButtonFragment fragment = ImageButtonFragment.newInstance(userName);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        addGroupButton = findViewById(R.id.addGroupButton);
        toFriendsSelect=findViewById(R.id.toFriendsSelect);
        btnFindGroup=findViewById(R.id.findGroupButton);
        etFindGroup=findViewById(R.id.findGroup);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("userName", userName);
                intent.setClass(GroupSelectActivity.this, AddGroupActivity.class);
                startActivity(intent);
            }
        });
        toFriendsSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("userName",userName);
                intent.setClass(GroupSelectActivity.this, FriendSelectActivity.class);
                startActivity(intent);
            }
        });
        btnFindGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                String findGroupText=etFindGroup.getText().toString();
                for(int i=0;i<groupNames.size();i++){
                    if(groupNames.get(i).contains(findGroupText)){
                        Map<String,Object> map=new HashMap<>();
                        map.put("groupName",groupNames.get(i));
                        map.put("image",images.get(i));
                        map.put("owner",owners.get(i));
                        data.add(map);
                    }
                }
                updateUi();
            }
        });
        listView = findViewById(R.id.listView);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", userName);
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

    private void showGroupsList(final String response) {

        JSONObject jsonObject = JSON.parseObject(response);
        JSONArray groupsList = jsonObject.getJSONArray("groups_list");

        if(groupsList!=null){
            for (int i = 0; i < groupsList.size(); i++) {
                JSONObject groupObject = groupsList.getJSONObject(i);
                String image = groupObject.getString("image");
                String owner = groupObject.getString("owner");
                String groupName = groupObject.getString("groupName");
                groupNames.add(groupName);
                images.add(image);
                owners.add(owner);
            }
            for (int i = 0; i < groupNames.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("groupName", groupNames.get(i));
                map.put("image", images.get(i));
                map.put("owner", owners.get(i));
                data.add(map);
            }
        }
        updateUi();
    }

    private void updateUi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] from = {"groupName", "image", "owner"};
                Integer[] to = {R.id.textView1, R.id.imageView, R.id.textView2};
                GroupsAdapter groupsAdapter = new GroupsAdapter(GroupSelectActivity.this, data, R.layout.group_list_view, from, to);
                listView.setAdapter(groupsAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        View listItemView = adapterView.getChildAt(i);
                        TextView textView1=listItemView.findViewById(R.id.textView1);
                        TextView textView2=listItemView.findViewById(R.id.textView2);

                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("groupName", textView1.getText().toString());
                        params.put("owner", textView2.getText().toString());
                        url=GetUrl.url+"/user/getGroupId";
                        client.post(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String response=new String(responseBody);
                                groupId=Integer.parseInt(response);
                                Intent intent=new Intent();
                                intent.putExtra("userName",userName);
                                intent.putExtra("groupId",groupId.toString());
                                intent.putExtra("groupName",textView1.getText().toString());
                                intent.setClass(GroupSelectActivity.this,ChatGroupActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(GroupSelectActivity.this, "getGroupId:Post请求失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
}
