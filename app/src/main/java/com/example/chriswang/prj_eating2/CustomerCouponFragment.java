package com.example.chriswang.prj_eating2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chriswang.prj_eating2.Service.FetchCustomerCoupons;
import com.example.chriswang.prj_eating2.Service.FetchRestaurantCoupons;
import com.example.chriswang.prj_eating2.adapters.CouponAdapter;
import com.example.chriswang.prj_eating2.model.Coupon;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerCouponFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerCouponFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerCouponFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FetchCustomerCoupons customerCoupons;
    private ArrayList<Coupon> mCoupons;
    private CouponAdapter mCouponAdapter;
    private RecyclerView mCouponRecycler;
    private OnFragmentInteractionListener mListener;

    public CustomerCouponFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CustomerCouponFragment newInstance(String param1, String param2) {
        CustomerCouponFragment fragment = new CustomerCouponFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(customerCoupons!=null){
            customerCoupons.cancel(true);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_coupon, container, false);
        if(isAdded()){
            mCoupons = new ArrayList<>();
            mCouponAdapter = new CouponAdapter(mCoupons, getActivity(), getContext());
            mCouponRecycler = view.findViewById(R.id.customer_coupon_recycler);
            customerCoupons = new FetchCustomerCoupons(getContext(), getActivity());
            Object[] objects = new Object[3];
            objects[0] = mCoupons;
            objects[1]= mCouponAdapter;
            objects[2] = mCouponRecycler;
            customerCoupons.execute(objects);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
}
