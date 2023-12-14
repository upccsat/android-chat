package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.adapter.FriendsAdapter;
import com.ntu.treatment.util.GetUrl;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import cz.msebera.android.httpclient.Header;

public class AddGroupActivity extends AppCompatActivity {
    private Button btnCreateGroup;
    private Button btnSelectFriend;
    private EditText etCreateGroup;
    private String userName;
    private String groupName;
    private String url;
    private ListView listView;
    private List<Map<String,Object>> data1;
    private ArrayList<String> userNamesToInvite;
    private Boolean createGroupAlready;
    private Integer groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        groupId=0;
        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        btnCreateGroup=findViewById(R.id.btnAddGroup);

        etCreateGroup=findViewById(R.id.etGroupName);
        btnSelectFriend=findViewById(R.id.btnSelectFriend);
        listView=findViewById(R.id.listView);
        data1=new ArrayList<>();
        userNamesToInvite=new ArrayList<>();
        userNamesToInvite.add(userName);
        btnSelectFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent1=new Intent();
                intent1.setClass(AddGroupActivity.this,SelectFriendsActivity.class);
                intent1.putExtra("userName",userName);
                startActivityForResult(intent1,1);
            }
        });

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //client用于创建群聊，client1用于获取groupId，client2用于邀请好友
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("owner", userName);
                params.put("groupName", etCreateGroup.getText().toString());
                url = GetUrl.url + "/user/createGroup";

                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);
                        if (response.equals("true")) {
                            createGroupAlready = true;
                            Log.e("AddGroupActivity", createGroupAlready.toString());
                            Log.e("AddGroupActivity", "1");
                            if (createGroupAlready) {
                                getGroupId(); // 调用获取 GroupId 的方法
                            }
                        } else {
                            createGroupAlready = false;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(AddGroupActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void getGroupId() {
        AsyncHttpClient client1 = new AsyncHttpClient();
        RequestParams params1 = new RequestParams();
        params1.put("owner", userName);
        params1.put("groupName", etCreateGroup.getText().toString());
        url = GetUrl.url+"/user/getGroupId";

        client1.post(url, params1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                groupId = Integer.parseInt(response);
                if (groupId != 0) {
                    System.out.println("groupId:"+groupId);
                    userNamesToInvite.add(userName);
                    userNamesToInvite.add(groupId.toString());
                    JSONArray jsonArray = new JSONArray(userNamesToInvite);
                    RequestParams params2 = new RequestParams();
                    params2.put("usernames", jsonArray.toString());
                    AsyncHttpClient client2 = new AsyncHttpClient();
                    url = GetUrl.url+"/user/addGroupMember";

                    client2.post(url, params2, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String response = new String(responseBody);
                            if (response.equals("true")) {
                                Toast.makeText(AddGroupActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent();
                                intent1.setClass(AddGroupActivity.this, GroupSelectActivity.class);
                                startActivity(intent1);
                                finish();
                            } else {
                                Toast.makeText(AddGroupActivity.this, "创建失败，请重试", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(AddGroupActivity.this, "addGroupMember:Post请求失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(AddGroupActivity.this, "getGroupId:Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1&&resultCode == 2) {
            if(data!=null){
                userNamesToInvite=data.getStringArrayListExtra("userNamesToInvite");
                if(!userNamesToInvite.isEmpty()){
                    for(String i:userNamesToInvite){
                        Map<String,Object> map=new HashMap<>();
                        //TODO 需要改一下image
                        map.put("userName",i);
                        Log.e("AddGroupActivity","userName:"+i);
                        map.put("image","");
                        data1.add(map);
                    }
                    String[] from={"userName","image"};
                    Integer[] to={R.id.textView,R.id.imageView};
                    FriendsAdapter friendsAdapter=new FriendsAdapter(
                            AddGroupActivity.this,
                            data1,
                            R.layout.friend_list_view,
                            from,
                            to);
                    listView.setAdapter(friendsAdapter);

                }
            }
        }else{
            Toast.makeText(AddGroupActivity.this,"回传失败",Toast.LENGTH_SHORT).show();
        }
    }
}