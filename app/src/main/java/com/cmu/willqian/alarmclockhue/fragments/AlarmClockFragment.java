package com.cmu.willqian.alarmclockhue.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cmu.willqian.alarmclockhue.R;
import com.cmu.willqian.alarmclockhue.activities.MainActivity;
import com.cmu.willqian.alarmclockhue.data.AlarmClock;
import com.cmu.willqian.alarmclockhue.data.AlarmClockAdapter;
import com.cmu.willqian.alarmclockhue.data.AlarmID;
import com.philips.lighting.hue.sdk.PHHueSDK;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmClockFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AlarmClockFragment extends Fragment {

    private TextView textClock;
    private int year;
    private int month;
    private int day;
    private int hour_of_day;
    private int minute;
//    private int AM_PM;
    private Context mContext;
    private AlarmClockAdapter adapter;
    private boolean flag_date = false;
    private boolean flag_time = false;

    private OnFragmentInteractionListener mListener;

    public AlarmClockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_clock, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textClock = (TextView) getActivity().findViewById(R.id.tclock);
        mContext = getContext();
        Calendar mCalendar= Calendar.getInstance(Locale.US);
        Date mydate=new Date();
        mCalendar.setTime(mydate);
        year=mCalendar.get(Calendar.YEAR);
        month=mCalendar.get(Calendar.MONTH) + 1;//Month starts from 0
        day=mCalendar.get(Calendar.DAY_OF_MONTH);
        hour_of_day = mCalendar.get(Calendar.HOUR_OF_DAY);
        minute = mCalendar.get(Calendar.MINUTE);
//        AM_PM = mCalendar.get(Calendar.AM_PM);
//        if(AM_PM == 0) textClock.setText(year + "/" + month + "/" + day + " " + hour_of_day + ":" + minute + " AM");
//        else {
//            if(hour_of_day == 0) hour_of_day = 12;
//            textClock.setText(year + "/" + month + "/" + day + " " + hour_of_day + ":" + minute + " PM");
//        }
        textClock.setText(year + "/" + month + "/" + day + " " + hour_of_day + ":" + minute);
        textClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, mDatePickerDialogListener, year, month - 1, day);
                datePickerDialog.setTitle("Please choose the date");
                datePickerDialog.show();
            }
        });

        adapter = new AlarmClockAdapter(getActivity());
        ListView alarmClockList = (ListView) getActivity().findViewById(R.id.alarm_clock_list);
        alarmClockList.setAdapter(adapter);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private DatePickerDialog.OnDateSetListener mDatePickerDialogListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year_tmp, int month_tmp, int dayOfMonth_tmp) {
            if(!flag_date) {
                year = year_tmp;
                month = month_tmp + 1;
                day = dayOfMonth_tmp;
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, mTimePickerDialogListener, hour_of_day, minute, false);
                timePickerDialog.setTitle("Please choose the time");
                timePickerDialog.show();
                flag_date = true;
            } else flag_date = false;
        }

    };

    private TimePickerDialog.OnTimeSetListener mTimePickerDialogListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            if(!flag_time) {
                hour_of_day = hourOfDay;
                minute = minuteOfHour;
                StringBuilder str = new StringBuilder();
//                if(hour_of_day == 12) str.append(year + "/" + month + "/" + day + " " + hour_of_day + ":" + minute + " PM");
//                else if(hour_of_day > 12 && hour_of_day < 24) str.append(year + "/" + month + "/" + day + " " + (hour_of_day - 12) + ":" + minute + " PM");
//                else str.append(year + "/" + month + "/" + day + " " + hour_of_day + ":" + minute + " AM");
                str.append(year + "/" + month + "/" + day + " " + hour_of_day + ":" + minute);
                textClock.setText(str);

                AlarmClock alarmClock = new AlarmClock(year, month - 1, day, hour_of_day, minute, AlarmID.getID());
                MainActivity.alarmClocks.add(alarmClock);
                Collections.sort(MainActivity.alarmClocks);
                sendAlarm(mContext, year, month - 1, day, hour_of_day, minute, alarmClock.getID());
                adapter.updateData();
                flag_time = true;
            } else flag_time = false;
        }
    };

    private void sendAlarm(Context context, int year, int month, int day, int hour, int minute, int ID){
        Calendar calendar =Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent();
        intent.setAction("alarm_receiver");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        MainActivity.alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }
}
