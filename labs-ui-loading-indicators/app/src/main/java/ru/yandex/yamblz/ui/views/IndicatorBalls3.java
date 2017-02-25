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

public class IndicatorBalls3 extends BaseProgressIndicator {

    private float scale[] = new float[3];
    private Paint paint;

    public IndicatorBalls3(Context context) {
        super(context);
        init();
    }

    public IndicatorBalls3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorBalls3(Context context, AttributeSet attrs, int defStyleAttr) {
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
        drawCircle(canvas, rad, x, y, scale[0]);
        drawCircle(canvas, rad, x + (rad * 2 + 8), y, scale[1]);
        drawCircle(canvas, rad, x + (rad * 2 + 8) * 2, y, scale[2]);
    }

    private void drawCircle(Canvas canvas, float radius, float x, float y, float scale) {
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();
    }


    @Override
    public void createAnimation() {
        for (int i = 0; i < 3; i++) {
            ValueAnimator transition = ValueAnimator.ofFloat(1f, 0.25f);
            transition.setDuration(600);
            transition.setRepeatMode(ValueAnimator.REVERSE);
            transition.setRepeatCount(ValueAnimator.INFINITE);
            transition.setStartDelay(i*100);
            int finalI = i;
            transition.addUpdateListener(animation -> {
                scale[finalI] = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            transition.start();
        }
    }

}