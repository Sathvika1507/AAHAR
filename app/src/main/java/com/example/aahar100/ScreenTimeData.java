package com.example.aahar100;

public class ScreenTimeData {
    private String appName;
    private String packageName;
    private long usageTime;
    private long lastTimeUsed;
    private int launchCount;

    public ScreenTimeData() {
    }

    public ScreenTimeData(String appName, String packageName, long usageTime, long lastTimeUsed, int launchCount) {
        this.appName = appName;
        this.packageName = packageName;
        this.usageTime = usageTime;
        this.lastTimeUsed = lastTimeUsed;
        this.launchCount = launchCount;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(long usageTime) {
        this.usageTime = usageTime;
    }

    public long getLastTimeUsed() {
        return lastTimeUsed;
    }

    public void setLastTimeUsed(long lastTimeUsed) {
        this.lastTimeUsed = lastTimeUsed;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    public String getFormattedUsageTime() {
        long hours = usageTime / (1000 * 60 * 60);
        long minutes = (usageTime % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
}
