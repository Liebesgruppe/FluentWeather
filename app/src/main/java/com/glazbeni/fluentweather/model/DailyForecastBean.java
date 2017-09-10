package com.glazbeni.fluentweather.model;

/**
 * Created by 29044 on 17.8.30.
 */

public class DailyForecastBean {

    private String dailyTmpMax;
    private String dailyTmpMin;
    private String dailyDate;
    private String dailyWeatherCode;

    public String getDailyTmpMax() {
        return dailyTmpMax;
    }

    public void setDailyTmpMax(String dailyTmpMax) {
        this.dailyTmpMax = dailyTmpMax;
    }

    public String getDailyTmpMin() {
        return dailyTmpMin;
    }

    public void setDailyTmpMin(String dailyTmpMin) {
        this.dailyTmpMin = dailyTmpMin;
    }

    public String getDailyDate() {
        return dailyDate;
    }

    public void setDailyDate(String dailyDate) {
        this.dailyDate = dailyDate;
    }

    public String getDailyWeatherCode() {
        return dailyWeatherCode;
    }

    public void setDailyWeatherCode(String dailyWeatherCode) {
        this.dailyWeatherCode = dailyWeatherCode;
    }
}
