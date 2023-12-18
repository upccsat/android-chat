package com.ntu.treatment;

import static org.apache.commons.httpclient.util.DateUtil.formatDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.util.GetUrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ModifyUserInfoActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Button btnSubmit;
    private String userName;
    private String birthday;
    private String phonenum;
    private String email;
    private Date date;
    private EditText etPhonenum;
    private EditText etEmail;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        datePicker=findViewById(R.id.datePicker);
        btnSubmit=findViewById(R.id.btnSubmit);
        etPhonenum=findViewById(R.id.editTextPhoneNumber);
        etEmail=findViewById(R.id.editTextEmail);
        date=null;
        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        birthday=intent.getStringExtra("birthday");
        phonenum=intent.getStringExtra("phonenum");
        email=intent.getStringExtra("email");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // 将字符串日期解析为 Date 对象
            if(birthday!=null){
                date = dateFormat.parse(birthday);
                // 在此处可以使用 date 对象进行进一步处理
                System.out.println("转换后的 Date 对象: " + date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date!=null){
            updateDatePicker(date);
        }else{
            Date date1=new Date();
            updateDatePicker(date1);
        }
        if(phonenum!=null)etPhonenum.setText(phonenum);
        if(email!=null)etEmail.setText(email);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                // 创建Calendar对象并设置选定的日期
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                Date selectedDate = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                // 将Date对象转换为字符串
                String dateString = dateFormat.format(selectedDate);
                url= GetUrl.url+ "/user/updateUserInfo";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("username", userName);
                params.put("birthday",dateString);
                params.put("phonenum",etPhonenum.getText().toString());
                params.put("emailAddress",etEmail.getText().toString());
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        submit(new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(ModifyUserInfoActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void updateDatePicker(Date newDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.updateDate(year, month, day);
    }
    private void submit(String response){
        if(response.equals("true")){
            Toast.makeText(ModifyUserInfoActivity.this,"更新成功，即将返回个人主页",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.setClass(ModifyUserInfoActivity.this, HomeActivity.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(ModifyUserInfoActivity.this,"更新失败，请重试",Toast.LENGTH_SHORT).show();
        }
    }
}