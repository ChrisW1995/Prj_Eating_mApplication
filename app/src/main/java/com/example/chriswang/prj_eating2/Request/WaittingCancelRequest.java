package com.example.chriswang.prj_eating2.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChrisWang on 2017/12/22.
 */

public class GetNewCouponRequest extends StringRequest{
    private static final String GET_COUPON_REQUEST_URL = "http://cw30cmweb.com/api/Coupon/StoreCoupon";
    private Map<String, String> params;

    public GetNewCouponRequest(String coupon_id, String c_id,
                        Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, GET_COUPON_REQUEST_URL, listener, errorListener);
        try {
            params = new HashMap<>();
            params.put("Coupon_Id", coupon_id);
            params.put("C_Id", c_id);

        } catch (Exception e) {
            Log.d("RecieveCouponRequest: ", e.toString());
        }

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
