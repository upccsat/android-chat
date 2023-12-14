package com.ntu.treatment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntu.treatment.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupsAdapter extends BaseAdapter {
    private Context context;
    private List<? extends Map<String, ?>> dataList;
    private int resource;
    private String[] from;
    private Integer[] to;
    private ArrayList<Integer> num;

    public GroupsAdapter(Context context, List<? extends Map<String, ?>> data,
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

            if (convertView.findViewById(viewId) instanceof TextView) {
                TextView textView = convertView.findViewById(viewId);
                textView.setText(value != null ? value.toString() : "");
            } else if (convertView.findViewById(viewId) instanceof ImageView) {
                ImageView imageView = convertView.findViewById(viewId);
                imageView.setImageResource(R.drawable.group);
            }
        }

        return convertView;
    }

}
