package com.mymarketapps.eye20.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gorillalogic on 10/30/15.
 */
public class Store {
    private static final String KEY_IS_TIMER_STARTED = "is_timer_started";
    private static final String KEY_SETTING_VIBRATION = "vibration";
    private static final String KEY_SETTING_SOUND = "sound";
    private static final String KEY_SETTING_VOLUME = "volume";

    private SharedPreferences mPrefs;
    private Context mContext;

    public Store(Context c) {
        mContext = c;
    }

    SharedPreferences getStore() {
        if (mPrefs == null)
            mPrefs = mContext.getSharedPreferences("LOCAL_STORE_EYE20", Context.MODE_PRIVATE);
        return mPrefs;
    }

    SharedPreferences.Editor getStoreEditor() {
        return getStore().edit();
    }

    public void setTimerStarted(boolean start) {
        getStoreEditor()
                .putBoolean(KEY_IS_TIMER_STARTED, start)
                .commit();
    }

    public boolean isTimerStarted() {
        return getStore().getBoolean(KEY_IS_TIMER_STARTED, false);
    }

    public void setVibrationOn(boolean on) {
        getStoreEditor()
                .putBoolean(KEY_SETTING_VIBRATION, on)
                .commit();
    }

    public boolean isVibrationOn() {
        return getStore().getBoolean(KEY_SETTING_VIBRATION, true);
    }

    public void setSoundOn(boolean on) {
        getStoreEditor()
                .putBoolean(KEY_SETTING_SOUND, on)
                .commit();
    }

    public boolean isSoundOn() {
        return getStore().getBoolean(KEY_SETTING_SOUND, true);
    }

    public void setVolume(int value) {
        getStoreEditor()
                .putInt(KEY_SETTING_VOLUME, value)
                .commit();
    }

    public int getVolume() {
        return getStore().getInt(KEY_SETTING_VOLUME, 20);
    }

}
