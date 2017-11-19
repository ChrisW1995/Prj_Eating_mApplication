package com.example.chriswang.prj_eating2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.chriswang.prj_eating2.Service.CustomFunction;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WaitingActivity extends AppCompatActivity {

    private Button btnWaitingCommit;
    private EditText edtWaitingDetail;
    private TextView tvCurrentNum;
    private Spinner spinnerNum;
    private CustomFunction customFunction;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        btnWaitingCommit = findViewById(R.id.btnWaitingCommit);
        edtWaitingDetail = findViewById(R.id.edtWaitingDetail);
        tvCurrentNum = findViewById(R.id.tv_current_waiting_num);
        spinnerNum = findViewById(R.id.spin_wait_people_num);
        customFunction = new CustomFunction();

        final Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000); // As I am using LENGTH_LONG in Toast
                    WaitingActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        final SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences("eatingData", Activity.MODE_PRIVATE);

        initSpinner();

        btnWaitingCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!customFunction.isConnectingToInternet(WaitingActivity.this)){
                    Toast.makeText(WaitingActivity.this, "請確認網路連線是否正常", Toast.LENGTH_SHORT).show();
                    return;

                }

                final String c_id = mySharedPreferences.getString("c_id", "");
                final String r_id = getIntent().getStringExtra("r_id");
                final String detail = edtWaitingDetail.getText().toString();
                final String regDeviceId = SharedPrefManager.getmInstance(getApplicationContext()).getToken();
                final String peopleNum = spinnerNum.getSelectedItem().toString();

                progressDialog = new ProgressDialog(WaitingActivity.this);
                progressDialog.setMessage("處理中..");
                progressDialog.setTitle("提示");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String num = jsonObject.getString("CurrentNo");
                            Toast.makeText(WaitingActivity.this, "已加入候位列表，號碼："+num,
                                    Toast.LENGTH_SHORT).show();
                            SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
                            sharedPrefManager.storeWaitNum(num);
                            thread.start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String json;

                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    json = new String(response.data);
                                    json = customFunction.trimMessage(json, "Message");
                                    if(json != null) {
                                        Toast.makeText(WaitingActivity.this, json, Toast.LENGTH_SHORT).show();
                                    }
                                    break;

                            }
                            //Additional cases
                        }
                    }
                };
                AddWaitingRequest waitingRequest = new AddWaitingRequest(r_id, c_id, detail, peopleNum, regDeviceId,
                        listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(WaitingActivity.this);
                queue.add(waitingRequest);
            }
        });
       ///end btnClick

    }

    private void initSpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 1; i<=12; i++){
            arrayList.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinnerNum.setAdapter(adapter);

    }

}
