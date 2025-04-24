package com.example.aahar100;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.aahar100.databinding.ActivityFoodMapBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FoodMap extends FragmentActivity implements OnMapReadyCallback {

    private ActivityFoodMapBinding binding;  // View binding reference
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLatLng;
    private Marker currentMarker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FirebaseAuth auth;
    private DatabaseReference foodMapRef;

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the binding
        binding = ActivityFoodMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Transparent status and navigation bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // Firebase setup
        auth = FirebaseAuth.getInstance();
        foodMapRef = FirebaseDatabase.getInstance().getReference("FoodMap");

        // Initialize the location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Manually initialize SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);  // Asynchronously load map
        }

        // Set up map click listener for adding markers
        binding.submitBtn.setOnClickListener(v -> {
            if (currentLatLng == null) {
                Toast.makeText(this, "Please select a location on the map.", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = binding.editTextName.getText().toString().trim();
            String food = binding.editTextFood.getText().toString().trim();
            if (name.isEmpty() || food.isEmpty()) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            askForPhoneNumberAndSubmit(name, food);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        mMap.setPadding(0, 100, 0, 125);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        checkLocationPermission();  // Ensure the app has location permissions

        // Set up map click listener for adding markers
        mMap.setOnMapClickListener(latLng -> {
            if (currentMarker != null) currentMarker.remove();
            currentLatLng = latLng;
            currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Drop food here")
                    .icon(setIcon(this, R.drawable.marker_donator_style)));
        });
    }

    // Ask for phone number and then submit the data
    private void askForPhoneNumberAndSubmit(String name, String food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share Number?")
                .setMessage("Do you want to share your phone number with the receiver?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseUser user = auth.getCurrentUser();
                    String number = user != null ? user.getPhoneNumber() : "not a number flag";
                    pushFoodData(name, number, food);
                })
                .setNegativeButton("No", (dialog, which) -> pushFoodData(name, "not a number flag", food))
                .show();
    }

    // Push food donation data to Firebase
    private void pushFoodData(String name, String number, String food) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("lat", currentLatLng.latitude);
        data.put("lng", currentLatLng.longitude);
        data.put("name", name);
        data.put("number", number);
        data.put("food", food);

        foodMapRef.push().setValue(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Donation added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FoodMap.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add donation", Toast.LENGTH_SHORT).show());
    }

    // Check if the app has location permission
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Get current location and update the map view
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 18));
            }
        });
    }

    // Handle permission results for location access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }

    // Set a custom icon for the map marker
    public BitmapDescriptor setIcon(Activity context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable == null) return null;

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
