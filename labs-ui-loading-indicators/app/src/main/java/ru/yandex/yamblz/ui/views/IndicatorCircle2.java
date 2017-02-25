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

public class IndicatorCircle2 extends BaseProgressIndicator {

    private float scale[] = {0, 0, 0};
    private int alpha[] = {255, 255, 255};
    private Paint paint;

    public IndicatorCircle2(Context context) {
        super(context);
        init();
    }

    public IndicatorCircle2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorCircle2(Context context, AttributeSet attrs, int defStyleAttr) {
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
        for (int i = 0; i < 3; i++) {
            drawCircle(canvas, x, alpha[i], x, y, scale[i]);
        }

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
            ValueAnimator transition = ValueAnimator.ofFloat(0f, 1f);
            transition.setDuration(1800);
            transition.setStartDelay(600*i);
            transition.setRepeatMode(ValueAnimator.RESTART);
            transition.setRepeatCount(ValueAnimator.INFINITE);
            int finalI = i;
            transition.addUpdateListener(animation -> {
                this.scale[finalI] = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            transition.start();

            ValueAnimator alpha = ValueAnimator.ofInt(200, 0);
            alpha.setDuration(1800);
            alpha.setStartDelay(600*i);
            alpha.setRepeatMode(ValueAnimator.RESTART);
            alpha.setRepeatCount(ValueAnimator.INFINITE);
            alpha.addUpdateListener(animation -> {
                this.alpha[finalI] = (int) animation.getAnimatedValue();
                postInvalidate();
            });
            alpha.start();
        }
    }

}