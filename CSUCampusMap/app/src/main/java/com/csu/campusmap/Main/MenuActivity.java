package com.csu.campusmap.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.csu.campusmap.Adapter.LeftNavAdapter;
import com.csu.campusmap.Base.BaseActivity;
import com.csu.campusmap.Commons.Commons;
import com.csu.campusmap.Model.Data;
import com.csu.campusmap.R;
import com.csu.campusmap.Util.Constant;
import com.csu.campusmap.Util.DataBase;
import com.csu.campusmap.Util.GpsInfo;
import com.csu.campusmap.fragment.MapsFragment;
import com.csu.campusmap.fragment.MenuFragment;
import com.csu.campusmap.fragment.PackingFragment;
import com.quinny898.library.persistentsearch.SearchBox;

import java.util.ArrayList;

public class MenuActivity extends BaseActivity {

    public DrawerLayout ui_drawerLayout;

    private ListView ui_drawerLeft;

    private LeftNavAdapter adapter;

    private ActionBarDrawerToggle mDrawerToggle;

    public Toolbar ui_toolbar;

    SearchBox ui_searchBox;
    public ArrayList<Data> mDatas;
    public ArrayList<Data> mYellows;
    public ArrayList<Data> mGreens;
    public ArrayList<Data> mMeters;
    public ArrayList<Data> mThhirtys;
    public ArrayList<Data> mMotors;
    public ArrayList<Data> mGyms;
    public ArrayList<Data> mDinnings;

    boolean isRead = false;

    DataBase dataBase = null;

    private LocationManager locationManager;
    private static boolean isGPSEnable;
    private static boolean isNetworkEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        Commons.g_MenuActivity = this;

        setupDrawer();

        navigate(9);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable){
            showSettingAlert();
        }else {
            Intent intent = new Intent(MenuActivity.this, GpsInfo.class);
            startService(intent);
        }

        /**  Read data from DB*/
        loadData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ui_drawerLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        navigate(position);

                    }
                });
            }
        }, Constant.DRAWER_DELAY_TIME);


    }

    public void loadData(){

        dataBase = new DataBase(this);
        dataBase.createDataBase();
        dataBase.openDataBase();
        mDatas = dataBase.getAllData();
        mYellows = dataBase.getAllYellow();
        mGreens = dataBase.getAllGreen();
        mGyms = dataBase.getAllGym();
        mMeters = dataBase.getAllMeta();
        mMotors = dataBase.getAllmotor();
        mThhirtys = dataBase.getAllThirty();

        mDinnings = new ArrayList<>();
        Data dinning1  = new Data(36.81125605 ,-119.7510463 ,"University Dining Hall");
        Data dinning2 = new Data(36.81139778 ,-119.7509551 ,"University Dining Hall (RDF)");
        mDinnings.add(dinning1);
        mDinnings.add(dinning2);

    }

    public void navigate(int id){
        Fragment fragment  = null;
        Class fragmentClass = null;

        try{
            Bundle bundle = new Bundle();

            switch (id){
                case 0: // Dinning

                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_DINNIG);
                    fragment.setArguments(bundle);

                    break;
                case 1: // Parking

                    fragmentClass = PackingFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();

                    break;
                case 2:// FoodCourt

                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_FOOD);
                    fragment.setArguments(bundle);

                    break;
                case 3: // GYM

                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_GYM);
                    fragment.setArguments(bundle);
                    break;


                case 4: // AQu

                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_AQU);
                    fragment.setArguments(bundle);

                    break;

                case 5: // Satellate

                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_SATELLATE);
                    fragment.setArguments(bundle);

                    break;
                case 9: // MainMenu
                    fragmentClass = MenuFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();

                    break;
                case 10: // motor
                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_MOTOR);
                    fragment.setArguments(bundle);
                    break;
                case 11: // thirty
                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_THIRTY);
                    fragment.setArguments(bundle);
                    break;
                case 12: // Green Packing
                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_GREEN);
                    fragment.setArguments(bundle);
                    break;
                case 13: // Yellow Packing
                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_YELLOW);
                    fragment.setArguments(bundle);
                    break;
                case 14: // Meta
                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_METE);
                    fragment.setArguments(bundle);
                    break;
                case 15: //
                    fragmentClass = MapsFragment.class;
                    fragment = (Fragment)fragmentClass.newInstance();
                    bundle.putInt(Constant.TYPE_KEY, Constant.TYPE_DINNIG);
                    fragment.setArguments(bundle);
                    break;

            }


        }catch (Exception e){
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    private void setupDrawer(){

        ui_toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(ui_toolbar);
        ui_drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, ui_drawerLayout, ui_toolbar, R.string.open, R.string.close );
        ui_drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        ui_drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        setupLeftDrawer();
    }

    private void setupLeftDrawer(){

        ui_drawerLeft = (ListView)findViewById(R.id.left_drawer);

        adapter = new LeftNavAdapter(this, getResources().getStringArray(R.array.arr_left_nav_list));

        ui_drawerLeft.setAdapter(adapter);

        ui_drawerLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void showSettingAlert(){
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle(getString(R.string.gps_setting));
        alertDlg.setMessage(getString(R.string.enable_gps));
        alertDlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MenuActivity.this.startActivity(intent);
            }
        });
        alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDlg.show();
    }

    @Override
    public void onBackPressed() {

        onExit();
    }
}
