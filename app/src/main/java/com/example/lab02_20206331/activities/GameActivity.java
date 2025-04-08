package com.example.lab02_20206331.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab02_20206331.R;
import com.example.lab02_20206331.activities.StatsActivity;
import com.example.lab02_20206331.models.GameResult;
import com.example.lab02_20206331.utils.StorageHelper;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.*;

public class GameActivity extends AppCompatActivity {

    private static final int MAX_ATTEMPTS = 3;
    private static final int TOTAL_WORDS = 12;

    private String[] correctWords;
    private List<String> mixedWords;
    private List<Button> buttons = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private List<Button> selectedButtons = new ArrayList<>();

    private int currentWordIndex = 0;
    private int attempts = 0;
    private long startTime;

    private String selectedPhrase;
    private String topic;
    private int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            saveResult("Cancelado", 0);
            finish();
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_stats) {
                startActivity(new Intent(this, StatsActivity.class));
                return true;
            }
            return false;
        });

        topic = getIntent().getStringExtra("topic");
        startTime = SystemClock.elapsedRealtime();

        selectedPhrase = getRandomPhrase(topic);
        correctWords = selectedPhrase.split(" ");
        mixedWords = prepareShuffledWords(correctWords);

        setupButtons();
    }

    private void setupButtons() {
        for (int i = 0; i < TOTAL_WORDS; i++) {
            int resId = getResources().getIdentifier("btn" + i, "id", getPackageName());
            Button btn = findViewById(resId);
            buttons.add(btn);

            String word = mixedWords.get(i);
            btn.setText(word);
            btn.setEnabled(!word.isEmpty());

            btn.setOnClickListener(v -> handleWordSelection(btn));
        }
    }

    private void handleWordSelection(Button btn) {
        String word = btn.getText().toString();

        if (word.equals(correctWords[selectedIndex])) {
            btn.setEnabled(false);
            selectedButtons.add(btn);
            selectedIndex++;
            if (selectedIndex == correctWords.length) {
                long elapsed = (SystemClock.elapsedRealtime() - startTime) / 1000;
                showResult("Ganaste 🎉", elapsed, attempts);
                saveResult("Ganó", (int) elapsed);
            }

        } else {
            attempts++;
            Toast.makeText(this, "Incorrecto. Intentos restantes: " + (MAX_ATTEMPTS - attempts), Toast.LENGTH_SHORT).show();
            resetSelection();

            if (attempts >= MAX_ATTEMPTS) {
                long elapsed = (SystemClock.elapsedRealtime() - startTime) / 1000;
                showResult("Perdiste 😞", elapsed, attempts);
                saveResult("Perdió", (int) elapsed);
            }
        }
    }


    private void resetSelection() {
        selectedIndex = 0;
        for (Button btn : selectedButtons) {
            btn.setEnabled(true); // Reactiva
        }
        selectedButtons.clear();
    }


    private void showResult(String message, long time, int attemptsUsed) {
        String finalMsg = message + "\nTiempo: " + time + "s";
        if (message.contains("Ganaste")) {
            finalMsg += "\nIntentos usados: " + attemptsUsed;
        }

        Toast.makeText(this, finalMsg, Toast.LENGTH_LONG).show();

        // Después de 2 segundos, regresar a inicio
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    private List<String> prepareShuffledWords(String[] words) {
        List<String> wordList = new ArrayList<>(Arrays.asList(words));
        while (wordList.size() < TOTAL_WORDS) wordList.add("");
        Collections.shuffle(wordList);
        return wordList;
    }

    private String getRandomPhrase(String topicKey) {
        Map<String, String[]> phrases = new HashMap<>();
        phrases.put("software", new String[]{
                "La fibra óptica envía datos a gran velocidad evitando cualquier interferencia eléctrica",
                "Los amplificadores EDFA mejoran la señal óptica en redes de larga distancia"
        });
        phrases.put("cybersecurity", new String[]{
                "Una VPN encripta tu conexión para navegar de forma anónima y segura",
                "El ataque DDoS satura servidores con tráfico falso y causa caídas masivas"
        });
        phrases.put("optical", new String[]{
                "Los fragments reutilizan partes de pantalla en distintas actividades de la app",
                "Los intents permiten acceder a apps como la cámara o WhatsApp directamente"
        });

        String[] options = phrases.getOrDefault(topicKey, new String[]{"Frase no encontrada"});
        return options[new Random().nextInt(options.length)];
    }

    private void saveResult(String outcome, int time) {
        GameResult result = new GameResult(outcome, topic, time, attempts);
        StorageHelper.saveResult(this, result);
    }
}
