package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/12.
 */

public class SaveAccountRequest extends StringRequest {
    private static final String SAVE_URL = "http://cw30cmweb.com/api/Customer/SaveAccount";
    private Map<String, String> params;

    public SaveAccountRequest(String id,String old_Password, String password, String name, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, SAVE_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("Id", id);
            params.put("C_Password", password);
            params.put("C_Old_Password", old_Password);
            params.put("C_Name", name);


        } catch (Exception e) {
            Log.d("GetAccountRequest: ", e.toString());
        }

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

