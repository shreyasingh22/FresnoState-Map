package com.csu.campusmap.Model;

/**
 * Created by Ung Man Pak on 3/5/2016.
 */
public class Data {

    String _name;
    double _lat;
    double _lon;


    public Data(){

    }

    public Data( double _lat, double _lon , String _name){

        this._lat = _lat;
        this._lon = _lon;
        this._name = _name;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public double get_lat() {
        return _lat;
    }

    public void set_lat(double _lat) {
        this._lat = _lat;
    }

    public double get_lon() {
        return _lon;
    }

    public void set_lon(double _lon) {
        this._lon = _lon;
    }
}
