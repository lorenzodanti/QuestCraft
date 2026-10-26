package it.unicam.cs.mpgc.rpg126280.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardManager {
    private final Gson gson;
    private final String filePath;

    public LeaderboardManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.filePath = "leaderboard.json";
    }

    public List<ScoreEntry> loadScores() {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<ScoreEntry>>() {}.getType();
            List<ScoreEntry> scores = gson.fromJson(reader, listType);
            return scores != null ? scores : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void addScore(String playerName, int score, int monstersDefeated, int levelReached) {
        List<ScoreEntry> scores = loadScores();
        scores.add(new ScoreEntry(playerName, score, monstersDefeated, levelReached));
        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(scores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}