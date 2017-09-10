package com.glazbeni.fluentweather.model;

import java.util.List;

/**
 * Created by 29044 on 17.9.1.
 */

public class WeatherBean {

    private String pm25;
    private String qlty;
    private String city;
    private String id;
    private String updateTime;
    private String txt;
    private String temp;
    private String weatherCode;
    private String airBrf;
    private String airTxt;
    private String cofmBrf;
    private String cofmTxt;
    private String cwBrf;
    private String cwTxt;
    private String drsgBrf;
    private String drsgTxt;
    private String fluBrf;
    private String fluTxt;
    private String sportBrf;
    private String sportTxt;
    private String travBrf;
    private String travTxt;
    private String uvBrf;
    private String uvTxt;

    private List<DailyForecastBean> dailyForecastBeanList;

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQlty() {
        return qlty;
    }

    public void setQlty(String qlty) {
        this.qlty = qlty;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getAirBrf() {
        return airBrf;
    }

    public void setAirBrf(String airBrf) {
        this.airBrf = airBrf;
    }

    public String getAirTxt() {
        return airTxt;
    }

    public void setAirTxt(String airTxt) {
        this.airTxt = airTxt;
    }

    public String getCofmBrf() {
        return cofmBrf;
    }

    public void setCofmBrf(String cofmBrf) {
        this.cofmBrf = cofmBrf;
    }

    public String getCofmTxt() {
        return cofmTxt;
    }

    public void setCofmTxt(String cofmTxt) {
        this.cofmTxt = cofmTxt;
    }

    public String getCwBrf() {
        return cwBrf;
    }

    public void setCwBrf(String cwBrf) {
        this.cwBrf = cwBrf;
    }

    public String getCwTxt() {
        return cwTxt;
    }

    public void setCwTxt(String cwTxt) {
        this.cwTxt = cwTxt;
    }

    public String getDrsgBrf() {
        return drsgBrf;
    }

    public void setDrsgBrf(String drsgBrf) {
        this.drsgBrf = drsgBrf;
    }

    public String getDrsgTxt() {
        return drsgTxt;
    }

    public void setDrsgTxt(String drsgTxt) {
        this.drsgTxt = drsgTxt;
    }

    public String getFluBrf() {
        return fluBrf;
    }

    public void setFluBrf(String fluBrf) {
        this.fluBrf = fluBrf;
    }

    public String getFluTxt() {
        return fluTxt;
    }

    public void setFluTxt(String fluTxt) {
        this.fluTxt = fluTxt;
    }

    public String getSportBrf() {
        return sportBrf;
    }

    public void setSportBrf(String sportBrf) {
        this.sportBrf = sportBrf;
    }

    public String getSportTxt() {
        return sportTxt;
    }

    public void setSportTxt(String sportTxt) {
        this.sportTxt = sportTxt;
    }

    public String getTravBrf() {
        return travBrf;
    }

    public void setTravBrf(String travBrf) {
        this.travBrf = travBrf;
    }

    public String getTravTxt() {
        return travTxt;
    }

    public void setTravTxt(String travTxt) {
        this.travTxt = travTxt;
    }

    public String getUvBrf() {
        return uvBrf;
    }

    public void setUvBrf(String uvBrf) {
        this.uvBrf = uvBrf;
    }

    public String getUvTxt() {
        return uvTxt;
    }

    public void setUvTxt(String uvTxt) {
        this.uvTxt = uvTxt;
    }

    public List<DailyForecastBean> getDailyForecastBeanList() {
        return dailyForecastBeanList;
    }

    public void setWeatherBeanList(List<DailyForecastBean> dailyForecastBeanList) {
        this.dailyForecastBeanList = dailyForecastBeanList;
    }
}
