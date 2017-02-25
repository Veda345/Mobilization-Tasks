package ru.yandex.yamblz.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.model.Singer;

/**
 * Created by vorona on 03.08.16.
 */

public class FullInfoDialogFragment extends DialogFragment {
    @BindView(R.id.title)
    TextView txt;
    @BindView(R.id.bio)
    TextView bio;
    @BindView(R.id.tracks)
    TextView tracks;
    @BindView(R.id.cover_big)
    ImageView cover;
    @BindView(R.id.background)
    ImageView back;

    private Singer singer;

    public static FullInfoDialogFragment newInstance(Singer singer) {
        Bundle args = new Bundle();
        args.putParcelable("Singer", singer);
        FullInfoDialogFragment fragment = new FullInfoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singer = getArguments().getParcelable("Singer");
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, null);
        ButterKnife.bind(this, view);
        if (getActivity().findViewById(R.id.tabs) != null)
            getActivity().findViewById(R.id.tabs).setVisibility(View.GONE);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_info, null);
        builder.setView(view)
                .setPositiveButton("Ok", (dialog, id) -> {
                });
        ButterKnife.bind(this, view);
        txt.setText(singer.getName());
        bio.setMovementMethod(new ScrollingMovementMethod());
        bio.setText(singer.getName() + " - " + singer.getBio());
        tracks.setText("Альбомов " + singer.getAlbums() + ", треков " + singer.getTracks());
        Context context = cover.getContext();
        Picasso.with(context).load(singer.getCoverBig()).into(cover);
        Picasso.with(context).load(singer.getCoverBig()).into(back);
        return builder.create();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        txt.setText(singer.getName());
        bio.setMovementMethod(new ScrollingMovementMethod());
        bio.setText(singer.getName() + " - " + singer.getBio());
        tracks.setText("Альбомов " + singer.getAlbums() + ", треков " + singer.getTracks());
        Context context = cover.getContext();
        Picasso.with(context).load(singer.getCoverBig()).into(cover);
        Picasso.with(context).load(singer.getCoverBig()).into(back);
    }
}
