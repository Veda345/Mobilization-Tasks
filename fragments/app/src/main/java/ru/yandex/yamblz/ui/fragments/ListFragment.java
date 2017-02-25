package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.adapters.FirstRecyclerAdapter;
import ru.yandex.yamblz.ui.adapters.PerformerSelectedListener;
import ru.yandex.yamblz.ui.adapters.RecyclerAdapter;

public class ListFragment extends Fragment {

    @BindView(R.id.list_d)
    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w("ListFragment", "OnCreateView");
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        if (getActivity() instanceof ListProvider && getActivity() instanceof PerformerSelectedListener) {
            ListProvider mProvider = (ListProvider) getActivity();
            RecyclerAdapter adapter = new FirstRecyclerAdapter(mProvider.getList(),
                    (PerformerSelectedListener) getActivity());
            rv.setAdapter(adapter);
        } else {
            throw new ClassCastException(getActivity().toString()
                    + " must implement ListProvider && PerformerSelectedListener");
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        return rootView;
    }

}