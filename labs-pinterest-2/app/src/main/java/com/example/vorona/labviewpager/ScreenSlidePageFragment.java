/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vorona.labviewpager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    public static final String ARG_PAGE = "page";

    @BindView(R.id.txtFullName)
    TextView fullName;
    @BindView(R.id.txtSkateName)
    TextView name;
    @BindView(R.id.txtFullInfo)
    TextView desc;
    @BindView(R.id.imgSkate)
    ImageView skate;
    @BindView(R.id.imgComp)
    ImageView comp;
    @BindView(R.id.second_part)
    View secondPart;

    private int mPageNumber;
    private int h;
    private boolean secondVisible = false;
    private float transY = 0, initial = 0, initText = 0;

    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        ButterKnife.bind(this, rootView);

        skate.setOnClickListener(this);

        name.setText(App.names[mPageNumber]);
        fullName.setText(App.full_names[mPageNumber]);
        desc.setText(App.descriptions[mPageNumber]);
        skate.setImageResource(App.img_skate[mPageNumber]);
        comp.setImageResource(App.img_comp[mPageNumber]);

        secondPart.setVisibility(View.INVISIBLE);
        secondPart.setOnTouchListener(this);

        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }


    @Override
    public void onClick(View view) {
        Log.w("OnClick", "Hey");
        secondPart.setVisibility(View.VISIBLE);
        h = secondPart.getHeight();

        secondVisible = !secondVisible;

        if (!secondVisible) h = 0;

        if (secondVisible)
            openDetails(h);
        else
            closeDetails(h);
    }

    private void openDetails(int h) {
        Log.w("", "Open Detail");
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(secondPart, "translationY", getView().getHeight(),
                getView().getHeight() - h - 64).setDuration(300);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(skate, "translationY", -h).setDuration(300);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(name, "translationY", -h).setDuration(300);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim1, anim2, anim3);
        set.start();
        ((ScreenSlideActivity) getActivity()).setSwipable(false);
    }

    private void closeDetails(int h) {
        Log.w("", "Close Detail");
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(secondPart, "translationY", getView().getHeight()).setDuration(300);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(skate, "translationY", 0).setDuration(300);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(name, "translationY", 0).setDuration(300);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim1, anim2, anim3);
        set.start();
        ((ScreenSlideActivity) getActivity()).setSwipable(true);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            transY = motionEvent.getRawY();
            initial = view.getY();
            initText = name.getY();
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && secondVisible) {
            if (motionEvent.getRawY() - transY > 0) {
                float dY = motionEvent.getRawY() - transY;
                view.setTranslationY(initial + dY);
                skate.setTranslationY(dY - h);
                name.setTranslationY(initText + dY);
            }

            if (motionEvent.getRawY() - transY > h / 4) {
                closeDetails(h);
                secondVisible = !secondVisible;
            }
            return true;
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (secondVisible) {
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(secondPart, "translationY",
                        getView().getHeight() - h - 64).setDuration(300);
                ObjectAnimator anim2 = ObjectAnimator.ofFloat(skate, "translationY", -h).setDuration(300);
                ObjectAnimator anim3 = ObjectAnimator.ofFloat(name, "translationY", -h).setDuration(300);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(anim1, anim2, anim3);
                set.start();
            }
        }
        return true;
    }
}
