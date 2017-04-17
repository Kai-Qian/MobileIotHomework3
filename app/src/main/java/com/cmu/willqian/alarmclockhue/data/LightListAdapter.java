package com.cmu.willqian.alarmclockhue.data;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.cmu.willqian.alarmclockhue.R;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;

/**
 * Created by willQian on 4/9/17.
 */

public class LightListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<PHLight> phLights;
    private PHHueSDK phHueSDK;
    private PHBridge bridge;
    public static final String TAG = "HUE";

    class LightListItem {
        private Switch light;
        private SeekBar seekBar;
    }

    public LightListAdapter(Context context, List<PHLight> phLights) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        this.phLights = phLights;
        phHueSDK = PHHueSDK.create();
        bridge = phHueSDK.getSelectedBridge();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final LightListItem item;

//        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.light_item, null);

            item = new LightListItem();
            item.light = (Switch) convertView.findViewById(R.id.switch1);
            item.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);

//            convertView.setTag(item);
//        }
//        else {
//            item = (LightListItem) convertView.getTag();
//        }


        final PHLight phLight = phLights.get(position);
        final PHLightState lastLightState = phLight.getLastKnownLightState();
        item.light.setText(phLight.getName());

        // Initialization. Initialize the switch and seekbar status
        if(lastLightState.isReachable()) {
            if(lastLightState.isOn()) {
                item.light.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    item.seekBar.setProgress(lastLightState.getBrightness() * 100 / 254, true);
                } else {
                    item.seekBar.setProgress(lastLightState.getBrightness() * 100 / 254);
                }
            } else {
                item.light.setChecked(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    item.seekBar.setProgress(0, true);
                } else {
                    item.seekBar.setProgress(0);
                }
            }
        } else {
            item.light.setTextColor(Color.GRAY);
            item.light.setChecked(false);
        }
        //Set the seek bar listener
        item.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PHLightState lightState = new PHLightState();
                lightState.setBrightness(progress * 254 / 100);
                bridge.updateLightState(phLight, lightState, listener);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Set the switch listener
        item.light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PHLightState lightState = new PHLightState();
                if(isChecked) {
                    lightState.setOn(true);
                    lightState.setBrightness(item.seekBar.getProgress() * 254 / 100);
                } else {
                    lightState.setOn(false);
                }
                bridge.updateLightState(phLight, lightState, listener);
            }
        });
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return phLights.size();
    }

    @Override
    public Object getItem(int position) {
        return phLights.get(position);
    }

    public void updateData(List<PHLight> phLights) {
        this.phLights = phLights;
        notifyDataSetChanged();
    }

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
}
