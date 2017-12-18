package com.example.chriswang.prj_eating2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;
import com.example.chriswang.prj_eating2.adapters.FeedbackAdapter;
import com.example.chriswang.prj_eating2.model.Feedback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView tv_Name, tv_Address, tv_Phone,tv_Time,tv_avg_score,
    tv_go_add_feedback;
    private String r_id;
    private ImageView imgDetailImg;
    private Button btnReserve, btnWaiting;
    private boolean waitSwitch;
    private ArrayList<Feedback> mFeedback;
    private FeedbackAdapter mFeedbackAdapter;
    private RecyclerView mFeedbackRecycler;
    private SharedPrefManager sharedPrefManager;
    private RatingBar ratingBar;
    private float score;

    private String id="", comment="", title="", rating="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        mFeedbackRecycler = findViewById(R.id.feedback_recycler);
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mFeedbackRecycler.setLayoutManager(linearLayout);
        mFeedbackRecycler.setHasFixedSize(true);
        mFeedback = new ArrayList<>();
        mFeedbackAdapter = new FeedbackAdapter(mFeedback, RestaurantDetailActivity.this);
        mFeedbackRecycler.setAdapter(mFeedbackAdapter);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        r_id = getIntent().getStringExtra("r_id");
        tv_Name = findViewById(R.id.tv_DetailName);
        tv_Address = findViewById(R.id.tv_DetailAddr);
        tv_Phone = findViewById(R.id.tv_DetailPhone);
        tv_Time = findViewById(R.id.tv_R_Time);
        tv_go_add_feedback = findViewById(R.id.tv_go_add_feedback);
        imgDetailImg = findViewById(R.id.img_DetailImg);
        btnReserve = findViewById(R.id.btnReserve);
        btnWaiting = findViewById(R.id.btnWaiting);
        tv_avg_score = findViewById(R.id.tv_avg_score);
        ratingBar = findViewById(R.id.r_detail_rating);

        waitSwitch = getIntent().getBooleanExtra("waitSwitch", false);
        tv_Name.setText(getIntent().getStringExtra("name"));
        tv_Address.setText("餐廳地址："+ getIntent().getStringExtra("address"));
        tv_Phone.setText("聯絡電話："+ getIntent().getStringExtra("phone"));
        tv_Time.setText(String.format("營業時間：%s - %s",
                getIntent().getStringExtra("openTime"),
                getIntent().getStringExtra("closeTime")));
        score = getIntent().getFloatExtra("score", 0);
        tv_avg_score.setText(String.valueOf(score));
        ratingBar.setRating(score);
        Glide.with(this)
                .load(getIntent().getStringExtra("imgPath"))
                .into(imgDetailImg);

        if(waitSwitch){
            btnWaiting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(RestaurantDetailActivity.this, WaitingActivity.class);
                    i.putExtra("r_id", r_id);
                    startActivity(i);
                }
            });
        }else {

            Drawable drawable = this.getResources().getDrawable(R.drawable.background_btn_gray);
            btnWaiting.setEnabled(false);
            btnWaiting.setBackground(drawable);
        }



        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailActivity.this, ReserveActivity.class);
                intent.putExtra("r_id", r_id);
                intent.putExtra("r_name",getIntent().getStringExtra("name"));
                startActivity(intent);
            }
        });

        tv_go_add_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailActivity.this, AddFeedbackActivity.class);
                intent.putExtra("r_id", r_id);
                intent.putExtra("title", title);
                intent.putExtra("comment", comment);
                intent.putExtra("rating", rating);
                intent.putExtra("id", id);
                startActivityForResult(intent, 101);
            }
        });
        new FetchCommentTask().execute();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_CANCELED){
                new FetchCommentTask().execute();
            }
        }
    }
    public class FetchCommentTask extends AsyncTask<Void, Void, Void>{
        private String mRemoteString;

        @Override
        protected Void doInBackground(Void... voids) {
            String c_id = sharedPrefManager.getC_Id();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri uri = Uri.parse(getString(R.string.get_feedback_api) + r_id +"/"+c_id);
            URL url;
            try {
                url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                mRemoteString = buffer.toString();
                JSONObject jsonObject = new JSONObject(mRemoteString);

                JSONArray contentArray = new JSONArray(jsonObject.getString("FeedbackContent"));

                if(!jsonObject.getString("Feedback").equals("null")){
                    JSONObject single_feedback = new JSONObject(jsonObject.getString("Feedback"));
                    comment = single_feedback.getString("Comment");
                    title = single_feedback.getString("Title");
                    rating = single_feedback.getString("Rating");
                    id = single_feedback.getString("Id");
                }
                String avg_score = jsonObject.getString("RatingAvg");
                Log.v("contentArray", contentArray.toString());
                Log.v("avgScore", avg_score);
                for (int i = 0; i < contentArray.length(); i++) {
                    String comment;
                    String commentTime;
                    String C_Name;
                    int rateNum;

                    comment = contentArray.getJSONObject(i).getString("Comment");
                    commentTime = contentArray.getJSONObject(i).getString("CommentTime");
                    commentTime = commentTime.substring(0,commentTime.indexOf('T'));
                    C_Name = contentArray.getJSONObject(i).getString("C_Name");
                    rateNum = contentArray.getJSONObject(i).getInt("Rating");
                    if(comment.equals("null"))
                        comment="";
                    Feedback feedback = new Feedback();
                    feedback.setC_Name(C_Name);
                    feedback.setRateNum(rateNum);
                    feedback.setComment(comment);
                    feedback.setCommentTime(commentTime);
                    feedback.setR_id(r_id);

                    mFeedback.add(feedback);
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("GetFeedback", "Error closing stream", e);
                    }
                }
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            mFeedbackAdapter.notifyDataSetChanged();
        }
    }
}
