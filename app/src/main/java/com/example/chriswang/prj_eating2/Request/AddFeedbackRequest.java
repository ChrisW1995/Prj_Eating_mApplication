package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/15.
 */

public class AddFeedbackRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://cw30cmweb.com/api/Feedback/AddComment";
    private Map<String, String> params;
    public Map<String, String> getParams() {
        return params;
    }

    public AddFeedbackRequest(String id, String C_Id, String r_id, String title, String Comment, String rating,
                              Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            rating = rating.substring(0,1);
            if(id.equals(""))
                id="0";
            params.put("Id", id);
            params.put("C_Id", C_Id);
            params.put("R_Id", r_id);
            params.put("Comment", Comment);
            params.put("Title", title);
            params.put("Rating", rating);

        }catch (Exception e){
            Log.d("AddFeedbackRequest: ", e.toString());

        }

    }
}
