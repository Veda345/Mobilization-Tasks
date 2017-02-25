package ru.yandex.yamblz.ui.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vorona on 06.08.16.
 */

public class CustomLayoutManager extends GridLayoutManager {
    private int currentFirst, currentLast;
    private Map<View, AnimatorSet> animations;

    public CustomLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CustomLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        init(context);
    }

    public CustomLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        init(context);
    }

    void init(Context context) {
        animations = new HashMap<>();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int p = super.scrollVerticallyBy(dy, recycler, state);
        if (currentLast == 0) currentLast = findLastVisibleItemPosition();
        int newFirst = findFirstVisibleItemPosition();
        int newLast = findLastVisibleItemPosition();

        if (newFirst < currentFirst || (newLast > currentLast && currentLast > 0)) {
            int start, finish;
            if (newFirst < currentFirst) {
                start = 0;
                finish = currentFirst - newFirst;
            } else {
                start = currentLast - newFirst + 1;
                finish = newLast - newFirst + 1;
            }
            for (int i = start; i < finish; i++) {
                View view = getChildAt(i);
                if (view == null)
                    return p;

                AnimatorSet oldAnimation = animations.get(view);
                if (oldAnimation != null) {
                    oldAnimation.cancel();
                    animations.remove(view);
                }

                AnimatorSet animator;
                view.setPivotY(view.getHeight() / 2);
                view.setPivotX(view.getWidth() / 2);
                animator = new AnimatorSet();
                animator.playTogether(
                        ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                        ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                        ObjectAnimator.ofFloat(view, "rotationX", 180f, 360f),
                        ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                );
                animator.setDuration(400);
                animations.put(view, animator);
                animator.start();
            }
        }

        currentFirst = newFirst;
        currentLast = newLast;

        return p;
    }

    @Override
    public void detachView(View child) {
        super.detachView(child);
        Log.w("Manager", "Detaching");
    }

    @Override
    public void detachAndScrapView(View child, RecyclerView.Recycler recycler) {
        super.detachAndScrapView(child, recycler);
        Log.w("Manager", "Detaching2");
    }
}
