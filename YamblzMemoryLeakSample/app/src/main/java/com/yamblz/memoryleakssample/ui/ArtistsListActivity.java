package com.yamblz.memoryleakssample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Artist[]>,
        ArtistsAdapter.ArtistsAdapterListener {
    private static final String POSITION = "position";
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artists_recycler_view)
    RecyclerView recyclerView;

    private static final int ARTISTS_LOADER_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artisits_list);
        getWindow().setBackgroundDrawableResource(R.drawable.window_background);

        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        showProgress();
        getSupportLoaderManager().initLoader(
                ARTISTS_LOADER_ID,
                null,
                this).forceLoad();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int pos = savedInstanceState.getInt(POSITION);
        (recyclerView.getLayoutManager()).scrollToPosition(pos);
        ((GridLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(pos, 0);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showContent(Artist[] data) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        ArtistsAdapter artistsAdapter = new ArtistsAdapter(data,
                Picasso.with(this),
                getResources(), this
        );
        recyclerView.setAdapter(artistsAdapter);
        artistsAdapter.notifyDataSetChanged();
    }

    private void showArtistDetails(@NonNull Artist artist) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail, ArtistDetailsFragment.newInstance(artist), "info")
                .addToBackStack("detail")
                .commit();
    }

    @Override
    public Loader<Artist[]> onCreateLoader(int id, Bundle args) {
        return new ArtistsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Artist[]> loader, Artist[] data) {
        showContent(data);
    }

    @Override
    public void onLoaderReset(Loader<Artist[]> loader) {

    }

    @Override
    public void onClickArtist(@NonNull Artist artist) {
        showArtistDetails(artist);
    }
}
