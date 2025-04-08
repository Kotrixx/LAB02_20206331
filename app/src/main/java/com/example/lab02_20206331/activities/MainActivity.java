package com.example.lab02_20206331.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.lab02_20206331.R;

public class MainActivity extends AppCompatActivity {

    Button btnSoftware, btnCybersecurity, btnOptical;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSoftware = findViewById(R.id.software_option);
        btnCybersecurity = findViewById(R.id.cybersecurity_option);
        btnOptical = findViewById(R.id.optical_option);

        btnSoftware.setOnClickListener(v -> {
            openGameWithTopic("software");
        });

        btnCybersecurity.setOnClickListener(v -> {
            openGameWithTopic("cybersecurity");
        });

        btnOptical.setOnClickListener(v -> {
            openGameWithTopic("optical");
        });
    }

    private void openGameWithTopic(String topic) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }
}