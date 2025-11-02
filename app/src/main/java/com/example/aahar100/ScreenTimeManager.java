package com.example.aahar100;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ScreenTimeManager {
    private Context context;
    private UsageStatsManager usageStatsManager;
    private PackageManager packageManager;

    public ScreenTimeManager(Context context) {
        this.context = context;
        this.usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        this.packageManager = context.getPackageManager();
    }

    public boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public void requestUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public List<ScreenTimeData> getTodayScreenTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        long endTime = System.currentTimeMillis();

        return getScreenTimeForPeriod(startTime, endTime);
    }

    public List<ScreenTimeData> getYesterdayScreenTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endTime = calendar.getTimeInMillis();

        return getScreenTimeForPeriod(startTime, endTime);
    }

    public List<ScreenTimeData> getWeekScreenTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        long endTime = System.currentTimeMillis();

        return getScreenTimeForPeriod(startTime, endTime);
    }

    private List<ScreenTimeData> getScreenTimeForPeriod(long startTime, long endTime) {
        List<ScreenTimeData> screenTimeDataList = new ArrayList<>();

        if (!hasUsageStatsPermission()) {
            return screenTimeDataList;
        }

        Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startTime, endTime);

        for (Map.Entry<String, UsageStats> entry : usageStatsMap.entrySet()) {
            UsageStats usageStats = entry.getValue();
            
            if (usageStats.getTotalTimeInForeground() > 0) {
                String packageName = usageStats.getPackageName();
                String appName = getAppName(packageName);
                
                if (appName != null && !isSystemApp(packageName)) {
                    ScreenTimeData data = new ScreenTimeData(
                            appName,
                            packageName,
                            usageStats.getTotalTimeInForeground(),
                            usageStats.getLastTimeUsed(),
                            0
                    );
                    screenTimeDataList.add(data);
                }
            }
        }

        screenTimeDataList.sort((a, b) -> Long.compare(b.getUsageTime(), a.getUsageTime()));

        return screenTimeDataList;
    }

    public long getTotalScreenTime(List<ScreenTimeData> dataList) {
        long total = 0;
        for (ScreenTimeData data : dataList) {
            total += data.getUsageTime();
        }
        return total;
    }

    public String getFormattedTime(long timeInMillis) {
        long hours = timeInMillis / (1000 * 60 * 60);
        long minutes = (timeInMillis % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else if (minutes > 0) {
            return minutes + "m";
        } else {
            return "< 1m";
        }
    }

    private String getAppName(String packageName) {
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return packageManager.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private boolean isSystemApp(String packageName) {
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }
}
