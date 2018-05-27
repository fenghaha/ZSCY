package com.fenghaha.zscy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fenghaha.zscy.R;

/**
 * Created by FengHaHa on2018/5/27 0027 2:36
 */
public class TimeRecAdapter extends RecyclerView.Adapter<TimeRecAdapter.ViewHolder> {
    String times[];

    public TimeRecAdapter(String[] times) {
        this.times = times;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.view.setText(times[position]);
    }

    @Override
    public int getItemCount() {
        return times.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tv_time);
        }
    }
}
