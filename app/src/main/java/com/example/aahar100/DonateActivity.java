package com.example.aahar100;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.Button;

import java.util.Objects;

public class DonateActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_CODE = 100;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private TextInputEditText foodItemEditText, descriptionEditText;
    private ProgressBar progressBar;

    private double userLat = 0.0, userLng = 0.0;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        // Initialize views
        foodItemEditText = findViewById(R.id.foodItem);
        descriptionEditText = findViewById(R.id.Description);
        Button submitButton = findViewById(R.id.button_add_pin);
        progressBar = findViewById(R.id.progressBar);

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize location components
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        setupLocationCallback();

        // Request permission
        requestLocationPermission();

        // Submit button listener
        submitButton.setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(DonateActivity.this, "Please sign in first.", Toast.LENGTH_SHORT).show();
                return;
            }

            String foodItem = Objects.requireNonNull(foodItemEditText.getText()).toString().trim();
            String description = Objects.requireNonNull(descriptionEditText.getText()).toString().trim();

            if (foodItem.isEmpty()) {
                foodItemEditText.setError("Enter food item");
                return;
            }

            showPhoneShareDialog(user, foodItem, description);
        });
    }

    private void showPhoneShareDialog(FirebaseUser user, String foodItem, String description) {
        new AlertDialog.Builder(this)
                .setTitle("Share Contact?")
                .setMessage("Do you want to share your phone number with the receiver?")
                .setPositiveButton("Yes", (dialog, which) -> uploadDonation(user, foodItem, description, true))
                .setNegativeButton("No", (dialog, which) -> uploadDonation(user, foodItem, description, false))
                .show();
    }

    private void uploadDonation(FirebaseUser user, String item, String description, boolean sharePhone) {
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference donationRef = FirebaseDatabase.getInstance().getReference("Donations").push();

        donationRef.child("userId").setValue(user.getUid());
        donationRef.child("userName").setValue(user.getDisplayName() != null ? user.getDisplayName() : "Anonymous");
        donationRef.child("item").setValue(item);
        donationRef.child("description").setValue(description);
        donationRef.child("latitude").setValue(userLat);
        donationRef.child("longitude").setValue(userLng);
        donationRef.child("sharePhone").setValue(sharePhone)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(DonateActivity.this, "Donation posted!", Toast.LENGTH_SHORT).show();
                        foodItemEditText.setText("");
                        descriptionEditText.setText("");
                    } else {
                        Toast.makeText(DonateActivity.this, "Failed to upload donation.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                Location location = result.getLastLocation();
                if (location != null) {
                    userLat = location.getLatitude();
                    userLng = location.getLongitude();
                    if (mMap != null) {
                        LatLng userLocation = new LatLng(userLat, userLng);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions()
                                .position(userLocation)
                                .title("Your Location")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                    }
                }
            }
        };
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Location permission is needed to show your position.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
