package com.mymarketapps.eye20;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by gorillalogic on 11/7/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName compName = new ComponentName(context.getPackageName(), BeepService.class.getName());
        startWakefulService(context, intent.setComponent(compName));
        setResultCode(Activity.RESULT_OK);
    }
}
