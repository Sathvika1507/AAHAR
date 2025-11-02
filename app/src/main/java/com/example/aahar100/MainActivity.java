package com.example.aahar100;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    
    private TextView tvScreenTime;
    private TextView tvGoalStatus;
    private TextView tvQuote;
    private TextView tvQuoteAuthor;
    private ProgressBar progressCircular;
    private Button btnViewDetails;
    private CardView cardGoals;
    private CardView cardStatistics;
    private ImageView btnSettings;
    
    private ScreenTimeManager screenTimeManager;
    private QuoteManager quoteManager;
    private DatabaseReference databaseReference;
    
    private long totalScreenTime = 0;
    private long dailyGoal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeManagers();
        checkAuthentication();
        setupClickListeners();
        loadData();
    }

    private void initializeViews() {
        tvScreenTime = findViewById(R.id.tvScreenTime);
        tvGoalStatus = findViewById(R.id.tvGoalStatus);
        tvQuote = findViewById(R.id.tvQuote);
        tvQuoteAuthor = findViewById(R.id.tvQuoteAuthor);
        progressCircular = findViewById(R.id.progressCircular);
        btnViewDetails = findViewById(R.id.btnViewDetails);
        cardGoals = findViewById(R.id.cardGoals);
        cardStatistics = findViewById(R.id.cardStatistics);
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void initializeManagers() {
        screenTimeManager = new ScreenTimeManager(this);
        quoteManager = new QuoteManager();
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        
        if (firebaseUser != null) {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(firebaseUser.getUid());
        }
    }

    private void checkAuthentication() {
        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, landing_page.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (!firebaseUser.isEmailVerified()) {
            showEmailVerificationDialog();
        }
    }

    private void setupClickListeners() {
        btnViewDetails.setOnClickListener(v -> {
            if (screenTimeManager.hasUsageStatsPermission()) {
                Intent intent = new Intent(MainActivity.this, ScreenTimeActivity.class);
                startActivity(intent);
            } else {
                showPermissionDialog();
            }
        });

        cardGoals.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GoalsActivity.class);
            startActivity(intent);
        });

        cardStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, setting_activity.class);
            startActivity(intent);
        });
    }

    private void loadData() {
        loadMotivationalQuote();
        loadScreenTimeData();
    }

    private void loadMotivationalQuote() {
        Quote quote = quoteManager.getDailyQuote();
        tvQuote.setText("\"" + quote.getText() + "\"");
        if (quote.getAuthor() != null && !quote.getAuthor().isEmpty()) {
            tvQuoteAuthor.setText("- " + quote.getAuthor());
        } else {
            tvQuoteAuthor.setVisibility(View.GONE);
        }
    }

    private void loadScreenTimeData() {
        if (!screenTimeManager.hasUsageStatsPermission()) {
            tvScreenTime.setText("--");
            tvGoalStatus.setText(R.string.permission_usage_stats_message);
            progressCircular.setProgress(0);
            return;
        }

        new Thread(() -> {
            List<ScreenTimeData> screenTimeDataList = screenTimeManager.getTodayScreenTime();
            totalScreenTime = screenTimeManager.getTotalScreenTime(screenTimeDataList);
            
            runOnUiThread(() -> {
                String formattedTime = screenTimeManager.getFormattedTime(totalScreenTime);
                tvScreenTime.setText(formattedTime);
                
                loadGoalData();
            });
        }).start();
    }

    private void loadGoalData() {
        if (databaseReference == null) return;
        
        databaseReference.child("dailyGoal").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                dailyGoal = task.getResult().getValue(Long.class);
                updateGoalProgress();
            } else {
                tvGoalStatus.setText(R.string.no_goal_set);
                progressCircular.setProgress(0);
            }
        });
    }

    private void updateGoalProgress() {
        if (dailyGoal > 0) {
            int progress = (int) ((totalScreenTime * 100) / dailyGoal);
            progressCircular.setProgress(Math.min(progress, 100));
            
            String goalTime = screenTimeManager.getFormattedTime(dailyGoal);
            if (totalScreenTime <= dailyGoal) {
                tvGoalStatus.setText(progress + "% of " + goalTime + " goal");
            } else {
                tvGoalStatus.setText(getString(R.string.goal_exceeded) + " (" + goalTime + ")");
            }
        } else {
            tvGoalStatus.setText(R.string.no_goal_set);
            progressCircular.setProgress(0);
        }
    }

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_usage_stats_title);
        builder.setMessage(R.string.permission_usage_stats_message);
        builder.setPositiveButton(R.string.permission_go_to_settings, (dialog, which) -> {
            screenTimeManager.requestUsageStatsPermission();
        });
        builder.setNegativeButton(R.string.permission_cancel, null);
        
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showEmailVerificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email to continue using DigitalWell.");
        builder.setCancelable(false);
        builder.setPositiveButton("Get Verified", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScreenTimeData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser != null) {
            firebaseUser.reload();
        }
    }
}
