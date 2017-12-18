package com.example.chriswang.prj_eating2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chriswang.prj_eating2.Request.AddFeedbackRequest;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;

public class AddFeedbackActivity extends AppCompatActivity {
    private TextView tv_feedback_commit;
    private EditText edt_comment, edt_title;
    RatingBar feedback_rating;
    private String c_id, r_id, comment, title;
    private String rating;
    private String id;
    private SharedPrefManager sharedPrefManager;
    private ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        tv_feedback_commit = findViewById(R.id.tv_feedback_commit);
        edt_comment = findViewById(R.id.edt_comment);
        edt_title = findViewById(R.id.edt_title);
        feedback_rating = findViewById(R.id.add_feedback_ratingBar);

        c_id = sharedPrefManager.getC_Id();
        r_id = getIntent().getStringExtra("r_id");
        comment = getIntent().getStringExtra("comment");
        title = getIntent().getStringExtra("title");
        rating = getIntent().getStringExtra("rating");
        id = getIntent().getStringExtra("id");

        if(!rating.equals(""))
            feedback_rating.setRating(Float.parseFloat(rating));
        if(comment.equals("null"))
            comment = "";
        if(title.equals(""))
            title ="";

        edt_comment.setText(comment);
        edt_title.setText(title);

        tv_feedback_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = String.valueOf(feedback_rating.getRating());
                comment = edt_comment.getText().toString();
                title = edt_title.getText().toString();

                progressDialog = new ProgressDialog(AddFeedbackActivity.this);
                progressDialog.setMessage("請稍候..");
                progressDialog.setTitle("提示");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                AddFeedback();
            }
        });
    }
    public void AddFeedback(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    progressDialog.dismiss();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                    Toast.makeText(AddFeedbackActivity.this, "評論完成", Toast.LENGTH_SHORT).show();
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddFeedbackActivity.this, "評論失敗，請稍後再試試", Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        AddFeedbackRequest feedbackRequestRequest = new AddFeedbackRequest(id, c_id, r_id, title, comment,
                rating, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(AddFeedbackActivity.this);
        queue.add(feedbackRequestRequest);

    }
}
