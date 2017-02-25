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

public class IndicatorSquare2 extends BaseProgressIndicator {

    private float transitionX, transitionY;
    private Paint paint;

    public IndicatorSquare2(Context context) {
        super(context);
        init();
    }

    public IndicatorSquare2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorSquare2(Context context, AttributeSet attrs, int defStyleAttr) {
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
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(transitionX, transitionY);
        canvas.drawRect(-x/2, -y/2, x/2, y/2, paint);
        canvas.restore();
    }

    @Override
    public void createAnimation() {
        ValueAnimator transition = ValueAnimator.ofFloat(1f, 0.1f);
        transition.setDuration(600);
        transition.setRepeatMode(ValueAnimator.REVERSE);
        transition.setRepeatCount(ValueAnimator.INFINITE);
        transition.addUpdateListener(animation -> {
            this.transitionY = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        transition.start();

        transition = ValueAnimator.ofFloat(1f, 0.1f);
        transition.setDuration(600);
        transition.setStartDelay(900);
        transition.setRepeatMode(ValueAnimator.REVERSE);
        transition.setRepeatCount(ValueAnimator.INFINITE);
        transition.addUpdateListener(animation -> {
            this.transitionX = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        transition.start();
    }

}