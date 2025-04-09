package com.example.lab02_20206331.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab02_20206331.R;
import com.example.lab02_20206331.utils.StorageHelper;

import java.util.ArrayList;
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

        // Obtener los resultados guardados
        List<String> results = StorageHelper.getAllResults(this);

        List<String> formattedResults = formatResults(results);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, formattedResults);
        resultsListView.setAdapter(adapter);

        newGameButton.setOnClickListener(v -> {
            // Volver a la pantalla de inicio
            finish();
        });
    }

    private List<String> formatResults(List<String> results) {
        List<String> formattedResults = new ArrayList<>();
        int gameNumber = 1;
        for (String result : results) {
            String[] parts = result.split(",");
            String outcome = parts[0];
            String time = parts[1];
            String attempts = parts.length > 2 ? "Intentos: " + parts[2] : "";

            String formatted = "Juego " + gameNumber + ": " + outcome + " / " + time;
            if (!attempts.isEmpty()) {
                formatted += "\n" + attempts;
            }

            formattedResults.add(formatted);
            gameNumber++;
        }
        return formattedResults;
    }
}
