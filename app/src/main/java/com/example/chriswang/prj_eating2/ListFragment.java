package com.example.chriswang.prj_eating2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.chriswang.prj_eating2.Service.FCMIdService;
import com.example.chriswang.prj_eating2.Service.GPS_Service;
import com.example.chriswang.prj_eating2.Service.SharedPrefManager;
import com.example.chriswang.prj_eating2.adapters.RestaurantAdapter;
import com.example.chriswang.prj_eating2.model.Restaurant;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Custom prop.
    private RecyclerView mRestaurantRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private ArrayList<Restaurant> mRestaurantCollection;
    private PullRefreshLayout pullRefreshLayout;
    private BroadcastReceiver broadcastReceiver;
    private GPS_Service gps_service;
    private double c_lat, c_lng;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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

    private void init(View v) {
        mRestaurantRecyclerView = v.findViewById(R.id.restaurant_recycler);
        mRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRestaurantRecyclerView.setHasFixedSize(true);
        mRestaurantCollection = new ArrayList<>();
        mRestaurantAdapter = new RestaurantAdapter(mRestaurantCollection, getActivity());
        mRestaurantRecyclerView.setAdapter(mRestaurantAdapter);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_list, container, false);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        gps_service = new GPS_Service(getContext());
        getGPS();


        init(v);
        new FetchDataTask().execute();
        pullRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGPS();
                init(v);
                new FetchDataTask().execute();
                pullRefreshLayout.setRefreshing(false);
                
            }
        });
        

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getActivity(), SharedPrefManager.getmInstance(getContext()).getToken(),Toast.LENGTH_SHORT).show();

            }
        };

        Log.d(TAG, "onResume: "+SharedPrefManager.getmInstance(getContext()).getToken());
    }

    public void getGPS(){

        Location location = gps_service.getLocation();

        if(location!=null){
            c_lat = location.getLatitude();
            c_lng = location.getLongitude();
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            if(c_lat != 0){

            }
        }

        getContext().registerReceiver(broadcastReceiver, new IntentFilter(FCMIdService.TOKEN_BROADCAST));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class FetchDataTask extends AsyncTask<Void, Void, Void>{
        private String mRemoteString;
        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection =null;
            BufferedReader reader = null;
            Uri uri = Uri.parse(getString(R.string.restaurant_list_api));
            URL url;
            try{
                url = new URL(uri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream==null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                }

                if(buffer.length()==0){
                    return null;
                }

                mRemoteString = buffer.toString();
//                JSONObject jsonObject = new JSONObject(mRemoteString);
                JSONArray restaurantsArray = new JSONArray(mRemoteString);
                Log.v("Response", restaurantsArray.toString());


                for (int i = 0; i < restaurantsArray.length(); i++){
                    String r_Name;
                    String r_Address;
                    String r_Phone;
                    String r_County;
                    String r_Area;
                    String r_DetailAddress;
                    String r_id;
                    String imgPath;
                    double distance;
                    double lat, lng;

//                    JSONObject jRestaurant = (JSONObject)jsonObject.(i);
                    r_Name = restaurantsArray.getJSONObject(i).getString("R_Name");

                    r_County = restaurantsArray.getJSONObject(i).getString("R_County");
                    r_Area = restaurantsArray.getJSONObject(i).getString("R_Area");
                    r_DetailAddress = restaurantsArray.getJSONObject(i).getString("R_DetailAddress");
                    r_Address = r_County+r_Area+r_DetailAddress;
                    r_Phone = restaurantsArray.getJSONObject(i).getString("R_PhoneNum");
                    r_id = restaurantsArray.getJSONObject(i).getString("Id");
                    lat = restaurantsArray.getJSONObject(i).getDouble("Lat");
                    lng = restaurantsArray.getJSONObject(i).getDouble("Lng");
                    distance = gps_service.getDistancBetweenTwoPoints(lat, lng, c_lat, c_lng);
                    if(!restaurantsArray.getJSONObject(i).getString("ImagePath").isEmpty()){
                        imgPath = restaurantsArray.getJSONObject(i).getString("ImagePath");

                    }
                    else
                        imgPath = "";

                    Restaurant restaurant = new Restaurant();
                    restaurant.setR_Name(r_Name);
                    restaurant.setR_Address(r_Address);
                    restaurant.setR_Phone(r_Phone);
                    restaurant.setR_id(r_id);
                    restaurant.setR_lat(lat);
                    restaurant.setR_lng(lng);
                    DecimalFormat df = new DecimalFormat("##.00");

                    restaurant.setDistance(Double.parseDouble(df.format(distance/1000)));
                    restaurant.setR_imgPath("http://pccu-eating.azurewebsites.net"+imgPath);

                    mRestaurantCollection.add(restaurant);
                }


            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MainActivity", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRestaurantAdapter.notifyDataSetChanged();
        }
    }
}
