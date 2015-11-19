package com.mymarketapps.eye20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.mymarketapps.eye20.model.Store;

/**
 * Created by gorillalogic on 11/7/15.
 */
public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private Store myStore;
    private Switch switch_sound, switch_vibration;
    private SeekBar volume_control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        myStore = new Store(this);

        switch_sound = (Switch) findViewById(R.id.switch_sound);
        switch_sound.setChecked(myStore.isSoundOn());
        switch_sound.setOnCheckedChangeListener(this);
        switch_vibration = (Switch) findViewById(R.id.switch_vibration);
        switch_vibration.setChecked(myStore.isVibrationOn());
        switch_vibration.setOnCheckedChangeListener(this);
        volume_control = (SeekBar) findViewById(R.id.volume);
        volume_control.setEnabled(myStore.isSoundOn());
        volume_control.setOnSeekBarChangeListener(this);
        volume_control.setProgress(myStore.getVolume());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_sound:
                myStore.setSoundOn(b);
                volume_control.setEnabled(b);
                if (!b) // if sound off, make sure vibration is always on.
                    switch_vibration.setChecked(!b);
                break;
            case R.id.switch_vibration:
                myStore.setVibrationOn(b);
                if (!b) // if vibration off, make sure sound is always on.
                    switch_sound.setChecked(!b);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        myStore.setVolume(seekBar.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
