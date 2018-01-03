package com.example.chriswang.prj_eating2.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.chriswang.prj_eating2.RestaurantInfoActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ChrisWang on 2017/12/19.
 */

public class GetPlacesOnMap extends AsyncTask<Object, String, String> implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener{
    private SharedPrefManager sharedPrefManager;
    GoogleMap mMap;
    Context mContext;
    Activity mActivity;

    public GetPlacesOnMap(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void GetPlaces(JSONArray restaurantArray){

        MarkerOptions markerOptions = new MarkerOptions();
        Marker marker;

        try {


            for(int i = 0; i<restaurantArray.length(); i++){
                double lat, lng;
                String r_Name;
                String r_id;
                String OpenTime;
                String CloseTime;

                r_Name = restaurantArray.getJSONObject(i).getString("R_Name");
                lat = restaurantArray.getJSONObject(i).getDouble("Lat");
                lng = restaurantArray.getJSONObject(i).getDouble("Lng");
                r_id = restaurantArray.getJSONObject(i).getString("Id");
                OpenTime = restaurantArray.getJSONObject(i).getString("StartTime");
                OpenTime = OpenTime.substring(0,OpenTime.lastIndexOf(':'));
                CloseTime = restaurantArray.getJSONObject(i).getString("CloseTime");
                CloseTime = CloseTime.substring(0,CloseTime.lastIndexOf(':'));
                LatLng latLng = new LatLng(lat, lng);
                marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(r_Name)
                        .snippet("營業時間："+OpenTime+" - "+CloseTime)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                marker.setTag(restaurantArray.getJSONObject(i));
                mMap.setOnInfoWindowClickListener(this);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];

        sharedPrefManager = new SharedPrefManager(mContext);
        String remoteString = sharedPrefManager.getRestaurantList();
        if(remoteString == null){
            return null;
        }
        return remoteString;
    }
    @Override
    protected void onPostExecute(String s){


        JSONArray restaurantArray = null;
        try {
            restaurantArray = new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetPlaces(restaurantArray);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        JSONObject r_object = (JSONObject) marker.getTag();
        if(r_object != null){
            String r_Name="", r_County="", r_Area="",
                    r_Phone="", r_Address="", r_DetailAddress="",
                    r_id="", r_ImgPath="", OpenTime="",
                    CloseTime="";
            boolean wait_switch = false, coupon = false;
            float score = 0;
            try {
                if(!r_object.getString("ImagePath").equals("null")){
                    r_ImgPath = "http://cw30cmweb.com" + r_object.getString("ImagePath");
                }else
                    r_ImgPath = "http://cw30cmweb.com/Img/index.jpg";

                r_id = r_object.getString("Id");
                wait_switch = r_object.getBoolean("WaitListSwitch");
                coupon = r_object.getBoolean("ExistCoupon");
                r_County = r_object.getString("R_County");
                r_Area = r_object.getString("R_Area");
                r_Name = r_object.getString("R_Name");
                r_Phone = r_object.getString("R_PhoneNum");
                r_DetailAddress = r_object.getString("R_DetailAddress");
                r_Address = r_County+r_Area+r_DetailAddress;
                score = Float.parseFloat(r_object.getString("Score"));
                OpenTime = r_object.getString("StartTime");
                OpenTime = OpenTime.substring(0,OpenTime.lastIndexOf(':'));
                CloseTime = r_object.getString("CloseTime");
                CloseTime = CloseTime.substring(0,CloseTime.lastIndexOf(':'));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(mActivity, RestaurantInfoActivity.class);
            intent.putExtra("r_id", r_id);
            intent.putExtra("name", r_Name);
            intent.putExtra("phone", r_Phone);
            intent.putExtra("address", r_Address);
            intent.putExtra("imgPath", r_ImgPath);
            intent.putExtra("waitSwitch", wait_switch);
            intent.putExtra("openTime", OpenTime);
            intent.putExtra("closeTime", CloseTime);
            intent.putExtra("score", score);
            intent.putExtra("coupon", coupon);
            mActivity.startActivity(intent);
        }
    }
}
