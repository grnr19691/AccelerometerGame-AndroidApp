package com.example.accelerometer.accgame;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.os.Bundle;
import android.app.Activity;
import android.os.PowerManager;



public class MainActivity extends Activity {
    private static final String TAG = "com.example.rajanageswararao.acceleration.MainActivity";
    MediaPlayer player;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager mPowerManager;
    private SimulationView mSimulationView;
    // private boolean musicON=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        player = MediaPlayer.create(MainActivity.this, R.raw.bg);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        player.start();
        mSimulationView = new SimulationView(this);
        setContentView(mSimulationView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mSimulationView.startSimulation();
        player.start();
        //player.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSimulationView.stopSimulation();
        mWakeLock.release();
        //player.stop();
        player.pause();
    }
    protected void onDestroy()
    {
        super.onDestroy();
        player.pause();
        player.release();
    }
}