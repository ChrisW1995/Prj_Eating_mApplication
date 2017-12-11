package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/1.
 */

public class VerifyRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL = "http://cw30cmweb.com/api/Customer/Verify";
    private Map<String, String> params;
    public Map<String, String> getParams() {
        return params;
    }

    public VerifyRequest(String code,String C_Id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("C_Id", C_Id);
            params.put("VerifyCode", code);

        }catch (Exception e){
            Log.d("VerifyRequest: ", e.toString());

        }

    }
}
