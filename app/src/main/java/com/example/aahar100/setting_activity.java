package com.example.aahar100;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

public class setting_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        CardView menu_update_profile = findViewById(R.id.menu_update_profile);
        CardView menu_update_email = findViewById(R.id.menu_update_email);
        CardView menu_change_password = findViewById(R.id.menu_change_password);
        CardView cardAboutUs = findViewById(R.id.cardAboutUs);
        CardView cardContact = findViewById(R.id.cardContact);
        CardView menu_delete_profile = findViewById(R.id.menu_delete_profile);

        menu_update_profile.setOnClickListener(view -> {
            Intent intent = new Intent(setting_activity.this, updateProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        menu_update_email.setOnClickListener(view -> {
            Intent intent = new Intent(setting_activity.this, updateEmailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        menu_change_password.setOnClickListener(view -> {
            Intent intent = new Intent(setting_activity.this, changePasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        cardAboutUs.setOnClickListener(view -> {
            Intent intent = new Intent(setting_activity.this, about.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        cardContact.setOnClickListener(view -> showAlertDialogBox());
        menu_delete_profile.setOnClickListener(view -> {
            Intent intent = new Intent(setting_activity.this, deleteProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void showAlertDialogBox() {
        //setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(setting_activity.this);
        builder.setTitle("Contact Us");
        builder.setMessage("Write a mail to our gmail id tarun.utkarsh125@gamil.com");
        builder.setPositiveButton("Open Gmail", (dialog, which) -> {
            // Open Gmail app
            Intent intent =new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();
        //change the button color (continue->blue)
        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.light_blue));
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.drawable.button_bg1);

        });
        //Show the AlertDialog
        alertDialog.show();
    }
}