package com.example.vorona.carrousel.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.example.vorona.carrousel.HierAsyncTask;
import com.example.vorona.carrousel.LoadListener;
import com.example.vorona.carrousel.file.File;
import com.example.vorona.carrousel.R;
import com.example.vorona.carrousel.file.Image;
import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.TransportClient;
import com.yandex.disk.client.exceptions.CancelledDownloadException;
import com.yandex.disk.client.exceptions.DownloadNoSpaceAvailableException;
import com.yandex.disk.client.exceptions.FileModifiedException;
import com.yandex.disk.client.exceptions.FileNotModifiedException;
import com.yandex.disk.client.exceptions.PreconditionFailedException;
import com.yandex.disk.client.exceptions.RangeNotSatisfiableException;
import com.yandex.disk.client.exceptions.RemoteFileNotFoundException;
import com.yandex.disk.client.exceptions.ServerWebdavException;
import com.yandex.disk.client.exceptions.UnknownServerWebdavException;
import com.yandex.disk.client.exceptions.WebdavNotAuthorizedException;
import com.yandex.disk.client.exceptions.WebdavUserNotInitialized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Adapter for given list of performers
 */
public class FirstRecyclerAdapter extends RecyclerAdapter implements Animation.AnimationListener{

    protected PerformerSelectedListener performerSelectedListener;

    /**
     * Will show list of performers in RecycleView
     */
    protected List<File> files;

    /**
     * Animation on click on performer's card
     */
    private Animation open;

    /**
     * Creates an instance of FirstRecyclerAdapter on specified list
     * @param s list of perfomers to show in RecycleView
     */
    public FirstRecyclerAdapter(List<File> s) {
        files = s;
    }

    /**
     * Sets specified listener for RecyclerView
     * @param listener given listener
     */
    public void setPerformerSelectedListener (PerformerSelectedListener listener) {
        performerSelectedListener = listener;
    }

    /**
     * Fills single card with performer's information(name, genres, cover)
     * @param holder holder for card(view)
     * @param position position of performer in given list
     */
    @Override
    public void onBindViewHolder(final GroupsViewHolder holder, int position) {
        open = AnimationUtils.loadAnimation(holder.img.getContext(),
                R.anim.openning);
        open.setAnimationListener(this);
        final Context context = holder.img.getContext();
        final File file = files.get(position);
        if (file instanceof Image) {

            String size = "";
            long sz = file.getSize();
            if (sz / 1024 <= 1024) {
                size = sz/1024 + " Kb";
            }
            else {
                if (sz/ (1024*1024) <= 1024) {
                    size = sz/(1024*1024) + " Mb";
                } else  {
                    size = sz/(1024*1024*1024) + " Gb";
                }
            }

            Date date = new Date(file.getDate());
            SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yy hh:mm");
            String dateText = df2.format(date);

            holder.title_file.setText(file.getName());
            holder.info_file.setText(new StringBuilder().append(size).append(" \u2022 ").append(dateText).toString());

            holder.title_file.setVisibility(View.VISIBLE);
            holder.info_file.setVisibility(View.VISIBLE);
            holder.title_folder.setVisibility(View.INVISIBLE);

            holder.img.setImageResource(R.drawable.ic_image);
            class DownloadPreview extends AsyncTask<ListItem, Void, Void> {
                byte[] ar;
                @Override
                protected Void doInBackground(ListItem... params) {
                    LoadListener load = new LoadListener();
                    Image img = new Image(params[0]);
                    try {
//                        URLEncoder.encode(img.getPath(), "UTF-8")
                        HierAsyncTask.tc.downloadPreview(img.getPath().replaceAll(" ", "%20"), load);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    img.setOut(load.out);
                    ar = ((ByteArrayOutputStream)(load.out)).toByteArray();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Glide.with(context)
                            .load(ar)
                            .asBitmap()
                            .into(holder.img);
                }
            }
            DownloadPreview load = new DownloadPreview();
            load.execute(file.getIt());
        }
        else {
            holder.img.setImageResource(R.drawable.folder);

            holder.title_folder.setText(file.getName());

            holder.title_file.setVisibility(View.INVISIBLE);
            holder.info_file.setVisibility(View.INVISIBLE);
            holder.title_folder.setVisibility(View.VISIBLE);
        }
        holder.itemView.setTag(R.id.tag, files.get(position));
    }

    /**
     * Returns number of performers in list
     * @return number of performers in list
     */
    @Override
    public int getItemCount() {
        return files.size();
    }

    /**
     * Calls listener's method handling clicks on views
     * @param v view on which clicked
     */
    @Override
    public void onClick(View v) {
        File singer = (File) v.getTag(R.id.tag);
        v.startAnimation(open);
        if (performerSelectedListener != null) {
            performerSelectedListener.onPerformerSelected(singer);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}
