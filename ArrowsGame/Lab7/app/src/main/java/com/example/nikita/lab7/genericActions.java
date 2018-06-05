package com.example.nikita.lab7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class genericActions {
    public static void setFullScreenMode(AppCompatActivity activity){
        Log.i("genericActions", "[setFullScreenMode");
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        activity.getSupportActionBar().hide();
    }
    public static void backToMainMenu(AppCompatActivity activity) {
        Log.i("genericActions", "[startActivity: mainMenuActivity.class");
        activity.startActivity(new Intent(activity, mainMenuActivity.class));
    }
    public static void startGame(AppCompatActivity activity){
        activity.startActivity(new Intent(activity, gameActivity.class));
    }
}
