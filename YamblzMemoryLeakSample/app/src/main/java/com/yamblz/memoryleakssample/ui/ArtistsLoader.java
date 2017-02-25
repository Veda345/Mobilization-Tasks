package com.yamblz.memoryleakssample.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.yamblz.memoryleakssample.communication.Api;
import com.yamblz.memoryleakssample.model.Artist;

/**
 * Created by i-sergeev on 01.07.16
 */
public class ArtistsLoader extends AsyncTaskLoader<Artist[]>
{
    private final Api api;

    public ArtistsLoader(Context context)
    {
        super(context);
        api = new Api(context);
    }

    @Override
    public Artist[] loadInBackground()
    {
        return api.getArtists();
    }
}
