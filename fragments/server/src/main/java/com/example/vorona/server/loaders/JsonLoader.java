package com.example.vorona.server.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.example.vorona.server.model.Singer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by vorona on 19.07.16.
 */

public class JsonLoader extends AsyncTaskLoader<List<Singer>> {
    List<Singer> singers;

    public JsonLoader(Context context) {
        super(context);

    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override
    public List<Singer> loadInBackground() {
        try {
            Log.w("Loader", "Loading from the net (server)");
            singers = getSinger();
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Loader", "We got exception during download");
            return null;
        }
        return singers;
    }


    public List<Singer> getSinger() throws IOException {
        URL url = new URL("http://cache-spb05.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json");
        List<Singer> res;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = connection.getInputStream();
            try {
                JsonReader reader = new JsonReader(new InputStreamReader(in));
                res = readJson(reader);
                reader.close();
                return res;
            } finally {
                if (in != null) {
                    in.close();
                }
                connection.disconnect();
            }
        } catch (Exception ignored) {
            JsonReader reader = new JsonReader(new InputStreamReader(getContext().getAssets().open("artists.json")));
            res = readJson(reader);
            reader.close();
            return res;
        }
    }

    /**
     * Combine all performers
     */
    private List<Singer> readJson(JsonReader reader) throws IOException {
        ArrayList<Singer> ar = new ArrayList<>();
        Singer cur;
        reader.beginArray();
        while (reader.hasNext()) {
            cur = takeData(reader);
            ar.add(cur);
        }
        reader.endArray();

        return ar;
    }

    /**
     * Read information about individual performers
     */
    private Singer takeData(JsonReader reader) throws IOException {
        Singer res = new Singer();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    res.setId(reader.nextLong());
                    break;
                case "name":
                    res.setName(reader.nextString());
                    break;
                case "tracks":
                    res.setTracks((int) reader.nextLong());
                    break;
                case "albums":
                    res.setAlbums((int) reader.nextLong());
                    break;
                case "link":
                    res.setLink(reader.nextString());
                    break;
                case "description":
                    res.setBio(reader.nextString());
                    break;
                case "cover":
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String s = reader.nextName();
                        switch (s) {
                            case "small":
                                res.setCover_small(reader.nextString());
                                break;
                            case "big":
                                res.setCover_big(reader.nextString());
                                break;
                            default:
                                reader.skipValue();
                        }
                    }
                    reader.endObject();
                    break;
                case "genres":
                    reader.beginArray();
                    StringBuilder str = new StringBuilder();
                    List<String> gns = new ArrayList<>();
                    while (reader.hasNext()) {
                        String s = reader.nextString();
                        gns.add(s);
                    }
                    reader.endArray();
                    res.setGenres(gns);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return res;
    }


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(List<Singer> sin) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (sin != null) {
                onReleaseResources(sin);
            }
        }
        singers = sin;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(sin);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (singers != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(singers);
        }

        if (takeContentChanged() || singers == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(List<Singer> singers) {
        super.onCanceled(singers);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(singers);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (singers != null) {
            onReleaseResources(singers);
            singers = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     *
     * @param singers
     */
    protected void onReleaseResources(List<Singer> singers) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }
}
