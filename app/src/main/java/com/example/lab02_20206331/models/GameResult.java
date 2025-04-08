package com.example.lab02_20206331.models;

public class GameResult {
    public String outcome; // Ganó / Perdió / Cancelado
    public String topic;
    public int timeInSeconds;
    public int attempts;

    public GameResult(String outcome, String topic, int timeInSeconds, int attempts) {
        this.outcome = outcome;
        this.topic = topic;
        this.timeInSeconds = timeInSeconds;
        this.attempts = attempts;
    }

    @Override
    public String toString() {
        if (outcome.equals("Cancelado")) {
            return "Cancelado | Tema: " + topic;
        } else if (outcome.equals("Ganó")) {
            return "Ganó | Tema: " + topic + " | Tiempo: " + timeInSeconds + "s | Intentos: " + attempts;
        } else {
            return "Perdió | Tema: " + topic + " | Tiempo: " + timeInSeconds + "s";
        }
    }
}
