package com.cmu.willqian.alarmclockhue.data;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.cmu.willqian.alarmclockhue.R;
import com.cmu.willqian.alarmclockhue.activities.MainActivity;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;

/**
 * Created by willQian on 4/12/17.
 */

public class AlarmClockAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;

    class AlarmClockListItem {
        private TextView textView;
        private ImageButton imageButton;
    }

    public AlarmClockAdapter(Context context) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return MainActivity.alarmClocks.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.alarmClocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AlarmClockAdapter.AlarmClockListItem item;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.alarmclock_item, null);
            item = new AlarmClockAdapter.AlarmClockListItem();
            item.textView = (TextView) convertView.findViewById(R.id.alarm_clock2);
            item.imageButton = (ImageButton) convertView.findViewById(R.id.btnDelete);

            convertView.setTag(item);
        }
        else {
            item = (AlarmClockListItem) convertView.getTag();
        }
        AlarmClock alarmClock = MainActivity.alarmClocks.get(position);
        String str = alarmClock.getTime();
        item.textView.setText(str);
        item.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("alarm_receiver");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, MainActivity.alarmClocks.get(position).getID(), intent,PendingIntent.FLAG_CANCEL_CURRENT);
                MainActivity.alarmManager.cancel(pendingIntent);
                MainActivity.alarmClocks.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void updateData() {
        notifyDataSetChanged();
    }
}
