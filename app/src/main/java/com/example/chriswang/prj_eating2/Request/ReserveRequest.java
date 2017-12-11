package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/7.
 */

public class ReserveRequest extends StringRequest{
    private static final String SMS_URL = "http://cw30cmweb.com/api/Reservation/Reserve";
    private Map<String, String> params;

    public ReserveRequest(String Name, String PhoneNum, String IsSomke,
                          String R_Id, String C_Id, String peopleNum,
                          String ReserveTime, String ReserveDate, String details,
                          Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, SMS_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("Name", Name);
            params.put("PhoneNum", PhoneNum);
            params.put("PeopleNum", peopleNum);
            params.put("IsSomke", IsSomke);
            params.put("R_Id", R_Id);
            params.put("C_Id", C_Id);
            params.put("Details", details);
            params.put("ReserveTime", ReserveTime);
            params.put("ReserveDate", ReserveDate);

        } catch (Exception e) {
            Log.d("Request: ", e.toString());
        }

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
