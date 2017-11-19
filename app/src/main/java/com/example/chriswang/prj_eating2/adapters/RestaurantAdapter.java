package com.example.chriswang.prj_eating2.adapters;

import android.app.Activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chriswang.prj_eating2.R;
import com.example.chriswang.prj_eating2.RestaurantDetailActivity;
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

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RestaurantDetailActivity.class);
                intent.putExtra("r_id", restaurant.getR_id());
                intent.putExtra("name", restaurant.getR_Name());
                intent.putExtra("phone", restaurant.getR_Phone());
                intent.putExtra("address", restaurant.getR_Address());
                intent.putExtra("imgPath", restaurant.getR_imgPath());

                mActivity.startActivity(intent);
            }
        });

        Glide.with(mActivity)
                .load(restaurant.getR_imgPath())
                .into(holder.img_Restaurant);

    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    public class RestaurantHolder extends RecyclerView.ViewHolder{

        TextView tvR_Name, tvR_Address, tvR_Phone, tvDistance;
        ImageView img_Restaurant;
        private LinearLayout linearLayout;
        public RestaurantHolder(View itemView) {
            super(itemView);

            tvR_Name = itemView.findViewById(R.id.tvR_Name);
            tvR_Address = itemView.findViewById(R.id.tvR_Address);
            tvR_Phone = itemView.findViewById(R.id.tvR_Phone);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            linearLayout = itemView.findViewById(R.id.restaurant_list_constraint);
            img_Restaurant = itemView.findViewById(R.id.img_Restaurant);

        }

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
