package com.example.vorona.carrousel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vorona.carrousel.file.Image;
import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.TransportClient;

import java.io.ByteArrayOutputStream;

/**
 * Async Task for uploading single images.
 */
public class SingleImageAsyncTask extends AsyncTask<Image, Integer, Integer> {

    private SingleImageActivity.PlaceholderFragment fragment;

    /**
     * Current state of task
     */
    private DownloadState state;

    private String LOG_TAG = "SingleImageAsyncTask";

    byte[] data;

    Image curImg;

    public static TransportClient tc;

    public SingleImageAsyncTask(SingleImageActivity.PlaceholderFragment fragment) {
        this.fragment = fragment;
        state = DownloadState.DOWNLOADING;
        if (fragment.getView() != null) {
            (fragment.getView().findViewById(R.id.progressSingleImage)).setVisibility(View.VISIBLE);
        }
    }

    /**
     * @return current state of task
     */
    DownloadState getState() {
        return state;
    }


    /**
     * Get byte array representing specified image. Stored in data.
     */
    @Override
    protected Integer doInBackground(Image... params) {
        Log.w(LOG_TAG, "Started Async Task");
        if (fragment != null) {
            SharedPreferences pref = fragment.getActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE); //
            String token = pref.getString("Code", "-1");
            Credentials cred = new Credentials("f946d1f8d9264ee1b3dd559e6949194d", token);
            TransportClient tc;
            curImg = params[0];
            try {
                tc = TransportClient.getInstance(fragment.getActivity(), cred);
                LoadListener loadListener = new LoadListener();
                tc.download(curImg.getPath(), loadListener);
                data = ((ByteArrayOutputStream) (loadListener.out)).toByteArray();

                if (data.length > 0) {
                    state = DownloadState.DONE;
                } else {
                    state = DownloadState.EMPTY;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                state = DownloadState.ERROR;
                return null;
            }
        }
        return null;
    }

    /**
     * Load image from byte array into ImageView.
     */
    @Override
    protected void onPostExecute(Integer vi) {
        Log.w(LOG_TAG, "Finished Async Task");
        if (state == DownloadState.DONE) {
            if (fragment.getView() != null) {
                (fragment.getView().findViewById(R.id.progressSingleImage)).setVisibility(View.INVISIBLE);
                Glide.with(fragment.getActivity())
                        .load(data)
                        .asBitmap()
                        .into((ImageView) fragment.getView().findViewById(R.id.cur_img));
            }
        }
    }



}

