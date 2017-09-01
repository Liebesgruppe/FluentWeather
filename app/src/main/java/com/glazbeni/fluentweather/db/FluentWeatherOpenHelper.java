package com.glazbeni.fluentweather.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 29044 on 17.8.22.
 */

public class FluentWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * Province表建表语句
     */
    public static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement,"
            + "province_name text,"
            + "province_code)";

    /**
     * City表建表语句
     */
    public static final String CREATE_CITY = "create table CityX ("
            + "id integer primary key autoincrement,"
            + "city_name text,"
            + "city_code text,"
            + "province_id integer)";

    /**
     * Country表建表语句
     */
    public static final String CREATE_COUNTRY = "create table Country ("
            + "id integer primary key autoincrement,"
            + "country_name text,"
            + "country_code text"
            + "city_id integer)";

    public FluentWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROVINCE); //创建Province表
        sqLiteDatabase.execSQL(CREATE_CITY); //创建City表
        sqLiteDatabase.execSQL(CREATE_COUNTRY); //创建Country表
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
