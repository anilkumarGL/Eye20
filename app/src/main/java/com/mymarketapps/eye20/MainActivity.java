package com.mymarketapps.eye20;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.mymarketapps.eye20.model.Store;
import com.mymarketapps.eye20.utils.AnimationFactory;
import com.mymarketapps.eye20.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Store myStore;
    private PendingIntent mAlarmPendingIntent;
    private Button start, stop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        myStore = new Store(this);
        mAlarmPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.mymarketapps.eye20.action.AlarmReceiver"), 0);
        if (myStore.isTimerStarted()) {
            AnimationFactory.flipTransition((ViewAnimator) findViewById(R.id.tile_flipper), AnimationFactory.FlipDirection.RIGHT_LEFT, mAnimListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int[] tip_indexes = getTips();
        ((TextView) findViewById(R.id.health_tip)).setText(Utils.HEALTH_TIPS[tip_indexes[0]]);
        ((TextView) findViewById(R.id.health_tip_flipped)).setText(Utils.HEALTH_TIPS[tip_indexes[1]]);
        enableStart(myStore.isTimerStarted() ? false : true);
    }

    private int[] getTips() {
        int tipIndex = Utils.getRandomIndex();
        int flipIndex = 0;
        if (tipIndex == 0)
            flipIndex = 1;
        else if (tipIndex == Utils.HEALTH_TIPS.length-1)
            flipIndex = Utils.HEALTH_TIPS.length-2;
        else
            flipIndex = tipIndex + 1;
        return new int[] {tipIndex, flipIndex};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * If enable is true, enables start button and disables stop button and vice versa.
     * @param enable
     */
    private void enableStart(boolean enable) {
        start.setEnabled(enable);
        stop.setEnabled(!enable);
        start.setBackgroundColor(getResources().getColor(enable ? R.color.colorPrimaryDark : R.color.colorDisabled));
        stop.setBackgroundColor(getResources().getColor(!enable ? R.color.colorPrimaryDark : R.color.colorDisabled));
    }

    private Animation.AnimationListener mAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // Flip to next tip
            AnimationFactory.flipTransition((ViewAnimator) findViewById(R.id.tip_flipper), AnimationFactory.FlipDirection.RIGHT_LEFT, null);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                AnimationFactory.flipTransition((ViewAnimator) findViewById(R.id.tile_flipper), AnimationFactory.FlipDirection.RIGHT_LEFT, mAnimListener);
                start20Alarm();
                break;

            case R.id.stop:
                AnimationFactory.flipTransition((ViewAnimator) findViewById(R.id.tile_flipper), AnimationFactory.FlipDirection.LEFT_RIGHT, mAnimListener);
                cancel20Alarm();
                break;
        }
        enableStart(myStore.isTimerStarted() ? false : true);
    }

    private void start20Alarm() {
        AlarmManager almgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        almgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 20, mAlarmPendingIntent);
        myStore.setTimerStarted(true);
    }

    private void cancel20Alarm() {
        AlarmManager almgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        almgr.cancel(mAlarmPendingIntent);
        myStore.setTimerStarted(false);
    }
}
