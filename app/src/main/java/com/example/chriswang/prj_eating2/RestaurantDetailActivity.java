package com.example.chriswang.prj_eating2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView tv_Name, tv_Address, tv_Phone,tv_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        tv_Name = findViewById(R.id.tv_DetailName);
        tv_Address = findViewById(R.id.tv_DetailAddr);
        tv_Phone = findViewById(R.id.tv_DetailPhone);

         tv_Name.setText(getIntent().getStringExtra("name"));
        tv_Address.setText("餐廳地址："+ getIntent().getStringExtra("address"));
        tv_Phone.setText("聯絡電話："+ getIntent().getStringExtra("phone"));

    }
}
