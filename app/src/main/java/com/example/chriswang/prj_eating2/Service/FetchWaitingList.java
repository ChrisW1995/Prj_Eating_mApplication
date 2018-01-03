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
import com.example.chriswang.prj_eating2.adapters.WaitingAdapter;
import com.example.chriswang.prj_eating2.model.Coupon;
import com.example.chriswang.prj_eating2.model.WaitList;

import org.json.JSONArray;

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

public class FetchWaitingList extends AsyncTask<Object, Void, Void> {
    Context mContext;
    Activity mActivity;
    String r_id;
    private String mRemoteString;
    private SharedPrefManager sharedPrefManager;
    private WaitingAdapter mWaitAdapter;
    private ArrayList<WaitList> mWaits;
    private RecyclerView mWaitRecycler;
    public FetchWaitingList(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        mWaits = (ArrayList<WaitList>) objects[0];
        mWaitAdapter = (WaitingAdapter)objects[1];
        mWaitRecycler = (RecyclerView)objects[2];
        sharedPrefManager = new SharedPrefManager(mContext);
        String c_id = sharedPrefManager.getC_Id();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        Uri uri = Uri.parse(mContext.getString(R.string.get_waiting_list_api) + c_id);
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
            JSONArray waitingArray = new JSONArray(mRemoteString);

            Log.v("contentArray", waitingArray.toString());
            for (int i = 0; i < waitingArray.length(); i++) {

                String addr;
                int mynum;
                int rest_num;
                String r_phone;
                String startTime;
                String endTime;
                String r_id;
                int id;
                String r_name;


                r_id= waitingArray.getJSONObject(i).getString("R_Id");
                mynum = waitingArray.getJSONObject(i).getInt("MyNum");
                rest_num = waitingArray.getJSONObject(i).getInt("RestNum");
                r_name = waitingArray.getJSONObject(i).getString("R_Name");
                addr = waitingArray.getJSONObject(i).getString("R_Address");
                id = waitingArray.getJSONObject(i).getInt("Id");
                WaitList waitList = new WaitList();
                waitList.setR_name(r_name);
                waitList.setR_addr(addr);
                waitList.setR_id(r_id);
                waitList.setRestNum(rest_num);
                waitList.setMyNum(mynum);
                waitList.setId(id);


                mWaits.add(waitList);
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
        mWaitRecycler.setLayoutManager(linearLayout);
        mWaitRecycler.setHasFixedSize(true);
        mWaitAdapter = new WaitingAdapter(mWaits, mActivity);
        mWaitRecycler.setAdapter(mWaitAdapter);
    }
}
