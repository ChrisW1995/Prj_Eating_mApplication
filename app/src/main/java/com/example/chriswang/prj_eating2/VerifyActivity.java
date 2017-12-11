package com.example.chriswang.prj_eating2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chriswang.prj_eating2.Request.VerifyRequest;
import com.example.chriswang.prj_eating2.Service.EncryptService;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyActivity extends AppCompatActivity {
    private String code;
    private String C_Id;
    private EditText edtVerifyCode;
    private ProgressDialog progressDialog;
    Button btnVerify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify);
        btnVerify = findViewById(R.id.btnVerify);
        edtVerifyCode = findViewById(R.id.edtVerifyCode);
        C_Id = getIntent().getStringExtra("c_id");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = edtVerifyCode.getText().toString();
                progressDialog = new ProgressDialog(VerifyActivity.this);
                progressDialog.setMessage("驗證中..");
                progressDialog.setTitle("提示");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(VerifyActivity.this,"驗證成功！", Toast.LENGTH_SHORT).show();
                            SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
                            EncryptService encryptService = new EncryptService();

                            String enPassword = encryptService.encrypt(jsonObject.getString("C_Password"));
                            String account = jsonObject.getString("C_Account");
                            String c_id = jsonObject.getString("C_Id");
                            sharedPrefManager.storeAccount(account, enPassword, c_id);
                            Intent i = new Intent(VerifyActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    Toast.makeText(VerifyActivity.this, "驗證碼錯誤", Toast.LENGTH_SHORT).show();
                            }
                            //Additional cases
                        }
                    }
                };


                VerifyRequest verifyActivity = new VerifyRequest(code, C_Id, responseListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(VerifyActivity.this);
                queue.add(verifyActivity);
            }
        });


    }

    /**
     * Created by ChrisWang on 2017/12/1.
     */


}
