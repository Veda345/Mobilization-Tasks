package com.yamblz.memoryleakssample.ui;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by i-sergeev on 01.07.16
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistVH> {
    @NonNull
    private final Artist[] artists;

    @NonNull
    private final Picasso picasso;

    @NonNull
    private final Resources resources;

    @NonNull
    private final ArtistsAdapterListener listener;

    public ArtistsAdapter(@Nullable Artist[] artists,
                          @NonNull Picasso picasso,
                          @NonNull Resources resources,
                          ArtistsAdapterListener listener) {
        this.picasso = picasso;
        this.resources = resources;
        if (artists == null) {
            artists = new Artist[0];
        }
        this.artists = artists;

        if (listener == null) {
            listener = ArtistsAdapterListener.NULL;
        }
        this.listener = listener;
    }

    @NonNull
    public Artist getArtist(int position) {
        return artists[position];
    }

    @Override
    public ArtistVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.artist_card, parent, false);
        return new ArtistVH(view);
    }

    @Override
    public void onBindViewHolder(ArtistVH holder, int position) {
        holder.bind(artists[position]);
    }

    @Override
    public int getItemCount() {
        return artists.length;
    }

    public class ArtistVH extends RecyclerView.ViewHolder {
        @BindView(R.id.artist_poster)
        ImageView posterImageView;

        @BindView(R.id.artist_name)
        TextView nameTextView;

        @BindView(R.id.artist_albums)
        TextView albumsTextView;

        @BindView(R.id.artist_tracks)
        TextView songsTextView;

        private Artist artist;

        public ArtistVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickArtist(artist);
                }
            });
        }

        public void bind(@NonNull Artist artist) {
            this.artist = artist;
            picasso.load(artist.getCover().getSmallImageUrl()).into(posterImageView);
            nameTextView.setText(artist.getName());
            albumsTextView.setText(resources.getQuantityString(R.plurals.artistAlbums,
                    artist.getAlbumsCount(),
                    artist.getAlbumsCount()));
            songsTextView.setText(resources.getQuantityString(R.plurals.artistTracks,
                    artist.getTracksCount(),
                    artist.getTracksCount()));
        }
    }

    public interface ArtistsAdapterListener {
        void onClickArtist(@NonNull Artist artist);

        ArtistsAdapterListener NULL = new ArtistsAdapterListener() {
            @Override
            public void onClickArtist(@NonNull Artist artist) {

            }
        };
    }
}
