package com.example.vorona.carrousel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.vorona.carrousel.file.File;
import com.example.vorona.carrousel.file.Folder;
import com.example.vorona.carrousel.file.Image;
import com.example.vorona.carrousel.list.FirstRecyclerAdapter;
import com.example.vorona.carrousel.list.RecyclerAdapter;
import com.example.vorona.carrousel.list.RecylcerDividersDecorator;
import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.TransportClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Async Task for loading content of selected folder.
 */
public class HierAsyncTask extends AsyncTask<String, Integer, Integer> {
    /**
     * Attached fragment.
     */
    private HierFragment fragment;

    /**
     * Current state of task
     */
    private DownloadState state;

    private String LOG_TAG = "HierAsyncTask";

    List<File> files;
    ArrayList<Image> images;

    //    TransportClient
    public static TransportClient tc;

    public HierAsyncTask(HierFragment fragment) {
        this.fragment = fragment;
        files = new ArrayList<>();
        images = new ArrayList<>();
        state = DownloadState.DOWNLOADING;
        fragment.updateView(this);
    }

    public void attachActivity(HierFragment fragment) {
        this.fragment = fragment;
        fragment.updateView(this);
    }

    /**
     * @return current state of task
     */
    DownloadState getState() {
        return state;
    }

    /**
     * Get list of files in selected folder and sublist consisting of images.
     */
    @Override
    protected Integer doInBackground(String... params) {
        Log.w(LOG_TAG, "Started Async Task");
        try {
            SharedPreferences pref = fragment.getActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE);
            String token = pref.getString("Code", "-1");
            Credentials cred = new Credentials("f946d1f8d9264ee1b3dd559e6949194d", token);
            tc = TransportClient.getInstance(fragment.getActivity(), cred);
            ListHandler listHandler = new ListHandler();
            tc.getList(params[1] + "/", listHandler);
            List<ListItem> items = new ArrayList<>();
            for (ListItem it : listHandler.listItems) {
                if (it.isCollection() || it.getContentType().contains("image")) {
                    items.add(it);
                }
            }
            items.remove(0);
            if (items.size() > 0) {
                state = DownloadState.DONE;
            } else {
                state = DownloadState.EMPTY;
            }

            for (int i = 0; i < items.size(); i++) {
                ListItem it = items.get(i);
                if (it.isCollection()) {
                    files.add(new Folder(it));
                } else {
                    Image img = new Image(it);
                    files.add(img);
                    images.add(img);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(LOG_TAG, "We got exception during download");
            state = DownloadState.ERROR;
            return null;
        }
    }

    /**
     * Show uploaded list in recyclerView
     *
     * @param vi
     */
    @Override
    protected void onPostExecute(Integer vi) {
        fragment.updateView(this);

        Log.w(LOG_TAG, "Finished Async Task");
        if (state == DownloadState.DONE) {
            RecyclerView rv = (RecyclerView) fragment.getView().findViewById(R.id.list_perf);
            FirstRecyclerAdapter mAdapter = new FirstRecyclerAdapter(files);
            setListener(rv, mAdapter);
        }
    }

    private void setListener(RecyclerView rv, RecyclerAdapter adapter) {
        rv.setHasFixedSize(true);
        int cnt = 1;
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(cnt, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(mLayoutManager);
        rv.addItemDecoration(new RecylcerDividersDecorator(Color.GRAY));
        adapter.setPerformerSelectedListener(fragment);
        rv.setAdapter(adapter);
    }

}

