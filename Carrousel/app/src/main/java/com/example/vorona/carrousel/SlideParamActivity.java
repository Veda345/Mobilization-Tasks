package com.example.vorona.carrousel;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

/**
 * Activity showing preferences for slide show. Such as repeats and speed.
 */
public class SlideParamActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private Switch mySwitch;
    private SeekBar mSeekBar;
    private int  minV = 1; //*10^3 millis
    private int maxV = 15;
    private int curV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_param);
        mySwitch = (Switch) findViewById(R.id.switchRepeat);

        SharedPreferences prefs = getSharedPreferences("SlideParams", MODE_PRIVATE);
        Boolean repeat = prefs.getBoolean("Repeat", false);
        mySwitch.setChecked(repeat);
        curV = prefs.getInt("Speed", 3);

        mSeekBar = (SeekBar) findViewById(R.id.sbSpeed);
        mSeekBar.setMax(maxV - minV);
        mSeekBar.setProgress(curV - minV);
        mSeekBar.setOnSeekBarChangeListener(this);


        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences prefs = getSharedPreferences("SlideParams", MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                if (mySwitch.isChecked()) {
                    edit.putBoolean("Repeat", true);
                } else {
                    edit.putBoolean("Repeat", false);
                }
                edit.commit();
            }
        });

        SharedPreferences.Editor edit = prefs.edit();
        if (mySwitch.isChecked()) {
            edit.putBoolean("Repeat", true);
        } else {
            edit.putBoolean("Repeat", false);
        }
        edit.commit();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        curV = progress + minV;

        SharedPreferences prefs = getSharedPreferences("SlideParams", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("Speed", curV);
        edit.commit();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onFinishClick(View view) {
        onBackPressed();
    }
}
