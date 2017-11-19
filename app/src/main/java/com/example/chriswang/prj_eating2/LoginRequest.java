package com.example.chriswang.prj_eating2;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/10/15.
 */

public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://pccu-eating.azurewebsites.net/api/Customer/Login";
    private Map<String, String> params;

    public LoginRequest(String C_Account, String C_Password,
                        Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("C_Account", C_Account);
            params.put("C_Password", C_Password);

        } catch (Exception e) {
            Log.d("RegisterRequest: ", e.toString());
        }

    }

    public Map<String, String> getParams() {
        return params;
    }

}
