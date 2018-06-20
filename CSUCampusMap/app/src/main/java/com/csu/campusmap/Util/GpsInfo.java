package com.csu.campusmap.Util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.csu.campusmap.preference.Preference;

import java.util.Timer;

public class GpsInfo extends Service {

    public Double myLat, myLon;

    protected LocationManager locationManager;

    private boolean isGpsReceived;

    public Location receivedLocation;

    final Handler mHandler = new Handler();
    Timer mTimer = new Timer();

    Location lastKnownLocation;

    private static long gpsGenTime;

    @Override
    public void onCreate() {
        super.onCreate();

        isGpsReceived = false;
        try{
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constant.MIN_TIME_MY_UPDATE, Constant.MIN_DISTANCE_UPDATE, netListener );

            if (locationManager != null){

                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constant.MIN_TIME_MY_UPDATE, Constant.MIN_DISTANCE_UPDATE, gpsListener);

            if (locationManager != null && lastKnownLocation == null){
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (lastKnownLocation != null){

            Preference.getInstance().putMyLat(GpsInfo.this, lastKnownLocation.getLatitude());
            Preference.getInstance().putMyLon(GpsInfo.this, lastKnownLocation.getLongitude());
        }

        /** For GPS Provider */
        try{
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (locationManager != null){
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (lastKnownLocation != null){

            Preference.getInstance().putMyLat(GpsInfo.this, lastKnownLocation.getLatitude());
            Preference.getInstance().putMyLon(GpsInfo.this, lastKnownLocation.getLongitude());
        }

    }

    public GpsInfo() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("GPS INFO SERVICE", "SERVEICE START TO RUN");

        return super.onStartCommand(intent, flags, startId);
    }

    public void updatewithNewLocation(Location location, String provider){

        Log.d("Current Provider =============>", "==========" + provider + "==========");


        if (isGpsReceived){

            if (LocationManager.GPS_PROVIDER.equals(provider)){

                receivedLocation = location;
                //
            }else {
                try{
                    gpsGenTime = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getTime(); // last received time from GPS

                }catch (RuntimeException e){
                    e.printStackTrace();
                }

                long curTime = System.currentTimeMillis();

                if ((curTime - gpsGenTime)> 10000){
                    receivedLocation = location;
                    Preference.getInstance().updateMyPosition(GpsInfo.this, receivedLocation.getLatitude(), receivedLocation.getLongitude());
                    isGpsReceived = false;
                }
            }
        }else {

            receivedLocation = location; //Network Provider Locational Information
            Preference.getInstance().updateMyPosition(GpsInfo.this, receivedLocation.getLatitude(), receivedLocation.getLongitude());

        }
    }

    LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            isGpsReceived = true;
            updatewithNewLocation(location, LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    LocationListener netListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            updatewithNewLocation(location, LocationManager.NETWORK_PROVIDER);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
