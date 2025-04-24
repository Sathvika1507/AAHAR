package com.example.aahar100;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import java.util.Objects;

public class forgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText textUserEmail;
    private TextInputLayout emailLayout;
    private ProgressBar progressBar;
    private final static String TAG="forgotPasswordActivity";
    /** @noinspection StatementWithEmptyBody*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        AppCompatButton reset_pwd_btn = findViewById(R.id.id16_reset_pwd_btn);
        textUserEmail=findViewById(R.id.textUserEmail);
        emailLayout=findViewById(R.id.email);
        progressBar=findViewById(R.id.progressBar);
        reset_pwd_btn.setOnClickListener(view -> {
            if(!validateEmail()){
            }
            else{
                progressBar.setVisibility(View.VISIBLE);
                resetPassword();
            }

        });
    }
    private void resetPassword() {
        String email= Objects.requireNonNull(textUserEmail.getText()).toString();
        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                Toast.makeText(forgotPasswordActivity.this,"Please check your inbox" , Toast.LENGTH_LONG).show();
                Intent intent =new Intent(forgotPasswordActivity.this,landing_page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else
            {
                try{
                    throw Objects.requireNonNull(task.getException());
                }
                catch(FirebaseAuthInvalidCredentialsException e)
                {
                    emailLayout.setError("User does not exists or is no longer valid. Please register again.");
                    emailLayout.requestFocus();
                }
                catch(Exception e)
                {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(forgotPasswordActivity.this,e.getMessage() , Toast.LENGTH_LONG).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }
    private boolean validateEmail() {
        String emailString = Objects.requireNonNull(textUserEmail.getText()).toString();
        if (emailString.isEmpty()) {
            emailLayout.setError("Email address is required");
            emailLayout.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            emailLayout.setError("Invalid email address");
            emailLayout.requestFocus();
            return false;
        } else {
            emailLayout.setError(null);
            return true;
        }
    }

    public void registerUser(View view) {
    }
}