package com.llw.changeavatar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.llw.changeavatar.utils.SPUtils;

public class FragmentFour extends Fragment {

    RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

    private ShapeableImageView ivHead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fourthLayout = inflater.inflate(R.layout.fragment_fragment_four, container, false);

        ImageView imageView = fourthLayout.findViewById(R.id.imageView);
//        ImageView imageView = fourthLayout.findViewById(R.id.iv_head);

        // 设置点击事件监听器
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 处理点击事件，跳转到ChangeAvatar界面
                navigateToChangeAvatar();
            }
        });


//        ivHead = fourthLayout.findViewById(R.id.iv_head);
//        ChangeAvatar changeAvatar = new ChangeAvatar();
//
//        String imageUrl = SPUtils.getString("imageUrl", null, changeAvatar);
//        if (imageUrl != null) {
//            Glide.with(changeAvatar).load(imageUrl).apply(requestOptions).into(ivHead);
//        }

        TextView textView = fourthLayout.findViewById(R.id.textView);
        TextView textView2 = fourthLayout.findViewById(R.id.textView2);
        TextView textView6 = fourthLayout.findViewById(R.id.textView6);
        TextView textView7 = fourthLayout.findViewById(R.id.textView7);
        TextView textView8 = fourthLayout.findViewById(R.id.textView8);
        TextView textView9 = fourthLayout.findViewById(R.id.textView9);
        // 从后台获取的文本
        String nickName = "昵称";
        String userName = "用户名";
        String gender = "性别";
        String age = "年龄";
        String hobby = "爱好";
        String phoneNum = "电话";

        // 将从后台获取的文本设置为TextView的内容
        textView.setText(nickName);
        textView2.setText("用户名：" + userName);
        textView6.setText("性别：" + gender);
        textView7.setText("年龄：" + age);
        textView8.setText("爱好：" + hobby);
        textView9.setText("电话：" + phoneNum);
        return fourthLayout;
    }

    private void navigateToChangeAvatar() {
        Intent intent = new Intent(getActivity(), ChangeAvatar.class);
        startActivity(intent);
    }
}


