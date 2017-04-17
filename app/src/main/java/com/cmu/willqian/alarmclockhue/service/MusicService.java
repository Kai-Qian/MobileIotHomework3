package com.cmu.willqian.alarmclockhue.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cmu.willqian.alarmclockhue.R;

/**
 * Created by willQian on 4/9/17.
 */

public class MusicService extends Service {
    private MediaPlayer player;

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        player = MediaPlayer.create(this, R.raw.music);
        player.setLooping(true);
        player.start();
        return START_STICKY;
    }
    public void onDestroy()
    {
        super.onDestroy();
        if(player != null){
            player.stop();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}