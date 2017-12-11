package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/1.
 */

public class SMSRequest extends StringRequest {
    private static final String SMS_URL = "https://rest.nexmo.com/sms/json";
    private Map<String, String> params;

    public SMSRequest(String code, String phone, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, SMS_URL, listener, errorListener);
        try {
            phone = phone.substring(1);
            params = new HashMap<>();
            params.put("api_key", "1af3d4b1");
            params.put("api_secret", "3aa4388b3d5219d3");
            params.put("from", "有食候");
            params.put("to","886"+phone);
            params.put("text", "有食候帳號驗證系統，您的驗證碼為："+code);


        } catch (Exception e) {
            Log.d("SMSRequest: ", e.toString());
        }

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
