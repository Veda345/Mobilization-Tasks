package ru.yandex.yamblz.ui.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.Random;

/**
 * Created by vorona on 02.08.16.
 */

public class IndicatorBalls4 extends BaseProgressIndicator {

    private int alpha[] = {255, 255, 255, 255, 20, 255, 255, 255, 255};
    private float scale[] = {1f, 1f, 1f, 1f, 0.5f, 1f, 1f, 1f, 1f};
    private Random rand;

    private Paint paint;

    public IndicatorBalls4(Context context) {
        super(context);
        init();
    }

    public IndicatorBalls4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorBalls4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        rand = new Random();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float rad = (getWidth() - 16) / 6;
        float x = getWidth() / 2;
        float y = x;
        int px = -1, py;
        for (int i = 0; i < 3; i++) {
            py = -1;
            for (int j = 0; j < 3; j++) {
                drawCircle(canvas, rad, alpha[i*3+j], x + (rad*2 + 8) * px, y + (rad*2 + 8) * py, scale[i*3+j]);
                py++;
            }
            px++;
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
        for (int i = 0; i < 9; i++) {
            ValueAnimator scale = ValueAnimator.ofFloat(1, 0.5f);
            scale.setDuration(600);
            scale.setRepeatMode(ValueAnimator.REVERSE);
            scale.setRepeatCount(ValueAnimator.INFINITE);
            scale.setStartDelay(rand.nextInt(500));
            int finalI = i;
            scale.addUpdateListener(animation -> {
                this.scale[finalI] = (float) scale.getAnimatedValue();
                postInvalidate();
            });
            scale.start();

            ValueAnimator alpha = ValueAnimator.ofInt(255, 50);
            alpha.setDuration(400);
            alpha.setStartDelay(rand.nextInt(500));
            alpha.setRepeatMode(ValueAnimator.REVERSE);
            alpha.setRepeatCount(ValueAnimator.INFINITE);
            alpha.addUpdateListener(animation -> {
                this.alpha[finalI] = (int) alpha.getAnimatedValue();
                postInvalidate();
            });
            alpha.start();
        }
    }

}