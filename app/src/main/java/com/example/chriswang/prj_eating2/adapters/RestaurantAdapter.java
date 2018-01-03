package com.example.chriswang.prj_eating2.adapters;

import android.app.Activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chriswang.prj_eating2.R;
import com.example.chriswang.prj_eating2.RestaurantInfoActivity;
import com.example.chriswang.prj_eating2.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by ChrisWang on 2017/11/1.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder>{

    private ArrayList<Restaurant> mData;
    private Activity mActivity;


    public RestaurantAdapter(ArrayList<Restaurant> data, Activity activity) {
        this.mData = data;
        this.mActivity = activity;
    }

    @Override
    public RestaurantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list,
                parent, false);
        return new RestaurantHolder(view);

    }

    @Override
    public void onBindViewHolder(RestaurantHolder holder, int position) {
        final Restaurant restaurant = mData.get(position);
        holder.setTvR_Name(restaurant.getR_Name());
        holder.setTvR_Address("地址：" + restaurant.getR_Address());
        holder.setTvR_Phone("電話："+ restaurant.getR_Phone());
        holder.setTvDistnce(String.valueOf(restaurant.getDistance()));
        holder.setRating(restaurant.getScore());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RestaurantInfoActivity.class);
                intent.putExtra("r_id", restaurant.getR_id());
                intent.putExtra("name", restaurant.getR_Name());
                intent.putExtra("phone", restaurant.getR_Phone());
                intent.putExtra("address", restaurant.getR_Address());
                intent.putExtra("imgPath", restaurant.getR_imgPath());
                intent.putExtra("waitSwitch", restaurant.isWait_status());
                intent.putExtra("coupon", restaurant.isExist_coupon());
                intent.putExtra("openTime", restaurant.getR_OpenTime());
                intent.putExtra("closeTime", restaurant.getR_CloseTime());
                intent.putExtra("score", restaurant.getScore());
                intent.putExtra("waitNum", restaurant.getWaitNum());
                mActivity.startActivity(intent);
            }
        });

        String path = restaurant.getR_imgPath();

        Glide.with(mActivity)
                .load(path)
                .into(holder.img_Restaurant);

        if(restaurant.isWait_status()){
            holder.setImg_wait(R.mipmap.iswaiting_blue);
        }else {
            holder.setImg_wait(R.mipmap.no_waiting);
        }
        if(restaurant.isExist_coupon()){
            holder.setImg_sale(R.mipmap.is_sale_orange);
        }else {
            holder.setImg_sale(R.mipmap.no_sale);
        }



    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    public class RestaurantHolder extends RecyclerView.ViewHolder{

        TextView tvR_Name, tvR_Address, tvR_Phone, tvDistance;
        ImageView img_Restaurant, img_wait, img_sale;
        RatingBar restaurant_rating;

        private LinearLayout linearLayout;
        public RestaurantHolder(View itemView) {
            super(itemView);

            tvR_Name = itemView.findViewById(R.id.tvR_Name);
            tvR_Address = itemView.findViewById(R.id.tvR_Address);
            tvR_Phone = itemView.findViewById(R.id.tvR_Phone);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            linearLayout = itemView.findViewById(R.id.restaurant_list_constraint);
            img_Restaurant = itemView.findViewById(R.id.img_Restaurant);
            img_wait = itemView.findViewById(R.id.img_wait);
            img_sale = itemView.findViewById(R.id.img_sale);
            restaurant_rating = itemView.findViewById(R.id.restaurant_ratingBar);

        }
        public void setImg_sale(int img_sale) {
            this.img_sale.setImageResource(img_sale);
        }

        public void setImg_wait(int img_wait) {
            this.img_wait.setImageResource(img_wait);
        }

        public void setRating(float rating){this.restaurant_rating.setRating(rating);}

        public void setTvDistnce(String tvDistnce){this.tvDistance.setText(tvDistnce+"km");}

        public void setTvR_Name(String tvR_Name) {
            this.tvR_Name.setText(tvR_Name);
        }

        public void setTvR_Address(String tvR_Address) {
            this.tvR_Address.setText(tvR_Address);
        }

        public void setTvR_Phone(String tvR_Phone) {
            this.tvR_Phone.setText(tvR_Phone);
        }
    }
}
