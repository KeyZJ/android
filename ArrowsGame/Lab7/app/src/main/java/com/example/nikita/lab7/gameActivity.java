package com.example.nikita.lab7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class gameActivity extends AppCompatActivity {
    private int score = 0;
    private int turn = 0;
    private int currentArrow;
    private List<String> colorList;
    private int delay = 1000;
    private int period = 1000;
    private MediaPlayer soundtrack;
    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (getIntent().getBundleExtra("bundle") != null) {
            onRestoreInstanceState(getIntent().getBundleExtra("bundle"));
        }
        genericActions.setFullScreenMode(this);
        initHandler();
        fillColours();
        startOST();
        //getNextArrow();
        startGenerator();
    }

    @SuppressLint("HandlerLeak")
    public void initHandler() { //Used to track turns.
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                updateTurn();
            }
        };
    }

    private void updateTurn() {
        TextView turnView = findViewById(R.id.turnTextViewGameLayout);
        turn++;
        turnView.setText(getString(R.string.fTurn, turn));
    }

    public void fillColours() {
        String lightBlue = "#03A9F4";
        String teal = "#009688";
        String deepPurple = "#673AB7";
        String green = "#4CAF50";
        String blueGray = "#607D8B";
        String red = "#F44336";
        String pink = "#E91E63";
        this.colorList = new ArrayList<>();
        this.colorList.add(lightBlue);
        this.colorList.add(teal);
        this.colorList.add(deepPurple);
        this.colorList.add(green);
        this.colorList.add(blueGray);
        this.colorList.add(red);
        this.colorList.add(pink);
    }

    public void startOST() {
        soundtrack = MediaPlayer.create(this, R.raw.soundtrack);
        soundtrack.start();
        soundtrack.setLooping(true);
    }

    private void getNextArrow() {
        Random rnd = new Random(System.currentTimeMillis());
        int min = 1, max = 8;
        currentArrow = min + rnd.nextInt(max - min + 1);
        Log.i("gameActivity", "[currentArrow generated] = " + currentArrow);
        ImageView image = (ImageView) findViewById(R.id.gameFieldImageView);
        switch (currentArrow) {
            case 1:
                image.setImageResource(R.drawable.arrow_top_24dp);
                break;
            case 2:
                image.setImageResource(R.drawable.arrow_bottom_24dp);
                break;
            case 3:
                image.setImageResource(R.drawable.arrow_left_24dp);
                break;
            case 4:
                image.setImageResource(R.drawable.arrow_right_24dp);
                break;
            case 5:
                image.setImageResource(R.drawable.arrow_invs_top_24dp);
                break;
            case 6:
                image.setImageResource(R.drawable.arrow_invs_bottom_24dp);
                break;
            case 7:
                image.setImageResource(R.drawable.arrow_invs_left_24dp);
                break;
            case 8:
                image.setImageResource(R.drawable.arrow_invs_right_24dp);
                break;
        }
    }

    void startGenerator() {
        Timer t = new Timer();
        getNextArrow();

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                getNextArrow();
                h.sendEmptyMessage(1);
            }
        }, delay, period);
    }

    public void topPressed(View view) {
        Log.i("gameActivity", "[Top button pressed]");
        checkAnswer(1, 6);
    }

    public void botPressed(View view) {
        Log.i("gameActivity", "[Bot button pressed]");
        checkAnswer(2, 5);
    }

    public void leftPressed(View view) {
        Log.i("gameActivity", "[Left button pressed]");
        checkAnswer(3, 8);
    }

    public void rightPressed(View view) {
        Log.i("gameActivity", "[Right button pressed]");
        checkAnswer(4, 7);
    }

    private void checkAnswer(int a1, int a2) {
        if (currentArrow == a1 || currentArrow == a2) {
            (MediaPlayer.create(this, R.raw.beep_short)).start();
            updateScore();
            Log.i("gameActivity", "[score % 5 = " + score % 5 + "]");
            if (this.score % 5 == 0 && score != 0) {
                Log.i("gameActivity", "[Changing colours]");
                changeColours();
                //TODO: fix this
                // There is problem: looks like that it's illegal to concurrently findViewById()
                //or to change any View from the layout.
            }
        } else {
            endGame(findViewById(R.id.pauseButtonGameLayout));
        }
    }

    private void updateScore() {
        TextView scoreView = findViewById(R.id.scoreTextViewGameLayout);
        this.score++;
        scoreView.setText(getString(R.string.fScore, score));
    }

    private void changeColours() {
        RelativeLayout br = findViewById(R.id.game_main_layout);
        Random rnd = new Random(System.currentTimeMillis());
        int min = 0, max = this.colorList.size();
        int currentColor = min + rnd.nextInt(max - min + 1);
        br.setBackgroundColor(Color.parseColor(this.colorList.get(currentColor)));
    }

    private void endGame(View view) {
        Log.i("gameActivity", "[GAME OVER]");
        Intent endGameIntent = new Intent(this, gameOverMenuActivity.class);
        endGameIntent.putExtra("score", score);
        startActivity(endGameIntent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void pauseGame(View view) {
        onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent gamePauseIntent = new Intent(this, pauseMenuActivity.class);
        Bundle bundle = new Bundle();
        onSaveInstanceState(bundle);
        gamePauseIntent.putExtra("bundle", bundle);
        startActivity(gamePauseIntent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", this.score);
        outState.putInt("turn", this.turn);
        outState.putInt("currentArrow", this.currentArrow);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.score = savedInstanceState.getInt("score");
        this.turn = savedInstanceState.getInt("turn");
        this.currentArrow = savedInstanceState.getInt("currentArrow");
        Log.i("gameActivity", "[score = " + this.score + "]");
        Log.i("gameActivity", "[turn = " + this.turn + "]");
        Log.i("gameActivity", "[currentArrow = " + this.currentArrow + "]");

        TextView scoreView = findViewById(R.id.scoreTextViewGameLayout);
        scoreView.setText(getString(R.string.fScore, score));

        TextView turnView = findViewById(R.id.turnTextViewGameLayout);
        turnView.setText(getString(R.string.fTurn, turn));
    }
}
//TODO: fix bug here;
//TODO: think more about Leaderboards !!