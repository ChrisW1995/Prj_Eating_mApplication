package com.example.chriswang.prj_eating2.Service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

/**
 * Created by ChrisWang on 2017/11/15.
 */

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "fcmsharedpref";
    private static final String SHARED_WAIT_NUM = "waitnum";
    private static final String SHARED_RESTAURANT_LIST = "r_list";

    private static final String KEY_ACCESS_TOKEN = "token";
    private static final String KEY_ACCESS_WAIT_NUM = "waitnum";
    private static final String KEY_ACCESS_R_LIST = "list";

    private static Context mContext;
    private static SharedPrefManager mInstance;

    public SharedPrefManager(Context context) {
        this.mContext = context;
    }

    public static synchronized SharedPrefManager getmInstance(Context context){
        if(mInstance==null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean storeToken(String token){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;
    }

    public String getToken(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public boolean storeWaitNum(String num){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_WAIT_NUM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_WAIT_NUM, num);
        editor.apply();
        return true;
    }

    public String getWaitNum() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_WAIT_NUM, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_WAIT_NUM, null);
    }

    public boolean storeAccount(String account, String password, String c_id){
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences;
        mySharedPreferences = mContext.getSharedPreferences("eatingData", mode);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.clear();
        editor.putString("account", account);
        editor.putString("password", password);
        editor.putString("c_id", c_id);
        editor.commit();

        return true;
    }

    public String getC_Id(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("eatingData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("c_id", null);
    }

    public boolean storeRestaurantList(String r_str){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_RESTAURANT_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_R_LIST, r_str);
        editor.apply();
        return true;
    }

    public String getRestaurantList(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_RESTAURANT_LIST, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_R_LIST, null);
    }
}
