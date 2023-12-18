package com.llw.changeavatar;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private FragmentOne firstFragment = null;// 用于显示第一个界面
    private FragmentTwo secondFragment = null;// 用于显示第二个界面
    private FragmentThree thirdFragment = null;// 用于显示第三个界面
    private FragmentFour fourthFragment = null;// 用于显示第四个界面

    private View firstLayout = null;// 第一个显示布局
    private View secondLayout = null;// 第二个显示布局
    private View thirdLayout = null;// 第三个显示布局
    private View fourthLayout = null;// 第四个显示布局

    private ImageView message_image = null;
    private ImageView news_image = null;
    private ImageView contacts_image = null;
    private ImageView setting_image = null;

    private TextView message_text = null;
    private TextView news_text = null;
    private TextView contacts_text = null;
    private TextView setting_text = null;

    private FragmentManager fragmentManager = null;// 用于对Fragment进行管理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//要求窗口没有title
        super.setContentView(R.layout.activity_main);
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();//用于对Fragment进行管理
        // 设置默认的显示界面
        setTabSelection(0);


    }

    /**
     * 在这里面获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件
     */
    @SuppressLint("NewApi")
    public void initViews() {
        fragmentManager = getFragmentManager();
        firstLayout = findViewById(R.id.message_layout);
        secondLayout = findViewById(R.id.news_layout);
        thirdLayout = findViewById(R.id.contacts_layout);
        fourthLayout = findViewById(R.id.setting_layout);

        message_image = (ImageView) findViewById(R.id.message_image);
        news_image = (ImageView) findViewById(R.id.news_image);
        contacts_image = (ImageView) findViewById(R.id.contacts_image);
        setting_image = (ImageView) findViewById(R.id.setting_image);

        message_text = (TextView) findViewById(R.id.message_text);
        news_text = (TextView) findViewById(R.id.news_text);
        contacts_text = (TextView) findViewById(R.id.contacts_text);
        setting_text = (TextView) findViewById(R.id.setting_text);

        //处理点击事件
        firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);
        thirdLayout.setOnClickListener(this);
        fourthLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_layout:
                setTabSelection(0);// 当点击了我的主页tab时，选中第1个tab
                break;
            case R.id.news_layout:
                setTabSelection(1);// 当点击了购物车tab时，选中第2个tab
                break;
            case R.id.contacts_layout:
                setTabSelection(2);// 当点击了我的订单tab时，选中第3个tab
                break;
            case R.id.setting_layout:
                setTabSelection(3);// 当点击了个人中心tab时，选中第4个tab
                break;
            default:
                break;
        }

    }

    /**
     * 根据传入的index参数来设置选中的tab页 每个tab页对应的下标。0表示主页，1表示支出，2表示收入，3表示设置
     */
    @SuppressLint("NewApi")
    private void setTabSelection(int index) {
        clearSelection();// 每次选中之前先清除掉上次的选中状态
        FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启一个Fragment事务
        hideFragments(transaction);// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        switch (index) {
            case 0:
                // 当点击了我的主页tab时改变控件的图片和文字颜色
                message_image.setImageResource(R.drawable.fa_1);//修改布局中的图片
                message_text.setTextColor(Color.parseColor("#0090ff"));//修改字体颜色

                if (firstFragment == null) {
                    // 如果FirstFragment为空，则创建一个并添加到界面上
                    firstFragment = new FragmentOne();
                    transaction.add(R.id.content, firstFragment);

                } else {
                    // 如果FirstFragment不为空，则直接将它显示出来

                    transaction.show(firstFragment);//显示的动作
                }

                break;
            // 以下和firstFragment类同
            case 1:
                news_image.setImageResource(R.drawable.fc_1);
                news_text.setTextColor(Color.parseColor("#0090ff"));
                if (secondFragment == null) {
                    secondFragment = new FragmentTwo();


                    transaction.add(R.id.content, secondFragment);
                } else {
                    transaction.show(secondFragment);
                }
                break;
            case 2:
                contacts_image.setImageResource(R.drawable.fb_1);
                contacts_text.setTextColor(Color.parseColor("#0090ff"));
                if (thirdFragment == null) {
                    thirdFragment = new FragmentThree();
                    transaction.add(R.id.content, thirdFragment);
                } else {
                    transaction.show(thirdFragment);
                }
                break;
            case 3:
                setting_image.setImageResource(R.drawable.fd_1);
                setting_text.setTextColor(Color.parseColor("#0090ff"));
                if (fourthFragment == null) {
                    fourthFragment = new FragmentFour();
                    transaction.add(R.id.content, fourthFragment);
                } else {

                    transaction.show(fourthFragment);
                }

                break;
        }
        transaction.commit();

    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {
        message_image.setImageResource(R.drawable.fa);
        message_text.setTextColor(Color.parseColor("#82858b"));

        contacts_image.setImageResource(R.drawable.fb);
        contacts_text.setTextColor(Color.parseColor("#82858b"));

        news_image.setImageResource(R.drawable.fc);
        news_text.setTextColor(Color.parseColor("#82858b"));

        setting_image.setImageResource(R.drawable.fd);
        setting_text.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都设置为隐藏状态 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (firstFragment != null) {
            transaction.hide(firstFragment);
        }
        if (secondFragment != null) {
            transaction.hide(secondFragment);
        }
        if (thirdFragment != null) {
            transaction.hide(thirdFragment);
        }
        if (fourthFragment != null) {
            transaction.hide(fourthFragment);
        }
    }

    //封装一个AlertDialog
    private void exitDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("您确定要退出程序吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).create();
        dialog.show();//显示对话框
    }

    /**
     * 返回菜单键监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果是返回按钮
            exitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }



}