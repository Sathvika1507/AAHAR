package com.example.aahar100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        CardView cardDonate = findViewById(R.id.carddonate);
        CardView cardReceive = findViewById(R.id.cardReceive);
        CardView cardFoodMap = findViewById(R.id.cardFoodMap);
        CardView cardMyPin = findViewById(R.id.cardMyPin);
        CardView cardHistory = findViewById(R.id.cardHistory);
        CardView profile = findViewById(R.id.profile);
        CardView menu_setting = findViewById(R.id.menu_setting);
        CardView menu_logout = findViewById(R.id.menu_logout);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        } else {
            checkIfEmailVerified(firebaseUser);
        }

        profile.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        menu_setting.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, setting_activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        menu_logout.setOnClickListener(view -> {
            authProfile.signOut();
            Intent intent = new Intent(MainActivity.this, landing_page.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        cardReceive.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            checkingPinExistOrNotForReceive();
        });

        cardDonate.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            checkingPinExistOrNotForDonate();
        });

        cardFoodMap.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FoodMap.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        cardHistory.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, History.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        cardMyPin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("DonateIdMapping");
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        String a = dataSnapshot.child("donationId").getValue(String.class);
                        showAlertDialogToRemoveCurrentPin(a);
                    } else {
                        showAlertDialogPinDoesNotExist();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Error checking user existence", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void checkingPinExistOrNotForDonate() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("DonateIdMapping");
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    showAlertDialogThree();
                } else {
                    startActivity(new Intent(MainActivity.this, DonateActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error checking user existence", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkingPinExistOrNotForReceive() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("DonateIdMapping");
        usersRef.child(uid);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    showAlertDialogThree();
                } else {
                    startActivity(new Intent(MainActivity.this, ReceiveActivity.class));  // Correctly launching the ReceiveActivity
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error checking user existence", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. If you have already verified your email, close the app and restart.");
        builder.setCancelable(false);
        builder.setPositiveButton("Get Verified", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.light_blue));
            Objects.requireNonNull(alertDialog.getWindow())
                    .setBackgroundDrawableResource(R.drawable.button_bg1);
        });
        alertDialog.show();
    }

    private void showAlertDialogToRemoveCurrentPin(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Remove Current Pin");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            DatabaseReference refMapping = FirebaseDatabase.getInstance().getReference("DonateIdMapping");
            DatabaseReference refMap = FirebaseDatabase.getInstance().getReference("FoodMap");

            refMapping.child(firebaseUser.getUid()).removeValue()
                    .addOnSuccessListener(unused -> Log.d(TAG, "User mapping deleted"))
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

            refMap.child(id).removeValue()
                    .addOnSuccessListener(unused -> Log.d(TAG, "Pin deleted"))
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

            Toast.makeText(MainActivity.this, "Your pin is removed", Toast.LENGTH_LONG).show();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.light_blue));
            Objects.requireNonNull(alertDialog.getWindow())
                    .setBackgroundDrawableResource(R.drawable.button_bg1);
        });

        progressBar.setVisibility(View.GONE);
        alertDialog.show();
    }

    private void showAlertDialogThree() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pin Already Exists");
        builder.setMessage("To proceed, please remove your existing pin.");
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface ->
                Objects.requireNonNull(alertDialog.getWindow())
                        .setBackgroundDrawableResource(R.drawable.button_bg1));
        alertDialog.show();
    }

    private void showAlertDialogPinDoesNotExist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pin Does Not Exist");
        builder.setMessage("Please place a donation pin first to continue.");
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface ->
                Objects.requireNonNull(alertDialog.getWindow())
                        .setBackgroundDrawableResource(R.drawable.button_bg1));
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.reload();
            if (!firebaseUser.isEmailVerified()) {
                showAlertDialog();
            }
        }
    }
}
