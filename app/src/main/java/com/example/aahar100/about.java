package com.example.aahar100;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.Toast;

public class about extends AppCompatActivity {

    CardView facebook , twitter ,instagram,version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        facebook=findViewById(R.id.facebook);
        twitter=findViewById(R.id.twitter);
        instagram=findViewById(R.id.instagram);
        version=findViewById(R.id.version);

        version.setOnClickListener(v -> Toast.makeText(about.this, "No update available ", Toast.LENGTH_SHORT).show());
        twitter.setOnClickListener(v -> Toast.makeText(about.this, "Currently we are not active in Twitter", Toast.LENGTH_SHORT).show());
        instagram.setOnClickListener(v -> Toast.makeText(about.this, "Currently we are not active in Instagram", Toast.LENGTH_SHORT).show());
        facebook.setOnClickListener(v -> Toast.makeText(about.this, "Currently we are not active in FaceBook", Toast.LENGTH_SHORT).show());
    }

}