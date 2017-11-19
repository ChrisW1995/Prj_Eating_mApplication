package com.example.chriswang.prj_eating2.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.chriswang.prj_eating2.model.Feedback;
import java.util.ArrayList;

/**
 * Created by ChrisWang on 2017/11/7.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackHolder> {

    private ArrayList<Feedback> mData;
    private Activity mActivity;

    public FeedbackAdapter(ArrayList<Feedback> mData, Activity mActivity) {
        this.mData = mData;
        this.mActivity = mActivity;
    }

    @Override
    public FeedbackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FeedbackHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class FeedbackHolder extends RecyclerView.ViewHolder{


        public FeedbackHolder(View itemView) {
            super(itemView);
        }
    }
}


