package com.example.aahar100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class updateProfileActivity extends AppCompatActivity {

    private TextInputEditText regName, regPhoneNo;
    private TextInputLayout regNameLayout ,  regPhoneNoLayout;
    AppCompatButton updateChanges,openUpdateProfileActivity;
    private  String textName,textMobile;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        regName=findViewById(R.id.textUserName);
        regPhoneNo=findViewById(R.id.textUserNumber);
        regNameLayout=findViewById(R.id.nameLayout);
        regPhoneNoLayout=findViewById(R.id.numberLayout);
        openUpdateProfileActivity=findViewById(R.id.openUpdateProfileActivity);
        updateChanges=findViewById(R.id.updateChanges);

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        progressBar=findViewById(R.id.progressBar);
        showProfile(Objects.requireNonNull(firebaseUser));

        openUpdateProfileActivity.setOnClickListener(view -> {
            Intent intent =new Intent(updateProfileActivity.this, UploadProfilePicActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        updateChanges.setOnClickListener(view -> updateProfile(firebaseUser));
    }

    private void updateProfile(FirebaseUser firebaseUser) {
        //noinspection StatementWithEmptyBody
        if(!validateName()  | !validatePhoneNo()){
        }
        else {
            textName= Objects.requireNonNull(regName.getText()).toString();
            textMobile= Objects.requireNonNull(regPhoneNo.getText()).toString();

            ReadWriteUserDetails writeUserDetails=new ReadWriteUserDetails(textMobile);
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
            String UserId = firebaseUser.getUid();
            progressBar.setVisibility(View.VISIBLE);
            referenceProfile.child(UserId).setValue(writeUserDetails).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    UserProfileChangeRequest profileUpdate =new UserProfileChangeRequest.Builder().setDisplayName(textName).build();
                    firebaseUser.updateProfile(profileUpdate);
                    Toast.makeText(updateProfileActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(updateProfileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);finish();
                }
                else{
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }catch(Exception e)
                    {
                        Toast.makeText(updateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userIdOfRegistered = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userIdOfRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetail = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetail!=null)
                {
                    textMobile=readUserDetail.mobile;
                    textName=firebaseUser.getDisplayName();

                    regName.setText(textName);
                    regPhoneNo.setText(textMobile);
                }
                else {
                    Toast.makeText(updateProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(updateProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validatePhoneNo() {
        String phoneNumber = Objects.requireNonNull(regPhoneNo.getText()).toString();
        if (phoneNumber.isEmpty()) {
            regPhoneNoLayout.setError("Phone number is required");
            regPhoneNoLayout.requestFocus();
            return false;
        } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            regPhoneNoLayout.setError("Invalid phone number");
            regPhoneNoLayout.requestFocus();
            return false;
        } else {
            regPhoneNoLayout.setError(null);
            return true;
        }
    }
    private Boolean validateName(){
        String val = Objects.requireNonNull(regName.getText()).toString();
        if(val.isEmpty())
        {
            regNameLayout.setError("Field cannot be empty");
            regNameLayout.requestFocus();
            return false;
        }
        else
        {
            regNameLayout.setError(null);
            regNameLayout.setErrorEnabled(false);
            return true;
        }
    }
}