package com.ntu.treatment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.FriendSelectActivity;
import com.ntu.treatment.R;
import com.ntu.treatment.util.GetUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class FriendInvitationAdapter extends BaseAdapter {
    private Context context;
    private List<? extends Map<String, ?>> dataList;
    private int resource;
    private String[] from;
    private Integer[] to;
    private String userName;
    private List<Integer> isReceiveds;
    private String url;


    public static final String REFRESH_ACTION = "com.example.chatupc.REFRESH_ACTION";

    public FriendInvitationAdapter(Context context, List<? extends Map<String, ?>> data,
                         int resource, String[] from, Integer[] to,String userName,List<Integer> isReceiveds) {
        this.context = context;
        this.dataList = data;
        this.resource = resource;
        this.from = from;
        this.to = to;
        this.userName=userName;
        this.isReceiveds=isReceiveds;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }
        Map<String, ?> dataItem = dataList.get(position);

        for (int i = 0; i < from.length; i++) {
            String key = from[i];
            int viewId = to[i];

            Object value = dataItem.get(key);

            // 在这里将数据显示在界面上，例如使用 findViewById 找到视图并设置数据
            if (convertView.findViewById(viewId) instanceof TextView) {
                TextView textView = convertView.findViewById(viewId);
                textView.setText(value != null ? value.toString() : "");
            } else if (convertView.findViewById(viewId) instanceof ImageView) {
                ImageView imageView = convertView.findViewById(viewId);
                imageView.setImageResource(R.drawable.contact_head_icon);
            }
        }
        Button btnAgree=convertView.findViewById(R.id.btnAgree);
        if(isReceiveds.get(position)==1){
            btnAgree.setVisibility(View.INVISIBLE);
        }
        TextView textView=convertView.findViewById(R.id.textView);
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               url= GetUrl.url+"/user/changeFriendInvitationStatus";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("fromUserName", textView.getText().toString());
                params.put("toUserName",userName);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println(new String(responseBody));
                        String response=new String(responseBody);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(new String(responseBody));
                                String response = new String(responseBody);
                                if ("true".equals(response)) {
                                    btnAgree.setVisibility(View.INVISIBLE);
                                    sendRefreshBroadcast(context);
                                } else if ("false".equals(response)) {
                                    Toast.makeText(context, "通过失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context.getApplicationContext(), "Post请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return convertView;
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(REFRESH_ACTION);
        context.sendBroadcast(intent);
    }

}
