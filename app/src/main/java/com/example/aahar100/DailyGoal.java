package com.example.aahar100;

public class DailyGoal {
    private long goalTimeInMillis;
    private long currentUsageTime;
    private String date;
    private boolean achieved;

    public DailyGoal() {
    }

    public DailyGoal(long goalTimeInMillis, long currentUsageTime, String date) {
        this.goalTimeInMillis = goalTimeInMillis;
        this.currentUsageTime = currentUsageTime;
        this.date = date;
        this.achieved = currentUsageTime <= goalTimeInMillis;
    }

    public long getGoalTimeInMillis() {
        return goalTimeInMillis;
    }

    public void setGoalTimeInMillis(long goalTimeInMillis) {
        this.goalTimeInMillis = goalTimeInMillis;
    }

    public long getCurrentUsageTime() {
        return currentUsageTime;
    }

    public void setCurrentUsageTime(long currentUsageTime) {
        this.currentUsageTime = currentUsageTime;
        this.achieved = currentUsageTime <= goalTimeInMillis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public int getProgressPercentage() {
        if (goalTimeInMillis == 0) return 0;
        return (int) ((currentUsageTime * 100) / goalTimeInMillis);
    }

    public String getFormattedGoalTime() {
        long hours = goalTimeInMillis / (1000 * 60 * 60);
        long minutes = (goalTimeInMillis % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    public String getFormattedCurrentTime() {
        long hours = currentUsageTime / (1000 * 60 * 60);
        long minutes = (currentUsageTime % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
}
