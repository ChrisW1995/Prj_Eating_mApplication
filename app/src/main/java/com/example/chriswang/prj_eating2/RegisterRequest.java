package com.example.chriswang.prj_eating2;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ChrisWang on 2017/10/14.
 */

public class RegisterRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "http://pccu-eating.azurewebsites.net/api/Customer/Register";
    private Map<String, String> params;


    public RegisterRequest(String C_Account, String C_Password, String C_Name, String C_PhoneNum, String Email,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("C_Account", C_Account);
            params.put("C_Password", C_Password);
            params.put("C_Name", C_Name);
            params.put("C_PhoneNum", C_PhoneNum);
            params.put("Email", Email);
        }catch (Exception e){
            Log.d("RegisterRequest: ", e.toString());
        }

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
