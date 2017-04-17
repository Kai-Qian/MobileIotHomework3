package com.cmu.willqian.alarmclockhue.activities;

import android.app.AlarmManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cmu.willqian.alarmclockhue.R;
import com.cmu.willqian.alarmclockhue.data.AlarmClock;
import com.cmu.willqian.alarmclockhue.fragments.AlarmClockFragment;
import com.cmu.willqian.alarmclockhue.fragments.ScenesFragment;
import com.cmu.willqian.alarmclockhue.fragments.fragmentFactory.FragmentFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AlarmClockFragment.OnFragmentInteractionListener, ScenesFragment.OnFragmentInteractionListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    public static Context mContext;
    private Fragment alarmFragment;
    private Fragment sceneFragment;
    public static List<AlarmClock> alarmClocks;
    public static AlarmManager alarmManager;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = manager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_alarm:
                    transaction.replace(R.id.content, alarmFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_scene:
                    transaction.replace(R.id.content, sceneFragment);
                    transaction.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        alarmClocks = new ArrayList<AlarmClock>();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        alarmFragment = FragmentFactory.getInstanceByIndex(1);
        sceneFragment = FragmentFactory.getInstanceByIndex(2);

        transaction.replace(R.id.content, alarmFragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
