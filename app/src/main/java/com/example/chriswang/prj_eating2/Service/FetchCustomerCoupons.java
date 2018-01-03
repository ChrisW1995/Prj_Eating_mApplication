package com.example.chriswang.prj_eating2.Service;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.chriswang.prj_eating2.R;
import com.example.chriswang.prj_eating2.adapters.CouponAdapter;
import com.example.chriswang.prj_eating2.model.Coupon;
import com.example.chriswang.prj_eating2.model.Feedback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ChrisWang on 2017/12/20.
 */

public class FetchRestaurantCoupons extends AsyncTask<Object, Void, Void> {
    Context mContext;
    Activity mActivity;
    String r_id;
    private String mRemoteString;
    private SharedPrefManager sharedPrefManager;
    private CouponAdapter mCouponAdapter;
    private ArrayList<Coupon> mCoupons;
    private RecyclerView mCouponRecycler;
    public FetchRestaurantCoupons(Context mContext, String r_id, Activity mActivity) {
        this.mContext = mContext;
        this.r_id = r_id;
        this.mActivity = mActivity;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        mCoupons = (ArrayList<Coupon>) objects[0];
        mCouponAdapter = (CouponAdapter)objects[1];
        mCouponRecycler = (RecyclerView)objects[2];
        sharedPrefManager = new SharedPrefManager(mContext);
        String c_id = sharedPrefManager.getC_Id();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        Uri uri = Uri.parse(mContext.getString(R.string.get_r_coupon_api) + r_id + "/" + c_id);
        URL url;
        try {
            url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            mRemoteString = buffer.toString();
            JSONArray couponsArray = new JSONArray(mRemoteString);

            Log.v("contentArray", couponsArray.toString());
            for (int i = 0; i < couponsArray.length(); i++) {
                String title;
                String content;
                String startTime;
                String endTime;
                String r_id;
                String id;
                boolean isReceived;

                r_id= couponsArray.getJSONObject(i).getString("R_Id");
                id = couponsArray.getJSONObject(i).getString("CouponId");
                title = couponsArray.getJSONObject(i).getString("Title");
                content = couponsArray.getJSONObject(i).getString("Desciption");
                startTime = couponsArray.getJSONObject(i).getString("StartTime");
                startTime = startTime.replace("T"," ");
                endTime = couponsArray.getJSONObject(i).getString("EndTime");
                endTime = endTime.replace("T"," ");
                isReceived = couponsArray.getJSONObject(i).getBoolean("IsReceive");
                Coupon coupon = new Coupon();
                coupon.setCouponId(id);
                coupon.setTitle(title);
                coupon.setDescription(content);
                coupon.setStartTime(startTime);
                coupon.setEndTime(endTime);
                coupon.setReceived(isReceived);
                coupon.setR_id(r_id);

                mCoupons.add(coupon);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("GetCoupon", "Error closing stream", e);
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(mActivity);
        mCouponRecycler.setLayoutManager(linearLayout);
        mCouponRecycler.setHasFixedSize(true);
        mCouponAdapter = new CouponAdapter(mCoupons, mActivity, mContext);
        mCouponRecycler.setAdapter(mCouponAdapter);
    }
}
