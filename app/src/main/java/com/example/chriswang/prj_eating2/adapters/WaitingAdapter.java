package com.example.chriswang.prj_eating2.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chriswang.prj_eating2.R;
import com.example.chriswang.prj_eating2.Request.GetNewCouponRequest;
import com.example.chriswang.prj_eating2.Request.WaittingCancelRequest;
import com.example.chriswang.prj_eating2.Service.CustomFunction;
import com.example.chriswang.prj_eating2.model.Menu;
import com.example.chriswang.prj_eating2.model.WaitList;

import java.util.ArrayList;

/**
 * Created by ChrisWang on 2017/12/17.
 */

public class WaitingAdapter extends RecyclerView.Adapter<WaitingAdapter.WaitingHolder> {
    private ArrayList<WaitList> mData;
    private Activity mActivity;


    public WaitingAdapter(ArrayList<WaitList> mData, Activity mActivity) {
        this.mData = mData;
        this.mActivity = mActivity;
    }

    @Override
    public WaitingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_row,
                parent, false);
        return new WaitingAdapter.WaitingHolder(view);
    }

    @Override
    public void onBindViewHolder(final WaitingHolder holder, int position) {
        final WaitList waitList = mData.get(position);
        holder.setTv_wait_name(waitList.getR_name());
        holder.setTv_rest_num(waitList.getRestNum());
        holder.setTv_my_num(waitList.getMyNum());
        int id = waitList.getId();
       holder.setWait_linearLayout_Click(id, position);

    }
    public void CancelWaitting(int id, final int position){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    mData.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(mActivity, "已取消候位",
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
               CustomFunction customFunction = new CustomFunction();
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
        WaittingCancelRequest cancelRequest = new WaittingCancelRequest(id, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        queue.add(cancelRequest);
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    public class WaitingHolder extends RecyclerView.ViewHolder{

        private TextView tv_wait_name, tv_rest_num, tv_my_num;
        LinearLayout wait_linearLayout;

        public WaitingHolder(View itemView) {
            super(itemView);
            tv_wait_name = itemView.findViewById(R.id.tv_waiting_name);
            tv_rest_num = itemView.findViewById(R.id.tv_rest_num);
            tv_my_num = itemView.findViewById(R.id.tv_my_num);
            wait_linearLayout = itemView.findViewById(R.id.linear_waitting);

        }

        public void setWait_linearLayout_Click(final int id, final int position){
            wait_linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("提示")
                            .setMessage("確定要取消候位嗎？");
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CancelWaitting(id, position);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
        }
        public void setTv_wait_name(String tv_wait_name) {
            this.tv_wait_name.setText(tv_wait_name);
        }

        public void setTv_rest_num(int tv_rest_num) {
            this.tv_rest_num.setText("目前剩餘組數：" + tv_rest_num);
        }

        public void setTv_my_num(int tv_my_num) {
            this.tv_my_num.setText("您的候位編號：" + tv_my_num);
        }
    }
}


