package ru.yandex.yamblz.ui.views;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by vorona on 02.08.16.
 */

public abstract class BaseProgressIndicator extends View {

    public BaseProgressIndicator(Context context) {
        super(context);
    }

    public BaseProgressIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseProgressIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void createAnimation();

    public void initAnimation(){
        createAnimation();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
            initAnimation();
    }
}
