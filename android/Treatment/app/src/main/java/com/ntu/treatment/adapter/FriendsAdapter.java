package com.ntu.treatment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ntu.treatment.R;
import com.ntu.treatment.util.GetUrl;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private List<? extends Map<String, ?>> dataList;
    private int resource;
    private String[] from;
    private Integer[] to;
    private String imageUrl;


    public FriendsAdapter(Context context, List<? extends Map<String, ?>> data,
                         int resource, String[] from, Integer[] to) {
        this.context = context;
        this.dataList = data;
        this.resource = resource;
        this.from = from;
        this.to = to;

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
//                imageView.setImageResource(R.drawable.contact_head_icon);
                imageUrl= GetUrl.url+"/images/"+value;
                Glide.with(context)
                        .load(imageUrl)  // 图片的 URL
                        .error(R.drawable.contact_head_icon)  // 加载失败时显示的图（可选）
                        .into(imageView);  // 需要更新的 ImageView
            }
        }
        return convertView;
    }
}