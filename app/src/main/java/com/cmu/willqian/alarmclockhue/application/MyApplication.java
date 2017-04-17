package com.cmu.willqian.alarmclockhue.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by willQian on 4/9/17.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}