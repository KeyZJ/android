package com.example.nikita.lab7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class pauseMenuActivity extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_pause_menu);

        bundle = getIntent().getBundleExtra("bundle");
        int score = bundle.getInt("score");
        Log.i("pauseMenuActivity", "[score = " + score + "]");
        setScore(score);
    }

    public void setScore(int score) {
        TextView results = findViewById(R.id.pause_results);
        results.setText(getString(R.string.fScore, score));
    }

    public void resumeGame(View view) {
        Intent resumeGameIntent = new Intent(this, gameActivity.class);
        resumeGameIntent.putExtra("bundle", bundle);
        startActivity(resumeGameIntent);
    }

    public void backToMainMenu(View view) {
        startActivity(new Intent(this, mainMenuActivity.class));
    }
}
