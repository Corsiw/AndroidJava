package com.example.ex1.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex1.R;
import com.example.ex1.model.ForecastResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private final List<ForecastResponse.ForecastItem> list;

    public ForecastAdapter(List<ForecastResponse.ForecastItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ForecastResponse.ForecastItem item = list.get(position);

        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH)
                .format(new Date(item.dt * 1000));

        holder.dayName.setText(day);
        holder.dayDesc.setText(item.weather.get(0).main);
        holder.dayTemp.setText(item.main.temp + "°C");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayName, dayDesc, dayTemp;

        public ViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.dayName);
            dayDesc = itemView.findViewById(R.id.dayDesc);
            dayTemp = itemView.findViewById(R.id.dayTemp);
        }
    }
}