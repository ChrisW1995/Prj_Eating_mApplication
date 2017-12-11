package com.example.chriswang.prj_eating2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.google.android.gms.actions.ReserveIntents;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView tv_Name, tv_Address, tv_Phone,tv_Time;
    private String r_id;
    private ImageView imgDetailImg;
    private Button btnReserve, btnWaiting;
    private boolean waitSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        r_id = getIntent().getStringExtra("r_id");
        tv_Name = findViewById(R.id.tv_DetailName);
        tv_Address = findViewById(R.id.tv_DetailAddr);
        tv_Phone = findViewById(R.id.tv_DetailPhone);
        tv_Time = findViewById(R.id.tv_R_Time);
        imgDetailImg = findViewById(R.id.img_DetailImg);
        btnReserve = findViewById(R.id.btnReserve);
        btnWaiting = findViewById(R.id.btnWaiting);


        waitSwitch = getIntent().getBooleanExtra("waitSwitch", false);
        tv_Name.setText(getIntent().getStringExtra("name"));
        tv_Address.setText("餐廳地址："+ getIntent().getStringExtra("address"));
        tv_Phone.setText("聯絡電話："+ getIntent().getStringExtra("phone"));
        tv_Time.setText(String.format("營業時間：%s - %s",
                getIntent().getStringExtra("openTime"),
                getIntent().getStringExtra("closeTime")));

        Glide.with(this)
                .load(getIntent().getStringExtra("imgPath"))
                .into(imgDetailImg);

        if(waitSwitch){
            btnWaiting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(RestaurantDetailActivity.this, WaitingActivity.class);
                    i.putExtra("r_id", r_id);
                    startActivity(i);
                }
            });
        }else {

            Drawable drawable = this.getResources().getDrawable(R.drawable.background_btn_gray);
            btnWaiting.setEnabled(false);
            btnWaiting.setBackground(drawable);
        }



        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailActivity.this, ReserveActivity.class);
                intent.putExtra("r_id", r_id);
                intent.putExtra("r_name",getIntent().getStringExtra("name"));
                startActivity(intent);
            }
        });

    }
}
