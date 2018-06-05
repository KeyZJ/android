package com.example.nikita.lab7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class leaderboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        genericActions.setFullScreenMode(this);
        loadTable();
    }

    private void loadTable() {
        List<Integer> scores = gameOverMenuActivity.getScoreList();
        ListView listView = (ListView) findViewById(R.id.leaderboard_leaderboardLayout);
        Integer[] data = scores.toArray(new Integer[0]);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    public void backToMainMenu(View view) {
        genericActions.backToMainMenu(this);
    }
}
