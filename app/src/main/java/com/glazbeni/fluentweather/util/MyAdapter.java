package com.glazbeni.fluentweather.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glazbeni.fluentweather.R;
import com.glazbeni.fluentweather.model.SevenWeather;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 29044 on 17.8.29.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<SevenWeather> sevenWeathers;

    public MyAdapter(Context context, List<SevenWeather> sevenWeathers) {
        this.context = context;
        this.sevenWeathers = sevenWeathers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.seven_weather, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvDate.setText(sevenWeathers.get(position).getDate());
        holder.tvTmpMax.setText(sevenWeathers.get(position).getMax());
        holder.tvTmpMin.setText(sevenWeathers.get(position).getMin());
    }

    @Override
    public int getItemCount() {
        return sevenWeathers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //用于显示未来七天天气的日期
        private TextView tvDate;
        //用于显示未来七天天气的图示
        private ImageView ivWeather;
        //用于显示未来七天天气的最高温
        private TextView tvTmpMax;
        //用于显示未来七天天气的最低温
        private TextView tvTmpMin;
        private View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvTmpMax = (TextView) view.findViewById(R.id.tv_tmp_max);
            tvTmpMin = (TextView) view.findViewById(R.id.tv_tmp_min);
//            ivWeather = (ImageView) view.findViewById(R.id.iv_weather);
        }
    }
}
