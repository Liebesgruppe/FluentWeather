package com.glazbeni.fluentweather.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.glazbeni.fluentweather.model.CardWeatherBean;
import com.glazbeni.fluentweather.model.DailyForecastBean;
import com.glazbeni.fluentweather.model.WeatherBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 29044 on 17.8.22.
 */

public class Utility {

    /**
     * 进行定位
     * @param context 上下文
     * @return String类型的位置
     */
    public static String locate(Context context) {
        String district = null;
        AMapLocationClient mLocationClient = new AMapLocationClient(context.getApplicationContext());
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        AMapLocation amLocation = mLocationClient.getLastKnownLocation();
        district = amLocation.getDistrict();
        mLocationClient.stopLocation();
        Log.d(TAG, "locate: 正在定位");
        return district;
    }

    /**
     * 解析服务器返回的JSON数据
     * @param response 从服务器返回的JSON数据
     * @return WeatherBean 类型的数据
     */
    public static WeatherBean handleWeatherResponse(String response) {
        WeatherBean bean = new WeatherBean();
        if (response != null) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray jsonArray = obj.getJSONArray("HeWeather5");
                JSONObject object = (JSONObject) jsonArray.get(0);
                JSONObject suggestion = object.getJSONObject("suggestion");
                bean.setPm25(object.getJSONObject("aqi").getJSONObject("city").getString("pm25"));
                bean.setQlty(object.getJSONObject("aqi").getJSONObject("city").getString("qlty"));
                bean.setCity(object.getJSONObject("basic").getString("city"));
                bean.setId(object.getJSONObject("basic").getString("id"));
                bean.setUpdateTime(object.getJSONObject("basic").getJSONObject("update").getString("loc"));
                bean.setTxt(object.getJSONObject("now").getJSONObject("cond").getString("txt"));
                bean.setWeatherCode(object.getJSONObject("now").getJSONObject("cond").getString("code"));
                bean.setTemp(object.getJSONObject("now").getString("tmp"));
                bean.setAirBrf(suggestion.getJSONObject("air").getString("brf"));
                bean.setAirTxt(suggestion.getJSONObject("air").getString("txt"));
                bean.setCofmBrf(suggestion.getJSONObject("comf").getString("brf"));
                bean.setCofmTxt(suggestion.getJSONObject("comf").getString("txt"));
                bean.setCwBrf(suggestion.getJSONObject("cw").getString("brf"));
                bean.setCwTxt(suggestion.getJSONObject("cw").getString("txt"));
                bean.setDrsgBrf(suggestion.getJSONObject("drsg").getString("brf"));
                bean.setDrsgTxt(suggestion.getJSONObject("drsg").getString("txt"));
                bean.setFluBrf(suggestion.getJSONObject("flu").getString("brf"));
                bean.setFluTxt(suggestion.getJSONObject("flu").getString("txt"));
                bean.setSportBrf(suggestion.getJSONObject("sport").getString("brf"));
                bean.setSportTxt(suggestion.getJSONObject("sport").getString("txt"));
                bean.setTravBrf(suggestion.getJSONObject("trav").getString("brf"));
                bean.setTravTxt(suggestion.getJSONObject("trav").getString("txt"));
                bean.setUvBrf(suggestion.getJSONObject("uv").getString("brf"));
                bean.setUvTxt(suggestion.getJSONObject("uv").getString("txt"));

                JSONArray daily = object.getJSONArray("daily_forecast");
                List<DailyForecastBean> dailyForecastBeanList = new ArrayList<DailyForecastBean>();
                for (int i = 0; i < daily.length(); i++) {
                    DailyForecastBean dailyForecastBean = new DailyForecastBean();
                    JSONObject tmpObj = (JSONObject) daily.get(i);
                    dailyForecastBean.setDailyDate(tmpObj.getString("date"));
                    dailyForecastBean.setDailyTmpMax(tmpObj.getJSONObject("tmp").getString("max"));
                    dailyForecastBean.setDailyTmpMin(tmpObj.getJSONObject("tmp").getString("min"));
                    dailyForecastBean.setDailyWeatherCode(tmpObj.getJSONObject("cond").getString("code_d"));
                    dailyForecastBeanList.add(dailyForecastBean);
                }
                bean.setWeatherBeanList(dailyForecastBeanList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "handleWeatherResponse: JSON数据为空");
        }
        return bean;
    }

    /**
     * 解析服务器返回的JSON数据中的一部分用于在“已添加的城市列表”中填充数据
     * @param response 服务器返回的JSON数据
     * @return CardWeatherBean 类型的数据
     */
    public static CardWeatherBean handleCardWeatherResponse(String response) {
        CardWeatherBean cardBean = null;
        if (response != null) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray array = obj.getJSONArray("HeWeather5");
                JSONObject object = (JSONObject) array.get(0);
                String status = object.getString("status");
                if (status.equals("ok")) {
                    cardBean = new CardWeatherBean();
                    cardBean.setCardNowWeather(object.getJSONObject("now").getJSONObject("cond").getString("txt"));
                    cardBean.setCardNowWeatherCode(object.getJSONObject("now").getJSONObject("cond").getString("code"));
                    cardBean.setCardNowTemp(object.getJSONObject("now").getString("tmp"));
                    cardBean.setCardCity(object.getJSONObject("basic").getString("city"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cardBean;
    }
}
