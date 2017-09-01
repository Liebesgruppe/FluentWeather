package com.glazbeni.fluentweather.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.glazbeni.fluentweather.R;
import com.glazbeni.fluentweather.model.SevenWeather;
import com.glazbeni.fluentweather.util.HttpCallbackListener;
import com.glazbeni.fluentweather.util.HttpUtil;
import com.glazbeni.fluentweather.util.MyAdapter;
import com.glazbeni.fluentweather.util.RuntimePermission;
import com.glazbeni.fluentweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 29044 on 17.8.24.
 */

public class ShowWeatherActivity extends AppCompatActivity {

    private Toolbar toolbar;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption;
    private AMapLocation amLocation;
    //用于显示所在城市
    private TextView tvLocation;
    //用于显示实时温度
    private TextView tvTemp;
    //用于显示最高最低气温
    private TextView tvTempMaxMin;
    //用于显示天气
    private TextView tvWeather;
    //用于显示PM2.5
    private TextView tvPM25;
    //用于显示空气质量
    private TextView tvQlty;
    //用于显示天气图片
    private ImageView ivWeather;
//    private SwipeRefreshLayout swipeRefresh;
    private ScrollView scrollView;
    private RecyclerView rvSevenWeather;
    private MyAdapter adapter;
    private List<SevenWeather> weatherList;
    private static final String URL_PART1 = "https://free-api.heweather.com/v5/weather?city=";
    private static final String URL_PART2 = "&key=457631a5733743148d3733cbc98a2500";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_weather);

        RuntimePermission.requestPermissions(this, 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}
                , new RuntimePermission.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        initData();
                    }

                    @Override
                    public void onPermissionDenied() {
                        AlertDialog builder = new AlertDialog.Builder(ShowWeatherActivity.this)
                                .setTitle("FBI WARNING")
                                .setMessage("必要的权限没有得到授予，应用可能无法正常运作(っ °Д °;)っ")
                                .setPositiveButton("我觉得可以", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                }).show();
                    }
                });
        initView();
        showWeather();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RuntimePermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    /**
     * 初始化布局资源
     */
    private void initView() {
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvTempMaxMin = (TextView) findViewById(R.id.tv_temp_max_min);
        tvWeather = (TextView) findViewById(R.id.tv_weather);
        tvPM25 = (TextView) findViewById(R.id.tv_pm25);
        tvQlty = (TextView) findViewById(R.id.tv_qlty);
//        ivWeather = (ImageView) findViewById(R.id.iv_weather);
//        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        scrollView = (ScrollView) findViewById(R.id.sv);
        rvSevenWeather = (RecyclerView) findViewById(R.id.rv_weather_seven);
        rvSevenWeather.setLayoutManager(new LinearLayoutManager(ShowWeatherActivity.this, LinearLayoutManager.HORIZONTAL, true));
        adapter = new MyAdapter(ShowWeatherActivity.this, weatherList);
        rvSevenWeather.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toobar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_location_on_white_24dp);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:

                        break;
                    case R.id.location_city:
                        startActivity(new Intent(ShowWeatherActivity.this, LocationCity.class));
                        break;
                    case R.id.menu_settings:
                        startActivity(new Intent(ShowWeatherActivity.this, SettingsActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 初始化所需数据
     */
    private void initData() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
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
        amLocation = mLocationClient.getLastKnownLocation();
        String district = amLocation.getDistrict();
        mLocationClient.stopLocation();
        Log.d("TEST", "initData: " + district);
        HttpUtil.sendHttpRequest(URL_PART1 + district + URL_PART2
                , new HttpCallbackListener() {
                    @Override
                    public void onFinish(String respone) {
                        Utility.handleWeatherResponse(ShowWeatherActivity.this, respone);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(ShowWeatherActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
        weatherList = new ArrayList<SevenWeather>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SevenWeather sevenWeather = new SevenWeather();
        for (int i = 0; i < preferences.getInt("size", 0); i++) {
            sevenWeather.setMax(preferences.getString("max" + i, ""));
            sevenWeather.setMin(preferences.getString("min" + i, ""));
            sevenWeather.setDate(preferences.getString("date" + i, ""));
            sevenWeather.setWeather(preferences.getString("weather" + i, ""));
            Log.d("ddd", "initData: " + preferences.getString("date" + i, ""));
            weatherList.add(sevenWeather);
        }
    }

    private void showWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tvLocation.setText(preferences.getString("city", ""));
        tvTemp.setText(preferences.getString("temp", "") + "°");
        tvTempMaxMin.setText(preferences.getString("max" + 0, "") + " / " + preferences.getString("min" + 0, "") + "C");
        tvWeather.setText(preferences.getString("txt", "") + " ");
        tvPM25.setText(preferences.getString("pm25", ""));
        tvQlty.setText(preferences.getString("qlty", ""));
    }
}
