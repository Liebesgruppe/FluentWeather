package com.glazbeni.fluentweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.glazbeni.fluentweather.model.Country;
import com.glazbeni.fluentweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 29044 on 17.8.22.
 */

public class FluentWeatherDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "fluent_weather";

    /**
     * 数据库版本
     */
    public static final int DB_VERSION = 1;

    private static FluentWeatherDB fluentWeatherDB;
    private SQLiteDatabase sqLiteDatabase;

    /**
     * 将构造方法私有化
     */
    private FluentWeatherDB(Context context) {
        FluentWeatherOpenHelper dbHelper = new FluentWeatherOpenHelper(context, DB_NAME, null, DB_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    /**
     * 获取FluentWeather的实例
     */
    public synchronized static FluentWeatherDB getInstance(Context context) {
        if (fluentWeatherDB == null) {
            fluentWeatherDB = new FluentWeatherDB(context);
        }
        return fluentWeatherDB;
    }

    /**
     * 将Province实例存储到数据库中
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            sqLiteDatabase.insert("Province", null, values);
        }
    }

    /**
     * 从数据库中读取所有省份的信息
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList = new ArrayList<Province>();
        Cursor cursor = sqLiteDatabase.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return provinceList;
    }

    /**
     * 将City实例存储到数据库
     */
//    public void saveCity(CityX cityH) {
//        if (cityH == null) {
//            ContentValues values = new ContentValues();
//            values.put("city_name", cityH.getCityName());
//            values.put("city_code", cityH.getCityCode());
//            values.put("province_id", cityH.getId());
//            sqLiteDatabase.insert("CityX", null, values);
//        }
//    }

    /**
     * 从数据库读取某省份所有城市的信息
     */
//    public List<CityX> loadCities() {
//        List<CityX> cityHList = new ArrayList<CityX>();
//        Cursor cursor = sqLiteDatabase.query("CityX", null, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                CityX cityH = new CityX();
//                cityH.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
//                cityH.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
//                cityH.setId(cursor.getInt(cursor.getColumnIndex("province_id")));
//                cityHList.add(cityH);
//            } while (cursor.moveToNext());
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//        return cityHList;
//    }

    /**
     * 将Country的实例保存到数据库中
     */
    public void saveCountry(Country country) {
        if (country == null) {
            ContentValues values = new ContentValues();
            values.put("country_name", country.getCountryName());
            values.put("country_code", country.getCountryCode());
            values.put("city_id", country.getId());
            sqLiteDatabase.insert("Country", null, values);
        }
    }

    /**
     * 从数据库中读取某城市下所有县信息
     */
    public List<Country> loadCountries() {
        List<Country> countryList = new ArrayList<Country>();
        Cursor cursor = sqLiteDatabase.query("Country", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                countryList.add(country);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return countryList;
    }
}
