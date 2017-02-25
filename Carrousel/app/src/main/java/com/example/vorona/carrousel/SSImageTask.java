package com.example.vorona.carrousel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vorona.carrousel.file.Image;
import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.TransportClient;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Semaphore;

/**
 * Async Task for downloading images and showing them in ImageView.
 */
public class SSImageTask extends AsyncTask<Image, Integer, Integer> {

    private Activity activity;

    /**
     * Current state of task
     */
    private DownloadState state;

    private String LOG_TAG = "SSImageTask";

    private byte[] data;

    private static int delay;

    private Semaphore semaphore;

    final Animation anim_out;
    final Animation anim_in;
    final ImageView img;

    public SSImageTask(Activity activity, Semaphore semaphore) {
        this.activity = activity;
        this.semaphore = semaphore;
        anim_out = AnimationUtils.loadAnimation(activity, android.R.anim.fade_out);
        anim_in = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
        img = (ImageView) activity.findViewById(R.id.fullscreen_content);

        SharedPreferences prefs = activity.getSharedPreferences("SlideParams", Context.MODE_PRIVATE);
        delay = prefs.getInt("Speed", 3) * 1000;
    }

    /**
     * @return current state of task
     */
    DownloadState getState() {
        return state;
    }

    /**
     * Download image, store byte array in data.
     */
    @Override
    protected Integer doInBackground(Image... params) {
        Log.w(LOG_TAG, "Started Async Task");
        state = DownloadState.DOWNLOADING;
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences pref = activity.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String token = pref.getString("Code", "-1");
        Credentials cred = new Credentials("f946d1f8d9264ee1b3dd559e6949194d", token);
        TransportClient tc;
        try {
            tc = TransportClient.getInstance(activity, cred);
            LoadListener loadListener = new LoadListener();
            tc.download(params[0].getPath(), loadListener);
            data = ((ByteArrayOutputStream) (loadListener.out)).toByteArray();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            state = DownloadState.ERROR;
            semaphore.release();
            return null;
        }
    }

    /**
     * Put image in ImageView, check if it was last image in slide show to restart or stop it.
     */
    @Override
    protected void onPostExecute(Integer vi) {
        if (data.length > 0) {
            state = DownloadState.DONE;
        } else {
            state = DownloadState.EMPTY;
        }

        if (state == DownloadState.DONE && activity != null && !activity.isDestroyed()) {
            activity.findViewById(R.id.progressBarShow).setVisibility(View.INVISIBLE);
            Glide.with(activity)
                    .load(data)
                    .asBitmap()
                    .into(img);
            anim_in.start();
            semaphore.release();
            anim_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    anim_in.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            img.startAnimation(anim_in);
                        }
                    });

                }
            });


        }
        if (activity instanceof SlideShowActivity && activity != null && !activity.isDestroyed()) {
            ((SlideShowActivity) activity).finishedTask();
            if (((SlideShowActivity) activity).getFinishedTask() == ((SlideShowActivity) activity).getMaxTask()) {
                activity.findViewById(R.id.imgPlay).setVisibility(View.VISIBLE);
                SharedPreferences prefs = activity.getSharedPreferences("SlideParams", Context.MODE_PRIVATE);
                Boolean repeat = prefs.getBoolean("Repeat", false);
                if (repeat) {
                    ((SlideShowActivity)activity).showPhotos();

                }
            }
        }
        Log.w(LOG_TAG, "Finished Async Task");
    }

}

