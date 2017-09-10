package com.glazbeni.fluentweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glazbeni.fluentweather.R;
import com.glazbeni.fluentweather.model.CardWeatherBean;

import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.glazbeni.fluentweather.activity.LocationCity.cityNameList;

/**
 * Created by 29044 on 17.9.1.
 */

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MyViewHolder> implements View.OnClickListener, ItemTouchHelperAdapter {

    private Context context;
    private List<CardWeatherBean> cardWeatherBeanList;
    private OnCardClickListener cardClickListener = null;

    public MyCardAdapter(Context context, List<CardWeatherBean> cardWeatherBeanList) {
        this.context = context;
        this.cardWeatherBeanList = cardWeatherBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_city, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvCardCity.setText(cardWeatherBeanList.get(position).getCardCity());
        holder.tvCardTemp.setText(" " + cardWeatherBeanList.get(position).getCardNowTemp() + "°C");
        holder.tvCardWeather.setText(cardWeatherBeanList.get(position).getCardNowWeather());
        holder.ivCardWeather.setImageResource(context.getResources().getIdentifier("w" + cardWeatherBeanList
                .get(position).getCardNowWeatherCode(), "drawable", "com.glazbeni.fluentweather"));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return cardWeatherBeanList.size();
    }

    @Override
    public void onClick(View v) {
        if (cardClickListener != null) {
            cardClickListener.onCardClick(v, (int) v.getTag());
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(cardWeatherBeanList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        cardWeatherBeanList.remove(position);
        notifyItemRemoved(position);
        cityNameList.remove(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        private TextView tvCardCity;
        private TextView tvCardWeather;
        private TextView tvCardTemp;
        private ImageView ivCardWeather;
        private View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            tvCardCity = (TextView) view.findViewById(R.id.tv_card_city);
            tvCardWeather = (TextView) view.findViewById(R.id.tv_card_weather);
            tvCardTemp = (TextView) view.findViewById(R.id.tv_card_temp);
            ivCardWeather = (ImageView) view.findViewById(R.id.iv_card_weather);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.GRAY);
        }

        @Override
        public void onItemCLear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

    public static interface OnCardClickListener {
        void onCardClick(View view, int position);
    }

    public void setOnCardClickListener(OnCardClickListener cardClickListener) {
        this.cardClickListener = cardClickListener;
    }
}
