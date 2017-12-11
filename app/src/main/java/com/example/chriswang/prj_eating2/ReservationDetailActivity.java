package com.example.chriswang.prj_eating2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chriswang.prj_eating2.Request.ReservationTimeRequest;
import com.example.chriswang.prj_eating2.Request.ReserveRequest;
import com.example.chriswang.prj_eating2.Request.SMSRequest;
import com.example.chriswang.prj_eating2.Service.CustomFunction;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReservationDetailActivity extends AppCompatActivity {

    private SharedPrefManager sharedPrefManager;
    private Button btn_check_reserve;
    private ProgressDialog progressDialog;
    private String r_id, str_smoke, name, phone, date, time, detail, peopleNum, smoke, c_id;
    private Spinner spin_time_list;
    private EditText edt_reserve_detail;
    private TextView tv_reservecheck_r_name, tv_reservecheck_date, tv_reservecheck_people,
            tvIsSmoke, tv_reservecheck_name, tv_reservecheck_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        spin_time_list = findViewById(R.id.spinner_time_list);
        btn_check_reserve = findViewById(R.id.btn_check_reserve);

        tv_reservecheck_r_name = findViewById(R.id.tv_reservecheck_r_name);
        tv_reservecheck_date = findViewById(R.id.tv_reservecheck_date);
        tv_reservecheck_people = findViewById(R.id.tv_reservecheck_people);
        tvIsSmoke = findViewById(R.id.tvIsSmoke);
        tv_reservecheck_name = findViewById(R.id.tv_reservecheck_name);
        tv_reservecheck_phone = findViewById(R.id.tv_reservecheck_phone);

        edt_reserve_detail = findViewById(R.id.edt_reserve_detail);

        r_id = getIntent().getStringExtra("r_id");
        c_id = sharedPrefManager.getC_Id();
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        date = getIntent().getStringExtra("date");
        peopleNum = getIntent().getStringExtra("peopleNum");
        str_smoke = getIntent().getStringExtra("smoke");

        if(str_smoke.equals("是"))
            smoke = "true";
        else
            smoke = "false";
        reserveTimeRequest();
        tv_reservecheck_r_name.setText(getIntent().getStringExtra("r_name"));
        tv_reservecheck_people.setText(peopleNum + " 位");
        tv_reservecheck_phone.setText(phone);
        tv_reservecheck_name.setText(name);
        tv_reservecheck_date.setText(date);
        tvIsSmoke.setText(str_smoke);


        btn_check_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ReservationDetailActivity.this);
                progressDialog.setMessage("處理中..");
                progressDialog.setTitle("提示");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                Reserve();
            }
        });

    }

    private void Reserve(){
        time = spin_time_list.getSelectedItem().toString();
        detail = edt_reserve_detail.getText().toString();


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                    Toast.makeText(ReservationDetailActivity.this, "訂位完成", Toast.LENGTH_SHORT).show();
                    ReservationDetailActivity.this.finish();

                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json;
                progressDialog.dismiss();
                CustomFunction customFunction = new CustomFunction();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            Toast.makeText(ReservationDetailActivity.this, "無可訂位時間", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case 400:
                            json = new String(response.data);
                            json = customFunction.trimMessage(json,"Message");
                            Toast.makeText(ReservationDetailActivity.this, json, Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        default:
                            Toast.makeText(ReservationDetailActivity.this, "訂位失敗", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    //Additional cases
                }
            }

        };

        ReserveRequest request = new ReserveRequest(name, phone, smoke, r_id, c_id, peopleNum, time, date, detail, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(ReservationDetailActivity.this);
        queue.add(request);

    }

    private void reserveTimeRequest() {

        progressDialog = new ProgressDialog(ReservationDetailActivity.this);
        progressDialog.setMessage("處理中..");
        progressDialog.setTitle("提示");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    ArrayList<String>timeArray = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("ReseTime: ", jsonArray.toString());
                    for(int i = 0 ; i < jsonArray.length(); i++){
                        String time = jsonArray.getJSONObject(i).getString("ReservationTime");
                        time = time.substring(0, time.lastIndexOf(':'));
                        timeArray.add(time);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ReservationDetailActivity.this,android.R.layout.simple_spinner_dropdown_item, timeArray);
                    spin_time_list.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json;
                progressDialog.dismiss();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 404:
                            Toast.makeText(ReservationDetailActivity.this, "無可訂位時間", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case 400:
                            Toast.makeText(ReservationDetailActivity.this, "錯誤", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        default:
                            Toast.makeText(ReservationDetailActivity.this, "錯誤", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    //Additional cases
                }
            }

        };
        ReservationTimeRequest request = new ReservationTimeRequest(r_id, peopleNum, smoke, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(ReservationDetailActivity.this);
        queue.add(request);

    }
}
