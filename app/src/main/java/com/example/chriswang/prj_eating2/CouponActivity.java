package com.example.chriswang.prj_eating2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chriswang.prj_eating2.Request.AddWaitingRequest;
import com.example.chriswang.prj_eating2.Request.GetNewCouponRequest;
import com.example.chriswang.prj_eating2.Service.CustomFunction;
import com.example.chriswang.prj_eating2.Service.FetchRestaurantCoupons;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;
import com.example.chriswang.prj_eating2.adapters.CouponAdapter;
import com.example.chriswang.prj_eating2.adapters.FeedbackAdapter;
import com.example.chriswang.prj_eating2.model.Coupon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CouponActivity extends AppCompatActivity {
    private String r_id;
    private FetchRestaurantCoupons restaurantCoupons;
    private ArrayList<Coupon> mCoupons;
    private CouponAdapter mCouponAdapter;
    private RecyclerView mCouponRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        r_id = getIntent().getStringExtra("r_id");
        mCoupons = new ArrayList<>();
        mCouponAdapter = new CouponAdapter(mCoupons, this, getApplicationContext());
        mCouponRecycler = findViewById(R.id.r_coupon_recycler);
        restaurantCoupons = new FetchRestaurantCoupons(getApplicationContext(), r_id, this);
        Object[] objects = new Object[3];
        objects[0] = mCoupons;
        objects[1]= mCouponAdapter;
        objects[2] = mCouponRecycler;
        restaurantCoupons.execute(objects);


    }


}
