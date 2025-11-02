package com.example.aahar100;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvCurrentGoal;
    private TextView tvGoalProgress;
    private ProgressBar progressGoal;
    private NumberPicker pickerHours;
    private NumberPicker pickerMinutes;
    private Button btnSaveGoal;
    
    private ScreenTimeManager screenTimeManager;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    
    private long currentGoalInMillis = 0;
    private long currentScreenTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        initializeViews();
        setupNumberPickers();
        setupClickListeners();
        loadCurrentGoal();
        loadScreenTimeData();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvCurrentGoal = findViewById(R.id.tvCurrentGoal);
        tvGoalProgress = findViewById(R.id.tvGoalProgress);
        progressGoal = findViewById(R.id.progressGoal);
        pickerHours = findViewById(R.id.pickerHours);
        pickerMinutes = findViewById(R.id.pickerMinutes);
        btnSaveGoal = findViewById(R.id.btnSaveGoal);
        
        screenTimeManager = new ScreenTimeManager(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        
        if (firebaseUser != null) {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(firebaseUser.getUid());
        }
    }

    private void setupNumberPickers() {
        pickerHours.setMinValue(0);
        pickerHours.setMaxValue(12);
        pickerHours.setValue(2);
        
        pickerMinutes.setMinValue(0);
        pickerMinutes.setMaxValue(59);
        pickerMinutes.setValue(0);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSaveGoal.setOnClickListener(v -> saveGoal());
    }

    private void loadCurrentGoal() {
        if (databaseReference == null) return;
        
        databaseReference.child("dailyGoal").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                currentGoalInMillis = task.getResult().getValue(Long.class);
                updateGoalDisplay();
            } else {
                tvCurrentGoal.setText(R.string.no_goal_set);
                progressGoal.setProgress(0);
                tvGoalProgress.setText("0% of goal");
            }
        });
    }

    private void loadScreenTimeData() {
        if (!screenTimeManager.hasUsageStatsPermission()) {
            return;
        }

        new Thread(() -> {
            List<ScreenTimeData> screenTimeDataList = screenTimeManager.getTodayScreenTime();
            currentScreenTime = screenTimeManager.getTotalScreenTime(screenTimeDataList);
            
            runOnUiThread(this::updateGoalDisplay);
        }).start();
    }

    private void updateGoalDisplay() {
        if (currentGoalInMillis > 0) {
            String formattedGoal = screenTimeManager.getFormattedTime(currentGoalInMillis);
            tvCurrentGoal.setText(formattedGoal);
            
            int progress = (int) ((currentScreenTime * 100) / currentGoalInMillis);
            progressGoal.setProgress(Math.min(progress, 100));
            
            String formattedCurrent = screenTimeManager.getFormattedTime(currentScreenTime);
            tvGoalProgress.setText(progress + "% of goal (" + formattedCurrent + " / " + formattedGoal + ")");
        }
    }

    private void saveGoal() {
        if (databaseReference == null) {
            Toast.makeText(this, "Please login to save goals", Toast.LENGTH_SHORT).show();
            return;
        }

        int hours = pickerHours.getValue();
        int minutes = pickerMinutes.getValue();
        
        if (hours == 0 && minutes == 0) {
            Toast.makeText(this, "Please set a valid goal", Toast.LENGTH_SHORT).show();
            return;
        }
        
        long goalInMillis = (hours * 60 * 60 * 1000L) + (minutes * 60 * 1000L);
        
        databaseReference.child("dailyGoal").setValue(goalInMillis)
                .addOnSuccessListener(aVoid -> {
                    currentGoalInMillis = goalInMillis;
                    updateGoalDisplay();
                    Toast.makeText(GoalsActivity.this, "Goal saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(GoalsActivity.this, "Failed to save goal: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScreenTimeData();
    }
}
