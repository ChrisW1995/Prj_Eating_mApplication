package com.example.chriswang.prj_eating2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.chriswang.prj_eating2.Service.SharedPrefManager;
import com.example.chriswang.prj_eating2.adapters.FeedbackAdapter;
import com.example.chriswang.prj_eating2.adapters.MenuAdapter;
import com.example.chriswang.prj_eating2.model.Feedback;
import com.example.chriswang.prj_eating2.model.Menu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MenuFragment extends Fragment implements InfoFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String r_id;
    private FetchMenuTask menuTask;
    private ArrayList<Menu> mMenu;
    private MenuAdapter mMenuAdapter;
    private RecyclerView mMenuRecycler;
    private SharedPrefManager sharedPrefManager;
    private float score;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(menuTask!=null){
            menuTask.cancel(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void init(View view){
        mMenuRecycler = view.findViewById(R.id.menu_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mMenuRecycler.setLayoutManager(layoutManager);
        mMenuRecycler.setItemAnimator(new DefaultItemAnimator());

        mMenu = new ArrayList<>();
        mMenuAdapter = new MenuAdapter(mMenu, getActivity());
        mMenuRecycler.setAdapter(mMenuAdapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        if(isAdded()){
            init(v);
            menuTask = new FetchMenuTask();
            menuTask.execute();
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onFragmentInteraction(String r_id) {
        this.r_id = r_id;
    }

    public class FetchMenuTask extends AsyncTask<Void, Void, Void> {
        private String mRemoteString;

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri uri = Uri.parse(getString(R.string.get_menu_api) + r_id);
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
                JSONArray menuArray = new JSONArray(mRemoteString);
                Log.v("menu response", menuArray.toString());

                for (int i = 0; i < menuArray.length(); i++) {
                    if(isCancelled()){
                        menuTask=null;

                    }
                    String menu_name;
                    String menu_path;

                    menu_name = menuArray.getJSONObject(i).getString("FoodName");
                    menu_path = menuArray.getJSONObject(i).getString("ImgPath");
                    menu_path = "http://cw30cmweb.com" + menu_path;

                    Menu menu = new Menu();
                    menu.setName(menu_name);
                    menu.setPath(menu_path);

                    mMenu.add(menu);
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
                        Log.e("GetMenu", "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mMenuAdapter.notifyDataSetChanged();
        }

    }
}
