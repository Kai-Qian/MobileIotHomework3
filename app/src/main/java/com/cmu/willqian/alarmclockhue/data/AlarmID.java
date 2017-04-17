package com.cmu.willqian.alarmclockhue.data;

/**
 * Created by willQian on 4/12/17.
 */

public class AlarmID {
    private static int ID = 0;

    public static int getID() {
        int tmp = ID;
        ID++;
        return tmp;
    }
}
