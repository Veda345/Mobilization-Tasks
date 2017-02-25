package ru.yandex.yamblz.ui.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/**
 * Created by vorona on 02.08.16.
 */

public class IndicatorTriangle1 extends BaseProgressIndicator {

    int transitionX[] = {0, 0, 0};
    int transitionY[] = {0, 0, 0};
    int width;
    private Paint paint;

    public IndicatorTriangle1(Context context) {
        super(context);
        init();
    }

    public IndicatorTriangle1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorTriangle1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth()/2;
        float rad = (getWidth()) / 8;
        for (int i = 0; i < 3; i++)
            drawCircle(canvas, rad, Math.abs(transitionX[i] + rad), Math.abs(transitionY[i] + rad));
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
            ValueAnimator transX = ValueAnimator.ofInt(0, width/2, width, 0);
            transX.setDuration(2400);
            transX.setInterpolator(new LinearInterpolator());
            transX.setRepeatMode(ValueAnimator.RESTART);
            transX.setRepeatCount(ValueAnimator.INFINITE);
            transX.setStartDelay(i*800);
            int finalI = i;
            transX.addUpdateListener(animation -> {
                transitionX[finalI] = (int)animation.getAnimatedValue();
                postInvalidate();
            });
            transX.start();

            int w = (int)((width)* Math.sqrt(3)/2);
            ValueAnimator transY = ValueAnimator.ofInt(w, 0, w, w);
            transX.setInterpolator(new LinearInterpolator());
            transY.setDuration(2400);
            transY.setRepeatMode(ValueAnimator.RESTART);
            transY.setRepeatCount(ValueAnimator.INFINITE);
            transY.setStartDelay(i*800);
            transY.addUpdateListener(animation -> {
                transitionY[finalI] = (int)animation.getAnimatedValue();
                postInvalidate();
            });
            transY.start();
        }
    }

}