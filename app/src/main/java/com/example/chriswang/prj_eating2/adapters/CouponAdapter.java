package com.example.chriswang.prj_eating2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chriswang.prj_eating2.CouponActivity;
import com.example.chriswang.prj_eating2.CouponDetailActivity;
import com.example.chriswang.prj_eating2.MenuFragment;
import com.example.chriswang.prj_eating2.R;
import com.example.chriswang.prj_eating2.Request.GetNewCouponRequest;
import com.example.chriswang.prj_eating2.Service.CustomFunction;
import com.example.chriswang.prj_eating2.Service.FetchRestaurantCoupons;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;
import com.example.chriswang.prj_eating2.WaitingActivity;
import com.example.chriswang.prj_eating2.model.Coupon;

import java.util.ArrayList;

/**
 * Created by ChrisWang on 2017/12/20.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponHolder>{

    private ArrayList<Coupon> mData;
    private Context mContext;
    private Activity mActivity;
    private CustomFunction customFunction;
    private SharedPrefManager sharedPrefManager;
    public CouponAdapter(ArrayList<Coupon> mData, Activity mActivity, Context mContext) {
        this.mData = mData;
        this.mActivity = mActivity;
        this.mContext = mContext;
    }


    @Override
    public CouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_row,
                parent, false);
        return new CouponAdapter.CouponHolder(view);
    }

    @Override
    public void onBindViewHolder(final CouponHolder holder, int position) {
        final Coupon coupon = mData.get(position);
        holder.setTv_coupon_title(coupon.getTitle());
        holder.setTv_coupon_content(coupon.getDescription());
        final String time = String.format("%S - %S", coupon.getStartTime(), coupon.getEndTime());
        holder.setTv_coupon_datetime(time);
        holder.setBtn_get_coupon(coupon.isReceived());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, CouponDetailActivity.class);
                i.putExtra("title", coupon.getTitle());
                i.putExtra("content", coupon.getDescription());
                i.putExtra("time", time);
                mActivity.startActivity(i);
            }
        });
        holder.btn_get_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager = new SharedPrefManager(mContext);
                GetCoupon(coupon.getCouponId(), sharedPrefManager.getC_Id(), holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    public void GetCoupon(String couponId, String c_id,  final CouponHolder holder){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    holder.setBtn_get_coupon(true);
                    Toast.makeText(mActivity, "已收藏",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json;
                customFunction = new CustomFunction();
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            json = new String(response.data);
                            json = customFunction.trimMessage(json, "Message");
                            if(json != null) {
                                Toast.makeText(mActivity, json, Toast.LENGTH_SHORT).show();
                            }
                            break;

                    }
                    //Additional cases
                }
            }
        };
        GetNewCouponRequest couponRequest = new GetNewCouponRequest(couponId, c_id, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        queue.add(couponRequest);
    }

    public class CouponHolder extends RecyclerView.ViewHolder{

        private TextView tv_coupon_title, tv_coupon_content, tv_coupon_datetime;
        private Button btn_get_coupon;
        private LinearLayout linearLayout;

        public CouponHolder(View itemView) {
            super(itemView);
            tv_coupon_title = itemView.findViewById(R.id.tv_coupon_title);
            tv_coupon_content = itemView.findViewById(R.id.tv_coupon_content);
            tv_coupon_datetime = itemView.findViewById(R.id.tv_coupon_datetime);
            btn_get_coupon = itemView.findViewById(R.id.btn_get_coupon);
            linearLayout = itemView.findViewById(R.id.linear_coupon);

        }

        public void setBtn_get_coupon(boolean isReceived) {
            if(isReceived){
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.background_btn_gray);
                this.btn_get_coupon.setEnabled(false);
                this.btn_get_coupon.setBackground(drawable);
                this.btn_get_coupon.setText("已\n領\n取");
            }else {
                this.btn_get_coupon.setText("領\n取");
                this.btn_get_coupon.setEnabled(true);
            }

        }

        public void setTv_coupon_title(String tv_coupon_title) {

            this.tv_coupon_title.setText(tv_coupon_title);
        }

        public void setTv_coupon_content(String tv_coupon_content) {
            if(tv_coupon_content.equals("null"))
                tv_coupon_content="";

            this.tv_coupon_content.setText(tv_coupon_content);
        }

        public void setTv_coupon_datetime(String tv_coupon_datetime) {
            this.tv_coupon_datetime.setText(tv_coupon_datetime);
        }
    }
}
