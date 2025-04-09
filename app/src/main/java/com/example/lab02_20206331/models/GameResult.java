package com.example.lab02_20206331.models;

public class GameResult {
    private String outcome;
    private String topic;
    private int time;
    private int attempts;

    public GameResult(String outcome, String topic, int time, int attempts) {
        this.outcome = outcome;
        this.topic = topic;
        this.time = time;
        this.attempts = attempts;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getTopic() {
        return topic;
    }

    public int getTime() {
        return time;
    }

    public int getAttempts() {
        return attempts;
    }
}
