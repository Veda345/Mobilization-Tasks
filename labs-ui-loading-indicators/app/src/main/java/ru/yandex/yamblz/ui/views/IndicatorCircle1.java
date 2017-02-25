package ru.yandex.yamblz.ui.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;


/**
 * Created by vorona on 02.08.16.
 */

public class IndicatorCircle1 extends BaseProgressIndicator {

    private float transitionX;
    private int alpha = 255;
    private Paint paint;

    public IndicatorCircle1(Context context) {
        super(context);
        init();
    }

    public IndicatorCircle1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorCircle1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = getWidth()/2;
        float y = getHeight()/2;
        float rad = x;
        drawCircle(canvas, rad, alpha, x, y, transitionX);
    }

    private void drawCircle(Canvas canvas, float radius, int alpha, float x, float y, float scale) {
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        paint.setAlpha(alpha);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();
    }


    @Override
    public void createAnimation() {
        ValueAnimator transition = ValueAnimator.ofFloat(0f, 1f);
        transition.setDuration(1000);
        transition.setRepeatMode(ValueAnimator.RESTART);
        transition.setRepeatCount(ValueAnimator.INFINITE);
        transition.addUpdateListener(animation -> {
            this.transitionX = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        transition.start();

        ValueAnimator alpha = ValueAnimator.ofInt(255, 0);
        alpha.setDuration(1000);
        alpha.setRepeatMode(ValueAnimator.RESTART);
        alpha.setRepeatCount(ValueAnimator.INFINITE);
        alpha.addUpdateListener(animation -> {
            this.alpha = (int) animation.getAnimatedValue();
            postInvalidate();
        });
        alpha.start();
    }

}