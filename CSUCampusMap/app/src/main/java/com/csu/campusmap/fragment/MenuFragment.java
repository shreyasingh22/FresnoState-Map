package com.csu.campusmap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.csu.campusmap.Base.BaseFragment;
import com.csu.campusmap.Main.MenuActivity;
import com.csu.campusmap.R;


public class MenuFragment extends BaseFragment implements View.OnClickListener {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button ui_btnNear = (Button)view.findViewById(R.id.btn_near);
        Button ui_btnTake = (Button)view.findViewById(R.id.btn_take);

        ui_btnTake.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_near:
                break;
            case R.id.btn_take:
                ((MenuActivity)getActivity()).navigate(2);
                break;
        }
    }
}
