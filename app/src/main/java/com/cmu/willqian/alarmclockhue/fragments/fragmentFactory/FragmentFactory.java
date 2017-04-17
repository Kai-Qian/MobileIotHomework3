package com.cmu.willqian.alarmclockhue.fragments.fragmentFactory;

import android.support.v4.app.Fragment;

import com.cmu.willqian.alarmclockhue.fragments.AlarmClockFragment;
import com.cmu.willqian.alarmclockhue.fragments.ScenesFragment;

/**
 * Created by willQian on 4/8/17.
 */

public class FragmentFactory {
    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case 1:
                fragment = new AlarmClockFragment();
                break;
            case 2:
                fragment = new ScenesFragment();
                break;
        }
        return fragment;
    }
}
