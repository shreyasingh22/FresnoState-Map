package com.csu.campusmap.Base;

import android.support.v4.app.Fragment;
import android.widget.Toast;


public class BaseFragment extends Fragment {

    public BaseActivity _context;


    // show progress
    public void showProgress(){
        _context.showProgress();
    }

    // kill the progress
    public void closeProgress(){
        _context.closeProgress();
    }

    public void showToast( String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

}
