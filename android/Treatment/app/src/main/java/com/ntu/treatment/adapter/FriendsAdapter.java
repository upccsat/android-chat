package com.ntu.treatment.adapter;

import android.content.Context;
import android.content.Intent;
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

import com.ntu.treatment.R;

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
                imageView.setImageResource(R.drawable.contact_head_icon);
                //TODO 解决imageView资源的选取问题，暂用R.drawable.contact_head_icon代替

                // 在这里设置ImageView的图片资源或路径
//                if (value instanceof Integer) {
//                    imageView.setImageResource((Integer) value);
//                } else if (value instanceof String) {
//                    // 如果value是String类型，你可以使用加载图片的库，比如Glide或Picasso
//                    // Glide.with(context).load((String) value).into(imageView);
//                }
            }
        }
        return convertView;
    }

}