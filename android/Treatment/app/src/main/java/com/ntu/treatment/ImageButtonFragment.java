package com.ntu.treatment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class ImageButtonFragment extends Fragment implements View.OnClickListener {

    private ImageButton button1, button2, button3;
    private String userName;

    public ImageButtonFragment() {
        // Required empty public constructor
    }

    public static ImageButtonFragment newInstance(String data) {
        ImageButtonFragment fragment = new ImageButtonFragment();
        Bundle args = new Bundle();
        args.putString("userName", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            userName = args.getString("userName");
            // 在这里使用获取到的数据
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.image_button_fragment_blank, container, false);

        button1 = view.findViewById(R.id.imageButton1);
        button2 = view.findViewById(R.id.imageButton2);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton1:
                Intent intent=new Intent();
                intent.putExtra("userName",userName);
                intent.setClass(getContext(), FriendSelectActivity.class);
                startActivity(intent);
                // 处理按钮1的点击事件
                break;
            case R.id.imageButton2:
                Intent intent1=new Intent();
                intent1.setClass(getContext(), HomeActivity.class);
                intent1.putExtra("userName",userName);
                startActivity(intent1);
                // 处理按钮2的点击事件

                break;

        }
    }

    private void handleButtonClick(ImageButton button) {
        // 在这里执行按钮点击后的逻辑，例如改变按钮颜色等
        // 示例：将按钮变为红色
        button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
    }
}