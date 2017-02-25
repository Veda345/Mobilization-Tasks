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

public class IndicatorBalls1 extends BaseProgressIndicator {

    private float scaleBorder = 1f, scaleCentre = 1f;

    private int alphaCentre = 255, alphaBorder = 127;

    private Paint paint;

    public IndicatorBalls1(Context context) {
        super(context);
        init();
    }

    public IndicatorBalls1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorBalls1(Context context, AttributeSet attrs, int defStyleAttr) {
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
        drawCircle(canvas, rad, alphaBorder, x, y, scaleBorder);
        drawCircle(canvas, rad, alphaCentre, x + (rad * 2), y, scaleCentre);
        drawCircle(canvas, rad, alphaBorder, x + (rad * 2) * 2, y, scaleBorder);
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
        for (int i = 0; i < 3; i++) {
            ValueAnimator scale = ValueAnimator.ofFloat(1, 0.75f, 1);
            scale.setDuration(600);
            scale.setRepeatMode(ValueAnimator.RESTART);
            scale.setRepeatCount(ValueAnimator.INFINITE);
            if (i == 0 || i == 2)
                scale.setStartDelay(300);
            int finalI = i;
            scale.addUpdateListener(animation -> {
                if (finalI == 1)
                    scaleCentre = (float) animation.getAnimatedValue();
                else
                    scaleBorder = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            scale.start();

            ValueAnimator alpha = ValueAnimator.ofInt(200, 45, 200);
            alpha.setDuration(600);
            alpha.setRepeatMode(ValueAnimator.RESTART);
            alpha.setRepeatCount(ValueAnimator.INFINITE);
            if (i == 0 || i == 2)
                alpha.setStartDelay(300);
            alpha.addUpdateListener(animation -> {
                if (finalI == 1)
                    alphaCentre = (int) animation.getAnimatedValue();
                else
                    alphaBorder = (int) animation.getAnimatedValue();
                postInvalidate();
            });
            alpha.start();
        }
    }

}