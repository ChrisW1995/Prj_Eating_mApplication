package com.example.chriswang.prj_eating2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.HttpClientStack;
import com.baoyz.widget.PullRefreshLayout;
import com.example.chriswang.prj_eating2.adapters.RestaurantAdapter;
import com.example.chriswang.prj_eating2.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
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
        mRestaurantRecyclerView = (RecyclerView)v.findViewById(R.id.restaurant_recycler);
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
        init(v);
        new FetchDataTask().execute();
        pullRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init(v);
                new FetchDataTask().execute();
                pullRefreshLayout.setRefreshing(false);
                
            }
        });

        return v;
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
            Toast.makeText(context, "List Fragment", Toast.LENGTH_SHORT).show();
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

//                    JSONObject jRestaurant = (JSONObject)jsonObject.(i);
                    r_Name = restaurantsArray.getJSONObject(i).getString("R_Name");

                    r_County = restaurantsArray.getJSONObject(i).getString("R_County");
                    r_Area = restaurantsArray.getJSONObject(i).getString("R_Area");
                    r_DetailAddress = restaurantsArray.getJSONObject(i).getString("R_DetailAddress");
                    r_Address = r_County+r_Area+r_DetailAddress;
                    r_Phone = restaurantsArray.getJSONObject(i).getString("R_PhoneNum");
                    r_id = restaurantsArray.getJSONObject(i).getString("Id");
                    Restaurant restaurant = new Restaurant();
                    restaurant.setR_Name(r_Name);
                    restaurant.setR_Address(r_Address);
                    restaurant.setR_Phone(r_Phone);
                    restaurant.setR_id(r_id);

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
