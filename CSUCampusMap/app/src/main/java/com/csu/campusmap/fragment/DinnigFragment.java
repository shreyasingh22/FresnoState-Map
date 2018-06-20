package com.csu.campusmap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.csu.campusmap.Base.BaseFragment;
import com.csu.campusmap.Main.MenuActivity;
import com.csu.campusmap.R;


public class DinnigFragment extends BaseFragment implements View.OnClickListener{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dinnig, container, false);


        ((MenuActivity)getActivity()).ui_toolbar.setVisibility(View.VISIBLE);

        Button ui_btnLocation = (Button)view.findViewById(R.id.btn_location);
        Button ui_btnMenu = (Button)view.findViewById(R.id.btn_menu);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_location:
                break;
            case R.id.btn_menu:
                break;
        }
    }
}
