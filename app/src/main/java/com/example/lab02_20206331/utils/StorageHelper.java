package com.example.lab02_20206331.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lab02_20206331.models.GameResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StorageHelper {

    private static final String PREFS_NAME = "game_results";
    private static final String RESULTS_KEY = "results";

    public static void saveResult(Context context, GameResult result) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        List<String> results = getAllResults(context);

        // Agregar nuevo resultado
        String formattedResult = result.getOutcome() + "," + "Termino en " + result.getTime() + "s" +
                (result.getAttempts() > 0 ? "," + result.getAttempts() : "");

        results.add(formattedResult);

        // Guardar los resultados
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(RESULTS_KEY, new HashSet<>(results));
        editor.apply();
    }

    public static List<String> getAllResults(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> resultSet = sharedPreferences.getStringSet(RESULTS_KEY, new HashSet<>());
        return new ArrayList<>(resultSet);
    }
}
