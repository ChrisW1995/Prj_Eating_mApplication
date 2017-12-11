package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/6.
 */

public class ReservationTimeRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://cw30cmweb.com/api/Reservation/GetReservationTime";
    private Map<String, String> params;
    public Map<String, String> getParams() {
        return params;
    }

    public ReservationTimeRequest(String r_id, String peopleNum, String isSmoke, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("R_Id", r_id);
            params.put("PeopleNum", peopleNum);
            params.put("IsSmoke", isSmoke);

        }catch (Exception e){
            Log.d("ReservationTimeRequest:", e.toString());

        }

    }
}
