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
import com.example.chriswang.prj_eating2.Request.RegisterRequest;
import com.example.chriswang.prj_eating2.Request.SMSRequest;
import com.example.chriswang.prj_eating2.Service.CustomFunction;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName, edtAccount, edtPassword, edtChkPassword,
            edtEmail, edtPhone;
    Button btnRegister;

    ProgressDialog progressDialog;
    private CustomFunction customFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtAccount = findViewById(R.id.edtAccount);
        edtPassword = findViewById(R.id.edtPassword);
        edtChkPassword = findViewById(R.id.edtChkPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edt_reserve_phone);
        btnRegister = findViewById(R.id.btnRegister);
        customFunction = new CustomFunction();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!customFunction.isConnectingToInternet(RegisterActivity.this)){
                    Toast.makeText(RegisterActivity.this, "請確認網路連線是否正常", Toast.LENGTH_SHORT).show();
                    return;

                }
                final String account = edtAccount.getText().toString();
                final String password = edtPassword.getText().toString();
                final String chkPassword = edtChkPassword.getText().toString();
                final String phone = edtPhone.getText().toString();
                final String email = edtEmail.getText().toString();
                final String name = edtName.getText().toString();
                final int verifyCode = customFunction.getVerifyCode();

                if(!password.equals(chkPassword)){
                    Toast.makeText(RegisterActivity.this, "兩次輸入的密碼不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("註冊中..");
                progressDialog.setTitle("提示");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String c_id = jsonObject.getString("C_Id");
                            SendSMS(String.valueOf(verifyCode),c_id , jsonObject.getString("C_PhoneNum"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                //When listen Error
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
                                    if(json != null) displayMessage(json);
                                    break;

                            }
                            //Additional cases
                        }
                        btnRegister.setEnabled(true);
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(account, password, name,
                        phone, email, verifyCode, responseListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);


            }
        });


    }

    public void SendSMS(final String code, final String c_id , String phone){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                    intent.putExtra("c_id", c_id);
                    RegisterActivity.this.startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                finish();
            }
        };

        SMSRequest smsRequest = new SMSRequest(code, phone, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(smsRequest);

    }

    public String trimMessage(String json, String key){
        String trimmedString;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }
    public void displayMessage(String toastString){
        Toast.makeText(RegisterActivity.this, toastString, Toast.LENGTH_LONG).show();
    }

}


