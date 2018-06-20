package com.csu.campusmap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.csu.campusmap.Base.BaseFragment;
import com.csu.campusmap.Main.MenuActivity;
import com.csu.campusmap.R;


public class PackingFragment extends BaseFragment implements View.OnClickListener {

    public PackingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_packing, container, false);

        ((MenuActivity)getActivity()).ui_toolbar.setVisibility(View.GONE);

        Button ui_btnYellow = (Button)view.findViewById(R.id.btn_yellow);
        ui_btnYellow.setOnClickListener(this);
        Button ui_btnGreen = (Button)view.findViewById(R.id.btn_green);
        ui_btnGreen.setOnClickListener(this);
        Button ui_btnMetered = (Button)view.findViewById(R.id.btn_metered);
        ui_btnMetered.setOnClickListener(this);
        Button ui_btnThird = (Button)view.findViewById(R.id.btn_thirty);
        ui_btnThird.setOnClickListener(this);


        if ( ((MenuActivity)getActivity()).ui_toolbar.getVisibility() == View.INVISIBLE){
            ((MenuActivity)getActivity()).ui_toolbar.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_yellow:
                ((MenuActivity)getActivity()).navigate(13);
                break;
            case R.id.btn_green:
                ((MenuActivity)getActivity()).navigate(12);
                break;
            case R.id.btn_metered:
                ((MenuActivity)getActivity()).navigate(14);
                break;
            case R.id.btn_thirty:
                ((MenuActivity)getActivity()).navigate(11);
                break;
        }
    }
}
