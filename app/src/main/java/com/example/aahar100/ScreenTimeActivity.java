package com.example.aahar100;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScreenTimeActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnToday, btnYesterday, btnWeek;
    private TextView tvTotalTime;
    private RecyclerView recyclerViewApps;
    private SwipeRefreshLayout swipeRefresh;
    
    private ScreenTimeManager screenTimeManager;
    private ScreenTimeAdapter adapter;
    private List<ScreenTimeData> screenTimeDataList;
    
    private String currentPeriod = "today";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_time);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        loadData(currentPeriod);
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnToday = findViewById(R.id.btnToday);
        btnYesterday = findViewById(R.id.btnYesterday);
        btnWeek = findViewById(R.id.btnWeek);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        recyclerViewApps = findViewById(R.id.recyclerViewApps);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        
        screenTimeManager = new ScreenTimeManager(this);
        screenTimeDataList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new ScreenTimeAdapter(screenTimeDataList);
        recyclerViewApps.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewApps.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnToday.setOnClickListener(v -> {
            currentPeriod = "today";
            updateButtonStates();
            loadData(currentPeriod);
        });

        btnYesterday.setOnClickListener(v -> {
            currentPeriod = "yesterday";
            updateButtonStates();
            loadData(currentPeriod);
        });

        btnWeek.setOnClickListener(v -> {
            currentPeriod = "week";
            updateButtonStates();
            loadData(currentPeriod);
        });

        swipeRefresh.setOnRefreshListener(() -> {
            loadData(currentPeriod);
            swipeRefresh.setRefreshing(false);
        });
    }

    private void updateButtonStates() {
        btnToday.setBackgroundResource(currentPeriod.equals("today") ? 
                R.drawable.button_gradient : R.drawable.card_gradient);
        btnYesterday.setBackgroundResource(currentPeriod.equals("yesterday") ? 
                R.drawable.button_gradient : R.drawable.card_gradient);
        btnWeek.setBackgroundResource(currentPeriod.equals("week") ? 
                R.drawable.button_gradient : R.drawable.card_gradient);
    }

    private void loadData(String period) {
        if (!screenTimeManager.hasUsageStatsPermission()) {
            tvTotalTime.setText("--");
            return;
        }

        new Thread(() -> {
            List<ScreenTimeData> data;
            
            switch (period) {
                case "yesterday":
                    data = screenTimeManager.getYesterdayScreenTime();
                    break;
                case "week":
                    data = screenTimeManager.getWeekScreenTime();
                    break;
                default:
                    data = screenTimeManager.getTodayScreenTime();
                    break;
            }
            
            long totalTime = screenTimeManager.getTotalScreenTime(data);
            
            runOnUiThread(() -> {
                screenTimeDataList.clear();
                screenTimeDataList.addAll(data);
                adapter.notifyDataSetChanged();
                
                String formattedTime = screenTimeManager.getFormattedTime(totalTime);
                tvTotalTime.setText(formattedTime);
            });
        }).start();
    }
}
