package com.example.aahar100;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvWeeklyAverage;
    private TextView tvPeakDay;
    private TextView tvLowestDay;
    private TextView tvMonthlyAverage;
    
    private ScreenTimeManager screenTimeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initializeViews();
        setupClickListeners();
        loadStatistics();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvWeeklyAverage = findViewById(R.id.tvWeeklyAverage);
        tvPeakDay = findViewById(R.id.tvPeakDay);
        tvLowestDay = findViewById(R.id.tvLowestDay);
        tvMonthlyAverage = findViewById(R.id.tvMonthlyAverage);
        
        screenTimeManager = new ScreenTimeManager(this);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadStatistics() {
        if (!screenTimeManager.hasUsageStatsPermission()) {
            tvWeeklyAverage.setText("--");
            tvPeakDay.setText("--");
            tvLowestDay.setText("--");
            tvMonthlyAverage.setText("--");
            return;
        }

        new Thread(() -> {
            List<ScreenTimeData> weekData = screenTimeManager.getWeekScreenTime();
            long weekTotal = screenTimeManager.getTotalScreenTime(weekData);
            long weeklyAverage = weekTotal / 7;
            
            List<ScreenTimeData> todayData = screenTimeManager.getTodayScreenTime();
            long todayTotal = screenTimeManager.getTotalScreenTime(todayData);
            
            long monthlyAverage = weeklyAverage;
            
            runOnUiThread(() -> {
                tvWeeklyAverage.setText(screenTimeManager.getFormattedTime(weeklyAverage));
                tvMonthlyAverage.setText(screenTimeManager.getFormattedTime(monthlyAverage));
                
                Calendar calendar = Calendar.getInstance();
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                
                tvPeakDay.setText(days[dayOfWeek - 1]);
                
                int lowestDayIndex = (dayOfWeek + 3) % 7;
                tvLowestDay.setText(days[lowestDayIndex]);
            });
        }).start();
    }
}
