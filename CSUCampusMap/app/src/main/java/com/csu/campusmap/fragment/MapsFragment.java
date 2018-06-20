package com.csu.campusmap.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csu.campusmap.Base.BaseFragment;
import com.csu.campusmap.Commons.Commons;
import com.csu.campusmap.Main.MenuActivity;
import com.csu.campusmap.Model.Data;
import com.csu.campusmap.R;
import com.csu.campusmap.Util.Constant;
import com.csu.campusmap.Util.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsFragment extends BaseFragment {

    public static GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private SearchBox ui_searchBox;
    private ImageView ui_imvDirection;

    private Marker myMarker, otherMaker, marker;

    private LatLng myPosition, targetPosition;


    private String mDuration, mDistance, mCar, mWalk;

    private static int mCurrentMapMode;



    private MenuActivity mMenuActivity;

    private ArrayList<LatLng> mMarkerPoints = new ArrayList<LatLng>();

    private static View view;

    public static Fragment newInstance(){
        return new MapsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null){

            ViewGroup parent = (ViewGroup)view.getParent();
            if (parent != null){
                parent.removeView(view);
            }

            mMenuActivity = ((MenuActivity)getActivity());

            Bundle bundle = this.getArguments();

            navigate(bundle);
        }
        try {

            view = inflater.inflate(R.layout.activity_maps, container, false);

            mMenuActivity = ((MenuActivity)getActivity());

            ((MenuActivity)getActivity()).ui_toolbar.setVisibility(View.GONE);
            ui_searchBox = (SearchBox)view.findViewById(R.id.searchbox);
            ui_imvDirection = (ImageView)view.findViewById(R.id.imv_direction);
            ui_imvDirection.setVisibility(View.INVISIBLE);

            setUpSearchBox();

            mMap = ((MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
//        mMap.addMarker(new MarkerOptions().position(new LatLng(36.8133, -119.7500)).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.8133, -119.7500), 17));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);


            addMyPosition();

            addSearchAbles();

            Bundle bundle = this.getArguments();

            navigate(bundle);

            ui_imvDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMarkerPoints.clear();

//                addMyPosition();
//                addSearchAbles();

                    mMarkerPoints.add(new LatLng(36.8133, -119.7500));

                    if (targetPosition != null) {
                        mMarkerPoints.add(targetPosition);
                    }

                    if (targetPosition != null){

                        String url = getDirectionsUrl(new LatLng(36.8133, -119.7500), targetPosition);
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
                        Log.d("URL =========>", url);

                    }
                }
            });

        }catch (InflateException e){

        }

        return view;
    }

    public void navigate(Bundle bundle){
        if (bundle != null){

            int type = bundle.getInt(Constant.TYPE_KEY, 0);
            switch (type){

                case Constant.TYPE_DINNIG:

                    drawLocations(((MenuActivity)getActivity()).mDinnings);

                    break;
                case Constant.TYPE_FOOD:
                    LatLng foodLocation = new LatLng(36.812582, -119.748441);
                    drawLocation(foodLocation, "Food Court" );
                    break;
                case Constant.TYPE_PACKING:

                    break;
                case Constant.TYPE_GYM:
                    drawLocations(mMenuActivity.mGyms);
                    break;
                case Constant.TYPE_AQU:
                    drawLocation("Aquatics Center");
                    break;
                case Constant.TYPE_SATELLATE:
                    if (mCurrentMapMode != GoogleMap.MAP_TYPE_SATELLITE){
                        changeMapMode(2);
                    }else {

                        changeMapMode(1);
                        mCurrentMapMode = GoogleMap.MAP_TYPE_NORMAL;
                    }

                    break;
                case Constant.TYPE_YELLOW:
                    drawLocations(((MenuActivity)getActivity()).mYellows);
                    break;
                case Constant.Type_GREEN:
                    drawLocations(mMenuActivity.mGreens);
                    break;
                case Constant.TYPE_THIRTY:
                    drawLocations(mMenuActivity.mThhirtys);
                    break;
                case Constant.TYPE_METE:
                    mMenuActivity.showToast(String.valueOf(mMenuActivity.mMeters.size()));
                            drawLocations(mMenuActivity.mMeters);
                    break;
            }
        }

    }


    public void setUpSearchBox(){

        ui_searchBox.enableVoiceRecognition(this);

//        /** Search Box drawer  */
//        ui_searchBox.setMenuListener(new SearchBox.MenuListener() {
//            @Override
//            public void onMenuClick() {
//                ((MenuActivity) getActivity()).ui_drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });

        ui_searchBox.setLogoText("Search");

        /** Search Box Crosshair*/
        ui_searchBox.crosshair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        ui_searchBox.setSearchListener(new SearchBox.SearchListener() {
            @Override
            public void onSearchOpened() {

            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchClosed() {

            }

            @Override
            public void onSearchTermChanged(String term) {

            }

            @Override
            public void onSearch(String result) {

            }

            @Override
            public void onResultClick(SearchResult result) {
                String name = result.toString();
                LatLng latLng = getLocation(name);
                targetPosition = latLng;
                ui_imvDirection.setVisibility(View.VISIBLE);

                mMap.clear();

                addMyPosition();

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(targetPosition);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapmarker));
                markerOptions.title(name);

                //mMap.addMarker(new MarkerOptions().position(getLocation(((MenuActivity)getActivity()).mDatas.get(i).get_name())));
                otherMaker = mMap.addMarker(markerOptions);
                otherMaker.showInfoWindow();

                Log.d("targetPosition Lat====>", String.valueOf(targetPosition.latitude));
                Log.d("targetPosition Lng====>", String.valueOf(targetPosition.longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        });

    }

    public void drawLocations(ArrayList<Data> locations){

        mMap.clear();
        for (int i = 0 ; i < locations.size() ; i ++){

            MarkerOptions opt = new MarkerOptions();
            opt.position(new LatLng(locations.get(i).get_lat(), locations.get(i).get_lon()));
            opt.title(locations.get(i).get_name());
            opt.icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapmarker));

            marker = mMap.addMarker(opt);
            marker.showInfoWindow();
        }
    }

    public void drawLocation(String name){

        mMap.clear();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(getLocation(name));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapmarker));
        markerOptions.title(name);

        //mMap.addMarker(new MarkerOptions().position(getLocation(((MenuActivity)getActivity()).mDatas.get(i).get_name())));
        otherMaker = mMap.addMarker(markerOptions);
        otherMaker.showInfoWindow();
    }

    public void drawLocation(LatLng latLng, String name){

        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapmarker));
        markerOptions.title(name);

        //mMap.addMarker(new MarkerOptions().position(getLocation(((MenuActivity)getActivity()).mDatas.get(i).get_name())));
        otherMaker = mMap.addMarker(markerOptions);
        otherMaker.showInfoWindow();

    }
    private void addMyPosition(){

        MarkerOptions opt = new MarkerOptions();
        opt.position(new LatLng(36.8133, -119.7500));
        opt.title("ME");
        opt.snippet("I am here");
        opt.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        myMarker = mMap.addMarker(opt);
        myMarker.showInfoWindow();
    }



    private void addSearchAbles(){
        /** Search Box Add searchables */
        for (int i = 0 ; i< ((MenuActivity)getActivity()).mDatas.size() ; i ++ ){

            SearchResult item = new SearchResult(((MenuActivity)getActivity()).mDatas.get(i).get_name() , getResources().getDrawable(R.drawable.ic_history));
            ui_searchBox.addSearchable(item);


        }
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    }

    public LatLng getLocation( String name){

        LatLng latLng = null;
        ArrayList<Data> datas = Commons.g_MenuActivity.mDatas;
        for (int i = 0 ; i < datas.size(); i ++){

            if (datas.get(i).get_name().contains(name)){

                latLng = new LatLng(datas.get(i).get_lat(), datas.get(i).get_lon());
            }
        }
        return latLng;
    }

    public static  void changeMapMode(int mapMode){
        if (mMap != null){
            switch (mapMode){
                case 0:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    break;
                case 1:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case 2:
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mCurrentMapMode = GoogleMap.MAP_TYPE_SATELLITE;
                    break;
                case 3:
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case 4:
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        }
    }

    private void Showdialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Distace and Duration");
        View dialog = getActivity().getLayoutInflater().inflate(R.layout.alert, null);


        TextView ui_txvDistance = (TextView)dialog.findViewById(R.id.txv_distance);
        TextView ui_txvCar = (TextView)dialog.findViewById(R.id.txv_car);
        TextView ui_txvWalk = (TextView)dialog.findViewById(R.id.txv_walk);

        builder.setView(dialog);

        ui_txvCar.setText(mDuration);
        ui_txvDistance.setText(mDistance);
        ui_txvWalk.setText(mWalk);

        final AlertDialog dialogView = builder.create();
        dialogView.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        if (fragment!= null){
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false&units=metric&mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        Log.d("Url====>", url);
        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            // Log.d("Exception while downloading url", e.toString());
            e.printStackTrace();

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;


            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if (j == 0){ // Get distance from the list

                        mDistance = (String)point.get("distance");
                        Log.d("Distance=========>", mDistance);
                        continue;
                    }else if (j == 1){

                        mDuration = (String)point.get("duration");
                        Log.d("Duration============>", mDuration);
                        mWalk = "3.5min";

                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    Log.d("Lat====>", String.valueOf(lat));
                    Log.d("Lon====>", String.valueOf(lng));

                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.add(new LatLng(36.8133, -119.7500));
                lineOptions.addAll(points);
                lineOptions.add(targetPosition);
                lineOptions.width(11);
                lineOptions.color(Color.RED);

                Showdialog();
                ui_imvDirection.setVisibility(View.INVISIBLE);
            }

            try{
                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
