package com.example.nikita.lab7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class mainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        genericActions.setFullScreenMode(this);
    }

    public void startGame(View view) {
        Log.i("mainMenuActivity", "[startGame button pressed");
        genericActions.startGame(this);
    }

    public void showLeaderboard(View view) {
        Log.i("mainMenuActivity", "[showLeaderboard button pressed");
        startActivity(new Intent(this, leaderboardActivity.class));
    }

    public void showSettings(View view) {
        Log.i("mainMenuActivity", "[openSettings button pressed");
        startActivity(new Intent(this, settingsActivity.class));
    }
}
