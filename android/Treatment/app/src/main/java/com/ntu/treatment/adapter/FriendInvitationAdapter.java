package com.ntu.treatment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private String url;

    public FriendInvitationAdapter(Context context, List<? extends Map<String, ?>> data,
                         int resource, String[] from, Integer[] to,String userName) {
        this.context = context;
        this.dataList = data;
        this.resource = resource;
        this.from = from;
        this.to = to;
        this.userName=userName;
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
            TextView textView = convertView.findViewById(viewId);
            if (textView != null) {
                textView.setText(value != null ? value.toString() : "");
            }
        }
        Button btnAgree=convertView.findViewById(R.id.btnAgree);
        TextView textView=convertView.findViewById(R.id.textView);
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               url= GetUrl.url+"/changeFriendInvitationStatus";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("fromUserName", textView.getText().toString());
                params.put("toUserName",userName);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                       //TODO 完成ui的更新以及friend数据库的更新
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

}
