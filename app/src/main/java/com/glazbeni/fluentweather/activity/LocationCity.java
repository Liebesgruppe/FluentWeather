package com.glazbeni.fluentweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glazbeni.fluentweather.R;
import com.glazbeni.fluentweather.model.CardWeatherBean;
import com.glazbeni.fluentweather.util.HttpCallbackListener;
import com.glazbeni.fluentweather.util.HttpUtil;
import com.glazbeni.fluentweather.util.MoveItemTouchHelperCallback;
import com.glazbeni.fluentweather.util.MyCardAdapter;
import com.glazbeni.fluentweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.glazbeni.fluentweather.activity.ShowWeatherActivity.GET_WEATHER_BEAN;
import static com.glazbeni.fluentweather.activity.ShowWeatherActivity.URL_PART1;
import static com.glazbeni.fluentweather.activity.ShowWeatherActivity.URL_PART2;

/**
 * Created by 29044 on 17.8.30.
 */

public class LocationCity extends AppCompatActivity implements TextView.OnEditorActionListener, TextWatcher, View.OnClickListener, MyCardAdapter.OnCardClickListener {

    private MyCardAdapter cardAdapter;
    private EditText etSearch; //搜索栏
    private ImageView ivClear; //用于清空搜索栏中的内容
    public static List<String> cityNameList;
    private List<CardWeatherBean> cardBeanList;
    public static final int NO_CONNECTION = 105;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_WEATHER_BEAN:
                    cardAdapter.notifyDataSetChanged();
                    break;
                case NO_CONNECTION:
                    Toast.makeText(LocationCity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_city);

        initView();
        readPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveAsPreferences();
    }

    /**
     * 初始化布局资源
     */
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_location_city);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_location_city);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        etSearch = (EditText) findViewById(R.id.et_search);
        ivClear = (ImageView) findViewById(R.id.iv_clear);
        cardBeanList = new ArrayList<>();
        cardAdapter = new MyCardAdapter(this, cardBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cardAdapter);
        ItemTouchHelper.Callback callback = new MoveItemTouchHelperCallback(cardAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
        ivClear.setOnClickListener(this);
        cardAdapter.setOnCardClickListener(this);
        ivClear.setVisibility(View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String etCity = etSearch.getText().toString();
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (!TextUtils.isEmpty(etCity)) {
                if (cityNameList.contains(etCity)) {
                    Toast.makeText(this, "城市已存在啦(●ˇ∀ˇ●)", Toast.LENGTH_SHORT).show();
                } else {
                    getJSONCard(etCity);
                    cityNameList.add(etCity);
                }
                etSearch.setText("");
                ivClear.setVisibility(View.GONE);
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        ivClear.setVisibility(View.VISIBLE); //当搜索栏中的内容产生变化时将“清空”按钮设为可见
    }

    @Override
    public void onClick(View v) {
        etSearch.setText("");
        ivClear.setVisibility(View.GONE); //当搜索栏中的内容为空时将“清空”按钮设为不可见
    }

    /**
     * 从服务器获取天气数据
     */
    private void getJSONCard(String district) {
        HttpUtil.sendHttpRequest(URL_PART1 + district + URL_PART2, new HttpCallbackListener() {
            CardWeatherBean cardWeatherBean = null;

            @Override
            public void onFinish(String response) {
                cardWeatherBean = Utility.handleCardWeatherResponse(response);
                if (cardWeatherBean != null) {
                    cardBeanList.add(cardWeatherBean);
                    Message msg = new Message();
                    msg.what = GET_WEATHER_BEAN;
                    handler.sendEmptyMessage(msg.what);
                } else {
                    Toast.makeText(LocationCity.this, "未找到该城市，请确保输入正确_(:з)∠)_", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onCardClick(View view, int position) {
        Intent intent = new Intent(this, ShowWeatherActivity.class);
        intent.putExtra("cityName", cardBeanList.get(position).getCardCity());
        startActivity(intent);
    }

    private void saveAsPreferences() {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pre.edit();
        int cityListSize = cityNameList.size();
        editor.putInt("city_list_size", cityListSize);
        for (int i = 0; i < cityListSize; i++) {
            editor.putString("city_name" + i, cityNameList.get(i));
        }
        editor.apply();
    }

    private void readPreferences() {
        cityNameList = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int preferencesSize = preferences.getInt("city_list_size", 0);
        if (preferencesSize > 0) {
            for (int i = 0; i < preferencesSize; i++) {
                getJSONCard(preferences.getString("city_name" + i, ""));
                cityNameList.add(preferences.getString("city_name" + i, ""));
            }
        }
    }
}