package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.views.BellView;

public class ContentFragment extends BaseFragment {
    private Random random;

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.custom_bell)
    BellView bell1;
    @BindView(R.id.custom_bell2)
    BellView bell2;
    @BindView(R.id.custom_bell3)
    BellView bell3;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        random = new Random();
        return view;
    }

    @OnClick({R.id.custom_bell, R.id.custom_bell2, R.id.custom_bell3})
    void onClick(View view) {
        if (!((BellView) view).notificationShown())
            ((BellView) view).animateEvents(random.nextInt(20) + 1);
        else
            ((BellView) view).deleteEvents();
    }

    @OnClick(R.id.btn)
    void onClick() {
        onClick(bell1);
        onClick(bell2);
        onClick(bell3);
    }
}
