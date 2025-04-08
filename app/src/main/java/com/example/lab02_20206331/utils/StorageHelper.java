package com.example.lab02_20206331.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lab02_20206331.models.GameResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class StorageHelper {
    private static final String PREFS_NAME = "GameStats";
    private static final String KEY_RESULTS = "results";

    public static void saveResult(Context context, GameResult result) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> current = prefs.getStringSet(KEY_RESULTS, new HashSet<>());
        Set<String> updated = new HashSet<>(current);
        updated.add(result.toString());
        prefs.edit().putStringSet(KEY_RESULTS, updated).apply();
    }

    public static List<String> getAllResults(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> results = prefs.getStringSet(KEY_RESULTS, new HashSet<>());
        return new ArrayList<>(results);
    }

    public static void clearResults(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_RESULTS).apply();
    }
}
