package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.adapter.FriendInvitationAdapter;
import com.ntu.treatment.util.GetUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ViewInvitationActivity extends AppCompatActivity  {
    private ListView listView;
    private String userName;
    private String url;
    private List<String> fromUserNames;
    private List<Integer> isReceiveds;
    private List<String> images;
    private List<Map<String,Object>> data;
    private Button addFriendButton;
    private Button viewInvitationButton;
    private Button findFriendButton;
    private EditText findFriend;
//    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // 在这里执行刷新操作
//            if (intent != null && intent.getAction() != null &&
//                    intent.getAction().equals(FriendInvitationAdapter.REFRESH_ACTION)) {
//                // 执行刷新操作
//                refreshActivity();
//            }
//        }
//    };
//    public void refreshActivity(){
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invitation);
        listView=findViewById(R.id.listView);
        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        fromUserNames=new ArrayList<>();
        images=new ArrayList<>();
        isReceiveds=new ArrayList<>();
        data=new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userName",userName );
        url= GetUrl.url+"/user/getAllFriendInvitation";

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("ViewInvitationActivity",new String(responseBody));
                updateUi(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ViewInvitationActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void updateUi(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                JSONObject jsonObject = JSON.parseObject(response);
                JSONArray friendsList=jsonObject.getJSONArray("friend_invitation_list");
                Log.e("ViewActivity",friendsList.toString());
                for(int i=0;i<friendsList.size();i++){
                    JSONObject friendObject=friendsList.getJSONObject(i);
                    if(friendObject!=null){
                        String userName=friendObject.getString("fromUserName");
                        Integer isReceived=friendObject.getInteger("isReceived");
                        fromUserNames.add(userName);
                        isReceiveds.add(isReceived);
                    }
                }
                for(int i=0;i<fromUserNames.size();i++){
                    Map<String,Object> map=new HashMap<>();
                    map.put("fromUserName",fromUserNames.get(i));
                    data.add(map);
                }
                String[] from={"fromUserName"};
                Integer[] to={R.id.textView};
                FriendInvitationAdapter friendInvitationAdapter=new FriendInvitationAdapter(ViewInvitationActivity.this,
                        data,
                        R.layout.friendinvitation_list_view,
                        from,
                        to,
                        userName,
                        isReceiveds
                );
                listView.setAdapter(friendInvitationAdapter);

            }
        });
    }
}