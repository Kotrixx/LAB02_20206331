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

    private Button playButton;
    private TextView attemptsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playButton = findViewById(R.id.btnJugar);
        attemptsTextView = findViewById(R.id.attemptsText);


        // Inicializa el botón de "Jugar"
        playButton.setText("Jugar");


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

        // Cuando presionan "Jugar"
        playButton.setOnClickListener(v -> startGame());  // Inicia el juego al presionar "Jugar"
    }

    private void startGame() {
        playButton.setText("Nuevo Juego");
        playButton.setOnClickListener(v -> restartGame());  // Cambia la funcionalidad al presionar "Nuevo Juego"

        startTime = SystemClock.elapsedRealtime();
        selectedPhrase = getRandomPhrase(topic);
        correctWords = selectedPhrase.split(" ");
        mixedWords = prepareShuffledWords(correctWords);

        // Inicializa los botones
        setupButtons();

        // Al principio no mostramos los intentos
        attemptsTextView.setVisibility(TextView.GONE);  // Oculta el TextView hasta que empiece el juego
    }

    private void restartGame() {
        attempts = 0;
        attemptsTextView.setVisibility(TextView.VISIBLE);  // Mostrar el contador de intentos al reiniciar el juego
        attemptsTextView.setText("Te quedan 3 intentos");

        selectedButtons.clear();
        selectedWords.clear();
        currentWordIndex = 0;
        buttons.clear();

        // Reiniciar el flujo de juego
        startGame();  // Llama nuevamente a `startGame` para reiniciar el flujo
    }


    private void setupButtons() {
        for (int i = 0; i < TOTAL_WORDS; i++) {
            int resId = getResources().getIdentifier("btn" + i, "id", getPackageName());
            Button btn = findViewById(resId);
            buttons.add(btn);

            // Inicialmente las palabras estarán ocultas
            btn.setText(""); // Botón vacío inicialmente
            btn.setEnabled(true); // El botón puede ser tocado

            final int finalIndex = i; // Necesario para el OnClickListener
            btn.setOnClickListener(v -> handleWordSelection(btn, finalIndex));
        }
    }

    private void handleWordSelection(Button btn, int buttonIndex) {
        String word = mixedWords.get(buttonIndex);

        // Muestra la palabra seleccionada aunque sea incorrecta
        btn.setText(word);  // Muestra la palabra seleccionada temporalmente

        // Si la palabra seleccionada es correcta
        if (word.equals(correctWords[currentWordIndex])) {
            btn.setEnabled(false);  // Deshabilita el botón si es correcta
            selectedButtons.add(btn);
            selectedWords.add(word);
            currentWordIndex++;

            if (currentWordIndex == correctWords.length) {
                long elapsed = (SystemClock.elapsedRealtime() - startTime) / 1000;
                attemptsTextView.setText(String.format("Ganaste / Terminó en %ds", elapsed));
                // showResult("Ganaste", elapsed, attempts);
                saveResult("Ganó %d", (int) elapsed);
            }

        } else {
            // Palabra es incorrecta
            attempts++; // Incrementa el contador de intentos
            attemptsTextView.setVisibility(TextView.VISIBLE);  // Asegurarse de que el contador de intentos esté visible
            attemptsTextView.setText("Te quedan " + (MAX_ATTEMPTS - attempts) + " intentos");

            // Restablecer la selección después de un error
            // Toast.makeText(this, "Incorrecto. Intentos restantes: " + (MAX_ATTEMPTS - attempts), Toast.LENGTH_SHORT).show();
            resetSelection(); // Restablece las palabras seleccionadas

            // Agotar los intentos
            if (attempts >= MAX_ATTEMPTS) {
                long elapsed = (SystemClock.elapsedRealtime() - startTime) / 1000;
                attemptsTextView.setText(String.format("Perdiste / Terminó en %ds", elapsed));

                // showResult("Perdiste", elapsed, attempts);
                saveResult("Perdió %d", (int) elapsed);
            }
        }
    }



    private void resetSelection() {
        // Si el jugador se equivoca se deshacen las selecciones anteriores
        currentWordIndex = 0;
        // Ocultar nuevamente
        for (Button b : selectedButtons) {
            b.setText("");  // Borra el texto
            b.setEnabled(true);
        }
        selectedButtons.clear();
        selectedWords.clear();
    }

    private void showResult(String message, long time, int attemptsUsed) {
        String finalMsg = message + "\nTiempo: " + time + "s";
        if (message.contains("Ganaste")) {
            finalMsg += "\nIntentos usados: " + attemptsUsed;
        }

        // Muestra el resultado antes de finalizar la actividad
        Toast.makeText(this, finalMsg, Toast.LENGTH_LONG).show();

        // Llama a finish() después de mostrar el resultado
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Aquí es donde terminamos la actividad de manera segura
        }, 3000);
    }


    private List<String> prepareShuffledWords(String[] words) {
        List<String> wordList = new ArrayList<>(Arrays.asList(words));
        while (wordList.size() < TOTAL_WORDS) wordList.add(""); // Agrega palabras vacías si hay menos de 12
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
        StorageHelper.saveResult(this, result);  // Guardar el resultado en SharedPreferences o base de datos
    }

}
