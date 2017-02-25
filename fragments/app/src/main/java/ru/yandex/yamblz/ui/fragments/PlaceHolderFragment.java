package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.yandex.yamblz.R;
import ru.yandex.yamblz.model.Singer;

/**
 * Created by vorona on 03.08.16.
 */

public class PlaceHolderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    OnMoreClicked mCallback;
    ListProvider mProvider;

    public PlaceHolderFragment() {
    }

    public static PlaceHolderFragment newInstance(int sectionNumber) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
        Bundle args = getArguments();
        int pos = args.getInt(ARG_SECTION_NUMBER);

        mCallback = (OnMoreClicked) getActivity();
        mProvider = (ListProvider) getActivity();
        Singer singer = mProvider.getList().get(pos);
        ((TextView)rootView.findViewById(R.id.txtArtist)).setText(singer.getName());
        Picasso.with(getActivity()).load(singer.getCoverBig())
                .into(((ImageView) rootView.findViewById(R.id.imgCover)));
        (rootView.findViewById(R.id.btnMore)).setOnClickListener(v -> mCallback.onArticleSelected(pos));

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCallback = null;
        mProvider = null;
    }
}