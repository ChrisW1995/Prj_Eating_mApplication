package com.example.chriswang.prj_eating2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView tv_Name, tv_Address, tv_Phone,tv_Time;
    private String r_id;
    private ImageView imgDetailImg;
    private Button btnReserve, btnWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        r_id = getIntent().getStringExtra("r_id");
        tv_Name = findViewById(R.id.tv_DetailName);
        tv_Address = findViewById(R.id.tv_DetailAddr);
        tv_Phone = findViewById(R.id.tv_DetailPhone);
        imgDetailImg = findViewById(R.id.img_DetailImg);
        btnReserve = findViewById(R.id.btnReserve);
        btnWaiting = findViewById(R.id.btnWaiting);

        tv_Name.setText(getIntent().getStringExtra("name"));
        tv_Address.setText("餐廳地址："+ getIntent().getStringExtra("address"));
        tv_Phone.setText("聯絡電話："+ getIntent().getStringExtra("phone"));

        Glide.with(this)
                .load(getIntent().getStringExtra("imgPath"))
                .into(imgDetailImg);

        btnWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RestaurantDetailActivity.this, WaitingActivity.class);
                i.putExtra("r_id", r_id);
                startActivity(i);
            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
