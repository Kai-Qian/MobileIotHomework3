package com.cmu.willqian.alarmclockhue.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.cmu.willqian.alarmclockhue.activities.MainActivity;
import com.cmu.willqian.alarmclockhue.application.MyApplication;

/**
 * Created by willQian on 4/9/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private Intent musicService;
    private Intent hueService;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        musicService = new Intent("music_service");
        hueService = new Intent("hue_service");
        context.startService(musicService);
        context.startService(hueService);

        dialog();
    }

    private void dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.mContext);
        dialogBuilder.setMessage("Stop the alarm");
        dialogBuilder.setPositiveButton("STOP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getContext().stopService(musicService);
                MyApplication.getContext().stopService(hueService);
                dialog.dismiss();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();
    }


}
