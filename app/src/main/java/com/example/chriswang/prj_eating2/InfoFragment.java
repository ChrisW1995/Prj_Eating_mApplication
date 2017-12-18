package com.example.chriswang.prj_eating2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void init(View v){
        mFeedbackRecycler = v.findViewById(R.id.feedback_recycler);
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mFeedbackRecycler.setLayoutManager(linearLayout);
        mFeedbackRecycler.setHasFixedSize(true);
        mFeedback = new ArrayList<>();
        mFeedbackAdapter = new FeedbackAdapter(mFeedback, getActivity());
        mFeedbackRecycler.setAdapter(mFeedbackAdapter);


        sharedPrefManager = new SharedPrefManager(getContext());
        r_id = getActivity().getIntent().getStringExtra("r_id");
        tv_Name = v.findViewById(R.id.tv_DetailName);
        tv_Address = v.findViewById(R.id.tv_DetailAddr);
        tv_Phone = v.findViewById(R.id.tv_DetailPhone);
        tv_Time = v.findViewById(R.id.tv_R_Time);
        tv_go_add_feedback = v.findViewById(R.id.tv_go_add_feedback);
        imgDetailImg = v.findViewById(R.id.img_DetailImg);
        btnReserve = v.findViewById(R.id.btnReserve);
        btnWaiting = v.findViewById(R.id.btnWaiting);
        tv_avg_score = v.findViewById(R.id.tv_avg_score);
        ratingBar = v.findViewById(R.id.r_detail_rating);

        waitSwitch = getActivity().getIntent().getBooleanExtra("waitSwitch", false);
        String _R_name = getActivity().getIntent().getStringExtra("name");
        tv_Name.setText(getActivity().getIntent().getStringExtra("name"));
        tv_Address.setText("餐廳地址："+ getActivity().getIntent().getStringExtra("address"));
        tv_Phone.setText("聯絡電話："+ getActivity().getIntent().getStringExtra("phone"));
        tv_Time.setText(String.format("營業時間：%s - %s",
                getActivity().getIntent().getStringExtra("openTime"),
                getActivity().getIntent().getStringExtra("closeTime")));
        score = getActivity().getIntent().getFloatExtra("score", 0);
        tv_avg_score.setText(String.valueOf(score));
        ratingBar.setRating(score);
        Glide.with(this)
                .load(getActivity().getIntent().getStringExtra("imgPath"))
                .into(imgDetailImg);

        if(waitSwitch){
            btnWaiting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), WaitingActivity.class);
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
                Intent intent = new Intent(getActivity(), ReserveActivity.class);
                intent.putExtra("r_id", r_id);
                intent.putExtra("r_name",getActivity().getIntent().getStringExtra("name"));
                startActivity(intent);
            }
        });

        tv_go_add_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFeedbackActivity.class);
                intent.putExtra("r_id", r_id);
                intent.putExtra("title", title);
                intent.putExtra("comment", comment);
                intent.putExtra("rating", rating);
                intent.putExtra("id", id);
                startActivityForResult(intent, 101);
            }
        });

        mListener.onFragmentInteraction(this.r_id);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_info, container, false);
        init(v);
        new FetchCommentTask().execute();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_CANCELED){
                mFeedback = null;
                new FetchCommentTask().execute();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }

    public class FetchCommentTask extends AsyncTask<Void, Void, Void> {
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
                    String _title;
                    int rateNum;

                    _title = contentArray.getJSONObject(i).getString("Title");
                    comment = contentArray.getJSONObject(i).getString("Comment");
                    commentTime = contentArray.getJSONObject(i).getString("CommentTime");
                    commentTime = commentTime.substring(0,commentTime.indexOf('T'));
                    C_Name = contentArray.getJSONObject(i).getString("C_Name");
                    rateNum = contentArray.getJSONObject(i).getInt("Rating");
                    if(comment.equals("null"))
                        comment="";
                    Feedback feedback = new Feedback();
                    feedback.setTitle(_title);
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
