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

public class IndicatorBalls2 extends BaseProgressIndicator {

    private int transition[] = new int[3];
    private Paint paint;

    public IndicatorBalls2(Context context) {
        super(context);
        init();
    }

    public IndicatorBalls2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorBalls2(Context context, AttributeSet attrs, int defStyleAttr) {
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
        float rad = (getWidth() - 16) / 6;
        float x = getWidth() / 2 - (rad * 2 + 8);
        float y = getHeight() / 2;
        drawCircle(canvas, rad, x, rad + transition[0]);
        drawCircle(canvas, rad, x + (rad * 2 + 8), rad + transition[1]);
        drawCircle(canvas, rad, x + (rad * 2 + 8) * 2, rad + transition[2]);
    }

    private void drawCircle(Canvas canvas, float radius, float x, float y) {
        canvas.save();
        canvas.translate(x, y);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();
    }


    @Override
    public void createAnimation() {
        for (int i = 0; i < 3; i++) {
            ValueAnimator transition = ValueAnimator.ofInt(100, 0, 100);
            transition.setDuration(800);
            transition.setRepeatMode(ValueAnimator.RESTART);
            transition.setRepeatCount(ValueAnimator.INFINITE);
            transition.setStartDelay(i*200);
            int finalI = i;
            transition.addUpdateListener(animation -> {
                this.transition[finalI] = (int) animation.getAnimatedValue();
                postInvalidate();
            });
            transition.start();
        }
    }

}