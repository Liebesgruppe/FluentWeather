package com.glazbeni.fluentweather.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glazbeni.fluentweather.R;

/**
 * Created by 29044 on 17.9.1.
 */

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MyViewHolder> {

    private Context context;

    private MyCardAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemCity;
        private TextView tvItemWeather;
        private ImageView ivWeather;
        private View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            tvItemCity = (TextView) view.findViewById(R.id.tv_item_city);
            tvItemWeather = (TextView) view.findViewById(R.id.tv_item_weather);
            ivWeather = (ImageView) view.findViewById(R.id.iv_item_weather);
        }
    }
}
