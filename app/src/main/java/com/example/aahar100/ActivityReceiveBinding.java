// Simulated generated view binding class
package com.example.aahar100;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;

import com.example.aahar100.databinding.ActivityFoodMapBinding;
import com.google.android.gms.maps.MapView;
import android.widget.RelativeLayout;

public final class ActivityReceiveBinding {
    @NonNull
    public final RelativeLayout receiveRoot;

    @NonNull
    public final MapView receiveMapView;

    @NonNull
    public final ProgressBar receiveProgressBar;

    @NonNull
    public final View root;

    private ActivityReceiveBinding(@NonNull View root, @NonNull RelativeLayout receiveRoot, @NonNull MapView receiveMapView, @NonNull ProgressBar receiveProgressBar) {
        this.root = root;
        this.receiveRoot = receiveRoot;
        this.receiveMapView = receiveMapView;
        this.receiveProgressBar = receiveProgressBar;
    }

    public static ActivityFoodMapBinding inflate(LayoutInflater layoutInflater) {
        return null;
    }

    @NonNull
    public View getRoot() {
        return root;
    }
}
