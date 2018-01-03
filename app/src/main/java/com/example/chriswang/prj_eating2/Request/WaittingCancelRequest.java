package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/22.
 */

public class WaittingCancelRequest extends StringRequest{
    private static String GET_CANCEL_REQUEST_URL = "http://cw30cmweb.com/api/Waiting/Cancel/";
    private Map<String, String> params;

    public WaittingCancelRequest(int id,
                                 Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, GET_CANCEL_REQUEST_URL + id, listener, errorListener);
        try {

        } catch (Exception e) {
            Log.d("CancelRequest: ", e.toString());
        }

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
