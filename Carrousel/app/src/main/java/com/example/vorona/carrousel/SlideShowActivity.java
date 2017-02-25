package com.example.vorona.carrousel;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.vorona.carrousel.file.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Fullscreen activity with slide show.
 */
public class SlideShowActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;

    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsViewTop, mControlsViewBottom;
    private boolean mVisible;

    private List<Image> images;

    /**
     * Started async tasks.
     */
    private List<SSImageTask> tasks;

    /***
     * Semaphore for controlling simultaneous uploading images.
     */
    Semaphore semaphore = new Semaphore(5);
    boolean running = false;

    private int finishedTask = 0, maxTask;

    ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent im = getIntent();
        images = im.getParcelableArrayListExtra("IMAGES");
        tasks = new ArrayList<>();

        running = true;
        maxTask = images.size();

        setContentView(R.layout.activity_slide_show);
        btn = (ImageView) findViewById(R.id.imgPlay);

        mVisible = true;
        mControlsViewTop = findViewById(R.id.fullscreen_content_controls_top);
        mControlsViewBottom = findViewById(R.id.fullscreen_content_controls_bottom);
        mContentView = findViewById(R.id.fullscreen_content);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
        showPhotos();

    }

    void showPhotos() {
        btn.setVisibility(View.INVISIBLE);
        finishedTask = 0;

        for (int i = 0; i < images.size(); i++) {
            if (running) {
                SSImageTask task = new SSImageTask(SlideShowActivity.this, semaphore);
                tasks.add(task);
                task.execute(images.get(i));
            }
        }
    }

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsViewTop.setVisibility(View.GONE);
        mControlsViewBottom.setVisibility(View.GONE);
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @Override
        public void run() {

            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsViewTop.setVisibility(View.VISIBLE);
            mControlsViewBottom.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void onClick(View view) {
        finishedTask = 0;
        showPhotos();
    }

    /**
     * Exit actvity after canceling all started tasks.
     */
    public void onDoneClick(View view) {
        running = false;
        for (SSImageTask task:tasks) {
                task.cancel(true);
        }
        finish();
    }

    /**
     * Open activity with slide show preferences.
     */
    public void onParamClick(View view) {
        Intent gr = new Intent(this, SlideParamActivity.class);
        startActivity(gr);
    }

    /**
     * Inc count of finished tasks.
     */
    public void finishedTask() {
        finishedTask++;
    }

    public int getFinishedTask() {
        return finishedTask;
    }

    /**
     * @return count of images in slide show.
     */
    public int getMaxTask() {
        return maxTask;
    }
}
