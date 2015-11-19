package com.mymarketapps.eye20;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import com.mymarketapps.eye20.model.Store;

/**
 * Created by gorillalogic on 11/7/15.
 */
public class BeepService extends Service {

    private Intent mIntent;
    private boolean is20SecTimerStart;
    private Store myStore;

    @Override
    public void onCreate() {
        super.onCreate();
        myStore = new Store(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIntent = intent;
        if (mIntent != null && mIntent.getBooleanExtra("is_rebooted", false)) {
            reStart20Alarm();
            tearDown();
        } else {
            is20SecTimerStart = true;
            playBeep(R.raw.beep_start);
        }
        return START_STICKY;
    }

    private void reStart20Alarm() {
        Store myStore = new Store(this);
        if (myStore.isTimerStarted()) {
            // restart timer
            AlarmManager almgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            almgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 20
                    , PendingIntent.getBroadcast(this, 0, new Intent("com.mymarketapps.eye20.action.AlarmReceiver"), 0));
        }
    }

    private void playBeep(int res) {
        if (myStore.isSoundOn()) {
            MediaPlayer mp = MediaPlayer.create(this, res);
            mp.setVolume(myStore.getVolume()/100f, myStore.getVolume()/100f);
            mp.start();
        }

        Handler h = new Handler();
        if (is20SecTimerStart) {
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start20SecTimer();
                }
            }, 1000 * 1);
        } else {
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tearDown();
                }
            }, 1000 * 2);
        }

        if (myStore.isVibrationOn())
            vibrate();
    }

    private void vibrate() {
        final Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0, 250, 500};
        v.vibrate(pattern, 0);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.cancel();
            }
        }, 1250);
    }

    private void start20SecTimer() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                is20SecTimerStart = false; // finish
                if (myStore.isTimerStarted()) {
                    playBeep(R.raw.beep_finish);
                } else {
                    tearDown();
                }
            }
        }, 1000 * 20);
    }

    private void tearDown() {
        AlarmReceiver.completeWakefulIntent(mIntent);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
