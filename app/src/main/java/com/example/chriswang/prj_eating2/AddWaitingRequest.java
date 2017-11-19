package com.example.chriswang.prj_eating2;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/11/17.
 */

public class AddWaitingRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://pccu-eating.azurewebsites.net/api/Waiting/NewWaiting";
    private Map<String, String> params;

    public AddWaitingRequest(String r_id, String c_id, String detail, String peopleNum, String RegDeviceID,
                             Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("R_Id", r_id);
            params.put("C_Id", c_id);
            params.put("RegDeviceID", RegDeviceID);
            params.put("PeopleNum", peopleNum);
            params.put("Detail", detail);
        }catch (Exception e){
            Log.d("AddWatingRequest: ", e.toString());
        }


    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
