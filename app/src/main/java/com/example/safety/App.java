package com.example.safety;

import android.app.Application;
import android.content.Intent;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startForegroundService(new Intent(this, BackService.class));
    }
}