package com.yamblz.uioptimizationsample.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yamblz.uioptimizationsample.R;
import com.yamblz.uioptimizationsample.model.Artist;

import java.io.IOException;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static rx.schedulers.Schedulers.io;

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

    public ArtistsAdapter(@Nullable Artist[] artists,
                          @NonNull Picasso picasso,
                          @NonNull Resources resources) {
        this.picasso = picasso;
        this.resources = resources;
        if (artists == null) {
            artists = new Artist[0];
        }
        this.artists = artists;
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

        @BindView(R.id.artist_songs)
        TextView songsTextView;

        @BindView(R.id.artist_description)
        TextView descriptionTextView;

        public ArtistVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(@NonNull final Artist artist) {
            Observable<Bitmap> obs = Observable.
                    fromCallable(() -> picasso.load(artist.getCover().getBigImageUrl()).get())
                    .subscribeOn(io())
                    .observeOn(AndroidSchedulers.mainThread());

            //Don't look here... At least we don't have overdraw. =(
            obs.subscribe(bitmap -> {
                if (bitmap == null) return;
                Shader shader = new LinearGradient(0, bitmap.getHeight()/2, 0,
                        bitmap.getHeight(),
                        Color.TRANSPARENT,
                        Color.BLACK,
                        Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setShader(shader);
                Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(bitmap1);
                canvas.drawPaint(paint);
                posterImageView.setImageBitmap(bitmap1);
            });


            nameTextView.setText(artist.getName());
            descriptionTextView.setText(artist.getDescription());
            albumsTextView.setText(resources.getQuantityString(R.plurals.artistAlbums,
                    artist.getAlbumsCount(),
                    artist.getAlbumsCount()));
            songsTextView.setText(resources.getQuantityString(R.plurals.artistTracks,
                    artist.getTracksCount(),
                    artist.getTracksCount()));
        }

        private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {

            Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(firstImage, 0f, 0f, null);
            canvas.drawBitmap(secondImage, 0, 0, null);
            return result;
        }
    }
}
