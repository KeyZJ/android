package com.example.nikita.lab7;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class gameOverMenuActivity extends AppCompatActivity {
    static List<Integer> scoreList = new ArrayList<>(10);

    static public List<Integer> getScoreList() {
        return scoreList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_menu);
        genericActions.setFullScreenMode(this);
        (MediaPlayer.create(this, R.raw.lose)).start();
        readLeaderboards(this);
        int intentScore = getIntent().getIntExtra("score", 0);
        setScore(intentScore);
        addScoreToLeaderboards(intentScore);
        Log.i("gameOverMenuActivity", "[score = " + intentScore + "]");
    }

    public void readLeaderboards(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        scoreList.clear();
        int size = sharedPreferences.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            scoreList.add(Integer.parseInt(sharedPreferences.getString("Status_" + i, null)));
        }
    }

    public void setScore(int score) {
        TextView results = findViewById(R.id.results);
        results.setText(getString(R.string.fScore, score));
    }

    private boolean addScoreToLeaderboards(int score) {
        SharedPreferences sharedPreferences = gameOverMenuActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (scoreList.isEmpty()) {
            scoreList.add(score);
        } else {
            if (score > Collections.min(scoreList)) {
                if (scoreList.size() < 10) {
                    scoreList.add(score);
                } else {
                    for (int i = 0; i < scoreList.size(); i++) {
                        if (scoreList.get(i) < score) {
                            scoreList.set(i, score);
                        }
                    }
                }
            }
        }

        editor.putInt("Status_size", scoreList.size());
        for (int i = 0; i < scoreList.size(); i++) {
            editor.remove("Status_" + i);
            editor.putString("Status_" + i, Integer.toString(scoreList.get(i)));
        }
        scoreList = scoreList.stream().distinct().collect(Collectors.toList()); //To remove duplicates.
        boolean isCommitted = editor.commit();
        updateLeaderboards();
        return isCommitted;
    }

    private void updateLeaderboards() {
        Collections.sort(scoreList, Collections.reverseOrder());
        ListView listView = (ListView) findViewById(R.id.leaderboard_gameOverLayot);
        Integer[] data = scoreList.toArray(new Integer[0]);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    public void loadGameAgain(View view) {
        genericActions.startGame(this);
    }

    public void backToMainMenu(View view) {
        genericActions.backToMainMenu(this);
    }
}
