package com.cmu.willqian.alarmclockhue.data;


import android.support.annotation.NonNull;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by willQian on 4/12/17.
 */

public class AlarmClock implements Comparable<AlarmClock> {

    private Calendar calendar;
    private int ID;

    public AlarmClock(int year, int month, int day, int hour_of_day, int minute, int ID) {
        calendar =Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour_of_day);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.set(Calendar.AM_PM, AM_PM); Cannot both set the hour_of_day and AM_PM togerther, will be wrong
        this.ID = ID;
    }

    public String getTime() {
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
        return format.format(date);
    }

    public int getID() {
        return ID;
    }

    @Override
    public int compareTo(@NonNull AlarmClock o) {
        return this.calendar.compareTo(o.calendar);
    }
}
