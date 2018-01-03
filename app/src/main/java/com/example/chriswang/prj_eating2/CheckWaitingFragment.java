package com.example.chriswang.prj_eating2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.chriswang.prj_eating2.Service.FetchCustomerCoupons;
import com.example.chriswang.prj_eating2.Service.FetchWaitingList;
import com.example.chriswang.prj_eating2.adapters.CouponAdapter;
import com.example.chriswang.prj_eating2.adapters.WaitingAdapter;
import com.example.chriswang.prj_eating2.model.Coupon;
import com.example.chriswang.prj_eating2.model.WaitList;

import java.util.ArrayList;


public class CheckWaitingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FetchWaitingList fetchWaitingList;
    private ArrayList<WaitList> mWaits;
    private WaitingAdapter mCouponAdapter;
    private RecyclerView mWaitRecycler;
    private PullRefreshLayout pullRefreshLayout;

    public CheckWaitingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CheckWaitingFragment newInstance(String param1, String param2) {
        CheckWaitingFragment fragment = new CheckWaitingFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_check_waiting, container, false);
        pullRefreshLayout = view.findViewById(R.id.swipeRefreshWait);
        if(isAdded()){

            refresh(view);

            pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh(view);
                    pullRefreshLayout.setRefreshing(false);
                }
            });
        }
        return view;
    }

    public void refresh(View view){

        mWaits = new ArrayList<>();
        mCouponAdapter = new WaitingAdapter(mWaits, getActivity());
        mWaitRecycler = view.findViewById(R.id.check_wait_recycler);
        fetchWaitingList = new FetchWaitingList(getContext(), getActivity());
        Object[] objects = new Object[3];
        objects[0] = mWaits;
        objects[1]= mCouponAdapter;
        objects[2] = mWaitRecycler;
        fetchWaitingList.execute(objects);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
