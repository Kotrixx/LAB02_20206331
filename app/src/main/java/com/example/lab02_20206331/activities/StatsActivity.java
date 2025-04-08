package com.example.lab02_20206331.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab02_20206331.R;
import com.example.lab02_20206331.utils.StorageHelper;

import java.util.List;

public class StatsActivity extends AppCompatActivity {

    ListView resultsListView;
    Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        resultsListView = findViewById(R.id.resultsListView);
        newGameButton = findViewById(R.id.btnNewGame);

        List<String> results = StorageHelper.getAllResults(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, results);

        resultsListView.setAdapter(adapter);

        newGameButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
