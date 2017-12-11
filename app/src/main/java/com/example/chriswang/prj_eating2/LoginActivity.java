package com.example.chriswang.prj_eating2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.example.chriswang.prj_eating2.Request.LoginRequest;
import com.example.chriswang.prj_eating2.Service.CustomFunction;
import com.example.chriswang.prj_eating2.Service.EncryptService;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;

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
        runtime_permission();
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
                    if(!runtime_permission())
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
                        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
                        int mode = Activity.MODE_PRIVATE;

                        String enPassword = encryptService.encrypt(jsonObject.getString("C_Password"));
                        String account = jsonObject.getString("C_Account");
                        String c_id = jsonObject.getString("C_Id");
                        sharedPrefManager.storeAccount(account, enPassword, c_id);
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
                    String json;
                    progressDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 404:
                                Toast.makeText(LoginActivity.this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                json = new String(response.data);
                                String[]msgArray = trimMessage(json);
                                Intent i = new Intent(LoginActivity.this, VerifyActivity.class);
                                i.putExtra("c_id", msgArray[1]);
                                Toast.makeText(LoginActivity.this, msgArray[0], Toast.LENGTH_SHORT).show();
                                startActivity(i);
                                finish();
                                break;
                            case 500:
                                Toast.makeText(LoginActivity.this, "系統錯誤，請稍後再試", Toast.LENGTH_SHORT).show();
                                break;
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
    public boolean runtime_permission(){
        if(Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

            }else {
                runtime_permission();
            }
        }
    }

    public String[] trimMessage(String json){
        String trimmedString;
        String[] msgArray;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString("Message");
            msgArray = trimmedString.split(","); //array[1] = C_id
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return msgArray;
    }
}



