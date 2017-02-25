package com.yamblz.memoryleakssample.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yamblz.memoryleakssample.R;
import com.yamblz.memoryleakssample.model.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistDetailsFragment extends Fragment {
    @BindView(R.id.artist_poster)
    ImageView posterImageView;

    @BindView(R.id.artist_name)
    TextView nameTextView;

    @BindView(R.id.artist_albums)
    TextView albumsTextView;

    @BindView(R.id.artist_tracks)
    TextView tracksTextView;

    @BindView(R.id.artist_description)
    TextView descriptionTextView;

    private static final String ARTIST = "artist";
    private Artist artist;

    public static ArtistDetailsFragment newInstance(Artist artist) {
        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARTIST, artist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_artist_details, container, false);
        ButterKnife.bind(this, rootView);
        Bundle args = getArguments();
        artist = args.getParcelable(ARTIST);
        clearViews();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (artist != null) {
            updateArtistView(artist);
        } else {
            clearViews();
        }
    }

    private void clearViews() {
        posterImageView.setImageResource(android.R.color.white);
        nameTextView.setText("");
        albumsTextView.setText("");
        tracksTextView.setText("");
        descriptionTextView.setText("");
    }

    private void updateArtistView(@NonNull Artist artist) {
        Picasso.with(getActivity()).load(artist.getCover().getBigImageUrl()).into(posterImageView);
        nameTextView.setText(artist.getName());
        albumsTextView.setText(getResources().getQuantityString(R.plurals.artistAlbums,
                artist.getAlbumsCount(),
                artist.getAlbumsCount()));
        tracksTextView.setText(getResources().getQuantityString(R.plurals.artistTracks,
                artist.getTracksCount(),
                artist.getTracksCount()));
        descriptionTextView.setText(artist.getDescription());
    }
}
