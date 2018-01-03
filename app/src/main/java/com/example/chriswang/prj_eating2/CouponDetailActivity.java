package com.example.chriswang.prj_eating2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CouponDetailActivity extends AppCompatActivity {
    private TextView tv_detail_title, tv_detail_content,
    tv_detail_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        String title, content, time;
        title = getIntent().getStringExtra("title");
        content =getIntent().getStringExtra("content");
        time = getIntent().getStringExtra("time");

        if(content.equals("null"))
            content = "";

        tv_detail_title = findViewById(R.id.tv_coupon_detail_title);
        tv_detail_content =findViewById(R.id.tv_coupon_detail_content);
        tv_detail_time = findViewById(R.id.tv_coupon_detail_time);
        tv_detail_title.setText(title);
        tv_detail_content.setText(content);
        tv_detail_time.setText(time);
    }
}
