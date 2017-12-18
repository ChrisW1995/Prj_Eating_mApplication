package com.example.chriswang.prj_eating2.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.chriswang.prj_eating2.R;
import com.example.chriswang.prj_eating2.model.Feedback;
import com.example.chriswang.prj_eating2.model.Restaurant;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_row,
                parent, false);
        return new FeedbackAdapter.FeedbackHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackHolder holder, int position) {
        final Feedback feedback = mData.get(position);
        holder.setTv_feedback_comment(feedback.getComment());
        holder.setTv_feedback_time(feedback.getCommentTime());
        holder.setTv_feedback_name(feedback.getC_Name());
        holder.setFeedback_ratingbar(feedback.getRateNum());
        holder.setTv_feedback_title(feedback.getTitle());
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }


    public class FeedbackHolder extends RecyclerView.ViewHolder{
        private TextView tv_feedback_name, tv_feedback_comment, tv_feedback_time, tv_feedback_title;
        private RatingBar feedback_ratingbar;
        private LinearLayout linearLayout;

        public FeedbackHolder(View itemView) {
            super(itemView);
            tv_feedback_name = itemView.findViewById(R.id.tv_feedback_name);
            tv_feedback_comment = itemView.findViewById(R.id.tv_feedback_comment);
            tv_feedback_time = itemView.findViewById(R.id.tv_feedback_time);
            feedback_ratingbar = itemView.findViewById(R.id.feedback_ratingBar);
            tv_feedback_title = itemView.findViewById(R.id.tv_feedback_title);
        }

        public void setTv_feedback_title(String title) {
            this.tv_feedback_title.setText(title);
        }

        public void setTv_feedback_name(String tv_feedback_name) {
            this.tv_feedback_name.setText(tv_feedback_name);
        }

        public void setTv_feedback_comment(String tv_feedback_comment) {
            this.tv_feedback_comment.setText(tv_feedback_comment);
        }

        public void setTv_feedback_time(String tv_feedback_time) {
            this.tv_feedback_time.setText(tv_feedback_time);
        }

        public void setFeedback_ratingbar(float rating) {
            this.feedback_ratingbar.setRating(rating);
        }
    }
}


