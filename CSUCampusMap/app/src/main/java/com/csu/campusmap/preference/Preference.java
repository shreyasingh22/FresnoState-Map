package com.csu.campusmap.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

	private static final String FILE_NAME = "Campusmap.pref";

	private static Preference mInstance = null;

	public static Preference getInstance() {
		if (null == mInstance) {
			mInstance = new Preference();
		}
		return mInstance;
	}


	public void put(Context context, String key, String value) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void put(Context context, String key, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void put(Context context, String key, int value) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void put(Context context, String key, long value) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public String get(Context context, String key,
			String defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		return pref.getString(key, defaultValue);
	}

	public int get(Context context, String key, int defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		return pref.getInt(key, defaultValue);
	}

	public long get(Context context, String key, long defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		return pref.getLong(key, defaultValue);
	}

	public boolean get(Context context, String key,
			boolean defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		return pref.getBoolean(key, defaultValue);
	}

	public void putMyLat(Context context, double lat){
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(PrefConst.PREFKEY_LAT, String.valueOf(lat));
		editor.commit();
	}
	public void putMyLon(Context context, double lon){
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(PrefConst.PREFKEY_LON, String.valueOf(lon));
		editor.commit();
	}

	public double getMyLat(Context context){
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);

		return Double.valueOf(pref.getString(PrefConst.PREFKEY_LAT, "0"));
	}

	public double getMyLon(Context context){
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);

		return Double.valueOf(pref.getString(PrefConst.PREFKEY_LON, "0"));
	}

	public void updateMyPosition(Context context, double lat, double lon){
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(PrefConst.PREFKEY_LAT);
		editor.remove(PrefConst.PREFKEY_LON);
		putMyLat(context, lat);
		putMyLon(context, lon);
	}

}
