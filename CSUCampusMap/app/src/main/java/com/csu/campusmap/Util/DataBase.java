package com.csu.campusmap.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.csu.campusmap.Model.Data;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Ung Man Pak on 3/6/2016.
 */
public class DataBase extends SQLiteOpenHelper{

    static Context _context;
    static SQLiteDatabase database = null;
    static DataBase instance = null;

    private static final String DB_PATH = "/data/data/com.csu.campusmap/databases/";
    private static final String DATABASE_NAME = "DB.db";
    private static final  int DATABASE_VERSION = 2;

    public static final String DATA_TABLE = "tbl_data";
    public static final String YELLOW_TABLE = "tbl_yellow";
    public static final String GREEN_TABLE = "tbl_green";
    public static final String MOTOR_TABLE = "tbl_motor";
    public static final String THIRTY_TABLE = "tbl_thirty";
    public static final String GYM_TABLE = "tbl_gym";
    public static final String META_TABEL = "tbl_meta";

    public static final String COLUMN_DATA_ID = "_id";
    public static final String COLUMN_DATA_LAT = "lat";
    public static final String COLUMN_DATA_LON = "lon";
    public static final String COLUMN_DATA_NAME = "name";




    public static SQLiteDatabase getDatabase(){
        if (null == database){
            database = instance.getWritableDatabase();
        }
        return database;
    }

    public static void deactivate(){

        if (null != database && database.isOpen()){
            database.close();
        }
        database = null;
        instance = null;
    }

    public  DataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._context = context;
    }

    //create database
    public void createDataBase(){

        boolean isExist = isDatabaseExist();

        if (!isExist){
            this.getReadableDatabase();
            copyDataBase();
        }
    }

    public void openDataBase(){
        
        String myPath =  DB_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    //if database not exist copy form assets
    private void copyDataBase(){

        try{

            //Open your local db as the input stream
            InputStream myInput = _context.getAssets().open(DATABASE_NAME);

            //Path to the just created empty db
            String outFileName = DB_PATH + DATABASE_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0 ){
                myOutput.write(buffer, 0, length);
            }

            //Close the stream
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }catch (Exception e){}
    }

    // check if database is exist
    private boolean isDatabaseExist(){

        SQLiteDatabase db = null;

        try {
            String myPath = DB_PATH + DATABASE_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch (SQLiteException e){
            db = null;
        }

        if (db != null){
            db.close();
        }
        return db != null ? true : false;
    }



    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATA_TABLE + " ( "
                + COLUMN_DATA_ID + " INTEGER primary key autoincrement, "
                + COLUMN_DATA_LAT + " TEXT NOT NULL, "
                + COLUMN_DATA_LON + " TEXT NOT NULL, "
                + COLUMN_DATA_NAME + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + YELLOW_TABLE + " ( "
                + COLUMN_DATA_ID + " INTEGER primary key autoincrement, "
                + COLUMN_DATA_LAT + " TEXT NOT NULL, "
                + COLUMN_DATA_LON + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + GYM_TABLE + " ( "
                + COLUMN_DATA_ID + " INTEGER primary key autoincrement, "
                + COLUMN_DATA_LAT + " TEXT NOT NULL, "
                + COLUMN_DATA_LON + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + META_TABEL + " ( "
                + COLUMN_DATA_ID + " INTEGER primary key autoincrement, "
                + COLUMN_DATA_LAT + " TEXT NOT NULL, "
                + COLUMN_DATA_LON + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + THIRTY_TABLE + " ( "
                + COLUMN_DATA_ID + " INTEGER primary key autoincrement, "
                + COLUMN_DATA_LAT + " TEXT NOT NULL, "
                + COLUMN_DATA_LON + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + GREEN_TABLE + " ( "
                + COLUMN_DATA_ID + " INTEGER primary key autoincrement, "
                + COLUMN_DATA_LAT + " TEXT NOT NULL, "
                + COLUMN_DATA_LON + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + MOTOR_TABLE + " ( "
                + COLUMN_DATA_ID + " INTEGER primary key autoincrement, "
                + COLUMN_DATA_LAT + " TEXT NOT NULL, "
                + COLUMN_DATA_LON + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static ContentValues getContentDataValues(Data data){

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATA_LAT, String.valueOf(data.get_lat()));
        cv.put(COLUMN_DATA_LON, String.valueOf(data.get_lon()));
        cv.put(COLUMN_DATA_NAME, String.valueOf(data.get_name()));

        return cv;
    }


    public static ArrayList<Data> getAllData(){

        ArrayList<Data> datas = new ArrayList<Data>();

        String [] columns = new String[]{COLUMN_DATA_ID, COLUMN_DATA_LAT, COLUMN_DATA_LON, COLUMN_DATA_NAME};

        Cursor cursor = database.query(DATA_TABLE, columns, null, null, null, null, null);

        if (cursor.moveToFirst()){

            do {

                Data data = new Data(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), cursor.getString(3));
                datas.add(data);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return datas;
    }

    public static ArrayList<Data> getAllYellow(){

        ArrayList<Data> yellows = new ArrayList<>();

        String [] colums = new String[]{COLUMN_DATA_ID, COLUMN_DATA_LAT, COLUMN_DATA_LON};

        Cursor cursor = database.query(YELLOW_TABLE, colums, null, null, null, null, null);

        if (cursor.moveToFirst()){

            do {

                Data data = new Data(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), "Yellow Packing");


                yellows.add(data);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return yellows;
    }

    public static ArrayList<Data> getAllGreen(){

        ArrayList<Data> greens = new ArrayList<>();

        String [] colums = new String[]{COLUMN_DATA_ID, COLUMN_DATA_LAT, COLUMN_DATA_LON};

        Cursor cursor = database.query(GREEN_TABLE, colums, null, null, null, null, null);

        if (cursor.moveToFirst()){

            do {

                Data data = new Data(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), "Green Packing");


                greens.add(data);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return greens;
    }
    public static ArrayList<Data> getAllMeta(){

        ArrayList<Data> metas = new ArrayList<>();

        String [] colums = new String[]{COLUMN_DATA_ID, COLUMN_DATA_LAT, COLUMN_DATA_LON};

        Cursor cursor = database.query(META_TABEL, colums, null, null, null, null, null);

        if (cursor.moveToFirst()){

            do {

                Data data = new Data(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), "Metered Packing");


                metas.add(data);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return metas;
    }

    public static ArrayList<Data> getAllmotor(){

        ArrayList<Data> motors = new ArrayList<>();

        String [] colums = new String[]{COLUMN_DATA_ID, COLUMN_DATA_LAT, COLUMN_DATA_LON};

        Cursor cursor = database.query(MOTOR_TABLE, colums, null, null, null, null, null);

        if (cursor.moveToFirst()){

            do {

                Data data = new Data(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), "Motor Packing");


                motors.add(data);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return motors;
    }

    public static ArrayList<Data> getAllGym(){

        ArrayList<Data> gyms = new ArrayList<>();

        String [] colums = new String[]{COLUMN_DATA_ID, COLUMN_DATA_LAT, COLUMN_DATA_LON};

        Cursor cursor = database.query(GYM_TABLE, colums, null, null, null, null, null);

        if (cursor.moveToFirst()){

            do {

                Data data = new Data(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), "GYM");


                gyms.add(data);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return gyms;
    }

    public static ArrayList<Data> getAllThirty(){

        ArrayList<Data> thirtys = new ArrayList<>();

        String [] colums = new String[]{COLUMN_DATA_ID, COLUMN_DATA_LAT, COLUMN_DATA_LON};

        Cursor cursor = database.query(THIRTY_TABLE, colums, null, null, null, null, null);

        if (cursor.moveToFirst()){

            do {

                Data data = new Data(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), "Thirty Minutes Packing");


                thirtys.add(data);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return thirtys;
    }
}
