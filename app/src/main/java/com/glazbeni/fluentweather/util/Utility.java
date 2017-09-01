package com.glazbeni.fluentweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.glazbeni.fluentweather.db.FluentWeatherDB;
import com.glazbeni.fluentweather.model.Country;
import com.glazbeni.fluentweather.model.Province;
import com.glazbeni.fluentweather.model.SevenWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 29044 on 17.8.22.
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(FluentWeatherDB fluentWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据存储到Province表中
                    fluentWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
//    public synchronized static boolean handleCitiesResponse(FluentWeatherDB fluentWeatherDB, String response, int provinceId) {
//        if (!TextUtils.isEmpty(response)) {
//            String[] allCities = response.split(",");
//            if (allCities != null && allCities.length > 0 ) {
//                for (String c : allCities) {
//                    String[] array = c.split("\\|");
//                    CityX cityH = new CityX();
//                    cityH.setCityName(array[1]);
//                    cityH.setCityCode(array[0]);
//                    cityH.setProvinceId(provinceId);
//                    //将解析出来的数据存储到City表
//                    fluentWeatherDB.saveCity(cityH);
//                }
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public synchronized static boolean handleCountriesResponse(FluentWeatherDB fluentWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountries = response.split(",");
            if (allCountries != null && allCountries.length > 0) {
                for (String c : allCountries) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(cityId);
                    //将解析出来的数据存储到Country表
                    fluentWeatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context, String respone) {
        if (respone != null) {
            try {
                JSONObject obj = new JSONObject(respone);
                JSONArray jsonArray = obj.getJSONArray("HeWeather5");
                JSONObject object = (JSONObject) jsonArray.get(0);

                JSONObject aqi = object.getJSONObject("aqi");
                JSONObject aqiCity = aqi.getJSONObject("city");
                String pm25 = aqiCity.getString("pm25");
                String qlty = aqiCity.getString("qlty");

                JSONObject basic = object.getJSONObject("basic");
                JSONObject update = basic.getJSONObject("update");
                String city = basic.getString("city");
                String id = basic.getString("id");
                String utc = update.getString("utc");

                JSONObject now = object.getJSONObject("now");
                JSONObject condNow = now.getJSONObject("cond");
                String txt = condNow.getString("txt");
                String temp = now.getString("tmp");

                JSONArray daily = object.getJSONArray("daily_forecast");
                List<SevenWeather> sevenWeatherlist = new ArrayList<SevenWeather>();
                Date df = new Date();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy:MM:dd");
                SimpleDateFormat ss = new SimpleDateFormat("M/dd");
                String M = null;
                String dd = null;
                String date = null;
                for (int i = 0; i < daily.length(); i++) {
                    SevenWeather sevenWeather = new SevenWeather();
                    JSONObject tmpObj = (JSONObject) daily.get(i);
                    JSONObject tmp = tmpObj.getJSONObject("tmp");
                    JSONObject cond = tmpObj.getJSONObject("cond");
                    M = tmpObj.getString("date").substring(6, 7);
                    dd = tmpObj.getString("date").substring(8);
                    date = M + "/" + dd;
                    sevenWeather.setMax(tmp.getString("max") + "°");
                    sevenWeather.setMin(tmp.getString("min") + "°");
                    sevenWeather.setDate(date);
                    sevenWeather.setWeather(cond.getString("txt_d"));
                    sevenWeatherlist.add(sevenWeather);
                }
                saveWeatherInfo(context, city, id, utc, pm25, qlty, txt, temp, sevenWeatherlist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "handleWeatherResponse: JSON数据为空");
        }
    }

    public static void saveWeatherInfo(Context context, String city,
                                       String id, String utc,
                                       String pm25, String qlty,
                                       String txt, String temp,
                                       List<SevenWeather> sevenWeathers) {
        SharedPreferences.Editor edtior = PreferenceManager.getDefaultSharedPreferences(context).edit();
        int size = sevenWeathers.size();
        edtior.putString("city", city);
        edtior.putString("id", id);
        edtior.putString("utc", utc);
        edtior.putString("pm25", pm25);
        edtior.putString("qlty", qlty);
        edtior.putString("txt", txt);
        edtior.putString("temp", temp);
        edtior.putInt("size", size);
        for (int i = 0; i < size; i++) {
            edtior.putString("max" + i, sevenWeathers.get(i).getMax());
            edtior.putString("min" + i, sevenWeathers.get(i).getMin());
            edtior.putString("date" + i, sevenWeathers.get(i).getDate());
            edtior.putString("weather" + i, sevenWeathers.get(i).getWeather());
        }
        edtior.commit();
    }
}
