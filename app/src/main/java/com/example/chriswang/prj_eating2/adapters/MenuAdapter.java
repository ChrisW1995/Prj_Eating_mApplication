package com.example.chriswang.prj_eating2.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chriswang.prj_eating2.R;
import com.example.chriswang.prj_eating2.model.Feedback;
import com.example.chriswang.prj_eating2.model.Menu;

import java.util.ArrayList;

/**
 * Created by ChrisWang on 2017/12/17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {
    private ArrayList<Menu> mData;
    private Activity mActivity;


    public MenuAdapter(ArrayList<Menu> mData, Activity mActivity) {
        this.mData = mData;
        this.mActivity = mActivity;
    }

    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_grid,
                parent, false);
        return new MenuAdapter.MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuHolder holder, int position) {
        final Menu menu = mData.get(position);
        holder.setTv_menu_name(menu.getName());

        String path = menu.getPath();

        Glide.with(mActivity)
                .load(path)
                .into(holder.img_menu);

    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    public class MenuHolder extends RecyclerView.ViewHolder{
        private ImageView img_menu;
        private TextView tv_menu_name;

        public MenuHolder(View itemView) {
            super(itemView);

            img_menu = itemView.findViewById(R.id.img_menu);
            tv_menu_name = itemView.findViewById(R.id.tv_menu_name);
        }

        public void setTv_menu_name(String name) {
            this.tv_menu_name.setText(name);
        }
    }
}


