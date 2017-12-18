package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/11.
 */

public class GetAccountRequest extends StringRequest {
    private static final String SMS_URL = "http://cw30cmweb.com/api/Customer/GetAccountInfo";
    private Map<String, String> params;

    public GetAccountRequest(String id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, SMS_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("Id", id);


        } catch (Exception e) {
            Log.d("GetAccountRequest: ", e.toString());
        }

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
