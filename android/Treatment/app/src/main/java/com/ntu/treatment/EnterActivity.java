package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class EnterActivity extends AppCompatActivity implements View.OnClickListener {
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        Intent intent1=getIntent();
        userName=intent1.getStringExtra("userName");
        System.out.println(userName);

    }
    @Override
    public void onClick(View view){
        System.out.println(view.getId()==R.id.buttonToFriends);
        if(view.getId()==R.id.buttonToFriends){
            Log.d("EnterActivity", "userName: " + userName);
            Intent intent=new Intent();
            intent.putExtra("userName",userName);
            intent.setClass(EnterActivity.this, FriendSelectActivity.class);
            startActivity(intent);
        }else if(view.getId()==R.id.buttonToGroups){
            Intent intent=new Intent();
            intent.putExtra("userName",userName);
            intent.setClass(EnterActivity.this,GroupSelectActivity.class);
            startActivity(intent);
        }
    }
}