package com.ntu.treatment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ntu.treatment.R;
import com.ntu.treatment.util.GetUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendsCheckBoxAdapter extends BaseAdapter {
    private Context context;
    private List<? extends Map<String, ?>> dataList;
    private int resource;
    private String[] from;
    private Integer[] to;
    private  List<String> checkedUserNames;
    private CompoundButton.OnCheckedChangeListener checkBoxListener;
    private String imageUrl;

    public List<String> getCheckedUserNames() {
        return checkedUserNames;
    }

    public FriendsCheckBoxAdapter(Context context, List<? extends Map<String, ?>> data,
                                  int resource, String[] from, Integer[] to) {
        this.context = context;
        this.dataList = data;
        this.resource = resource;
        this.from = from;
        this.to = to;

        checkedUserNames=new ArrayList<>();
        this.checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 获取 CheckBox 所在的位置
                String userName = (String) buttonView.getTag();

                // 在这里处理 checkedPositions 的更新
                if (isChecked) {
                    checkedUserNames.add(userName);
                    System.out.println(123);
                } else {
                    checkedUserNames.remove(userName);
                }
            }
        };
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

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);


        // 将位置信息设置为 tag，方便在监听器中获取位置


        checkBox.setOnCheckedChangeListener(null);
        checkBox.setOnCheckedChangeListener(checkBoxListener);
        Map<String, ?> dataItem = dataList.get(position);

        for (int i = 0; i < from.length; i++) {
            String key = from[i];
            int viewId = to[i];

            Object value = dataItem.get(key);

            // 在这里将数据显示在界面上，例如使用 findViewById 找到视图并设置数据
            if (convertView.findViewById(viewId) instanceof TextView) {
                TextView textView = convertView.findViewById(viewId);
                textView.setText(value != null ? value.toString() : "");
                checkBox.setTag(textView.getText().toString());
            } else if (convertView.findViewById(viewId) instanceof ImageView) {
                ImageView imageView = convertView.findViewById(viewId);
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
