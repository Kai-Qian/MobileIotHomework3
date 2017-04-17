package com.cmu.willqian.alarmclockhue.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cmu.willqian.alarmclockhue.R;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * HueService - The starting point for creating your own Hue App.
 * Currently contains a simple view with a button to change your lights to random colours.  Remove this and add your own app implementation here! Have fun!
 * 
 * @author SteveyO
 *
 */
public class HueService extends Service {
    private PHHueSDK phHueSDK;
    private PHBridge bridge;
    public static final String TAG = "HUE";
    private final Timer timer = new Timer();
    private TimerTask task;
    private int brightness = 0;
    private List<PHLight> allLights;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (brightness + 20 <= 254) {
                        brightness += 20;
                        System.out.println(brightness);
                        for (PHLight light : allLights) {
                            PHLightState lightState = new PHLightState();
                            lightState.setBrightness(brightness);
                            bridge.updateLightState(light, lightState, listener);
                        }
                    } else timer.cancel();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate() {
        phHueSDK = PHHueSDK.create();
        bridge = phHueSDK.getSelectedBridge();
        allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setOn(true);
            lightState.setBrightness(0);
            bridge.updateLightState(light, lightState, listener);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task,1000,1000); //delay 1000ms before execution，execute every 1000ms
        return START_STICKY;
    }

    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {
        
        @Override
        public void onSuccess() {  
        }
        
        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
           Log.w(TAG, "Light has updated");
        }
        
        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };
    
    @Override
    public void onDestroy() {
        timer.cancel();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {
            
            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }
            
            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
