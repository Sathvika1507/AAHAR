package com.example.aahar100;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScreenTimeAdapter extends RecyclerView.Adapter<ScreenTimeAdapter.ViewHolder> {

    private List<ScreenTimeData> screenTimeDataList;

    public ScreenTimeAdapter(List<ScreenTimeData> screenTimeDataList) {
        this.screenTimeDataList = screenTimeDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app_usage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScreenTimeData data = screenTimeDataList.get(position);
        holder.tvAppName.setText(data.getAppName());
        holder.tvPackageName.setText(data.getPackageName());
        holder.tvUsageTime.setText(data.getFormattedUsageTime());
    }

    @Override
    public int getItemCount() {
        return screenTimeDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppName;
        TextView tvPackageName;
        TextView tvUsageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppName = itemView.findViewById(R.id.tvAppName);
            tvPackageName = itemView.findViewById(R.id.tvPackageName);
            tvUsageTime = itemView.findViewById(R.id.tvUsageTime);
        }
    }
}
