package com.example.chriswang.prj_eating2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chriswang.prj_eating2.Service.CustomFunction;
import com.example.chriswang.prj_eating2.Service.EncryptService;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText edtLoginAcc, edtLoginPass;
    private CustomFunction customFunction;
    private Button btnLogin, btnGoRegister;
    private EncryptService encryptService = new EncryptService();
    private String mAccount, mPassword;
    private Boolean checkFile = false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        int mode = Activity.MODE_PRIVATE;

            try {
                SharedPreferences mySharedPreferences;
                mySharedPreferences = getSharedPreferences("eatingData", mode);
                mAccount = mySharedPreferences.getString("account", "");
                mPassword = mySharedPreferences.getString("password", "");
                mPassword = encryptService.decrypt(mPassword);
                if(!mAccount.equals("")){
                    login();
                }
            } catch (Exception e) {
                Log.d("TAG", "ERROR ");
                e.printStackTrace();
            }


        edtLoginAcc = findViewById(R.id.edtLoginAcc);
        edtLoginPass = findViewById(R.id.edtLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);
        btnGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccount = edtLoginAcc.getText().toString();
                mPassword = edtLoginPass.getText().toString();
                login();
            }
        });
    }

    public void login() {
        try {
            if (!customFunction.isConnectingToInternet(LoginActivity.this)) {
                Toast.makeText(LoginActivity.this, "請檢察網路狀態是否正常", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("登入中..");
            progressDialog.setTitle("提示");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            Response.Listener<String> responeListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        progressDialog.dismiss();

                            int mode = Activity.MODE_PRIVATE;
                            SharedPreferences mySharedPreferences;
                            mySharedPreferences = getSharedPreferences("eatingData", mode);
                            SharedPreferences.Editor editor = mySharedPreferences.edit();
                            editor.clear();
                            String enPassword = encryptService.encrypt(jsonObject.getString("C_Password"));
                            editor.putString("account", jsonObject.getString("C_Account"));
                            editor.putString("password", enPassword);
                            editor.putString("c_id", jsonObject.getString("C_Id"));
                            editor.commit();

                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
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
                    progressDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 404:
                                Toast.makeText(LoginActivity.this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
                        }
                        //Additional cases
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(mAccount, mPassword, responeListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "請檢察網路狀態是否連線", Toast.LENGTH_SHORT).show();
        }

    }
}



