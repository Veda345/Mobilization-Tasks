package ru.yandex.yamblz.ui.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.yamblz.R;

/**
 * Created by vorona on 07.08.16.
 */

public class ButtonView extends LinearLayout {

    @BindView(R.id.txtBtn)
    TextView txt;
    @BindView(R.id.left_bound)
    ImageView left;
    @BindView(R.id.right_bound)
    ImageView right;
    @BindView(R.id.rect)
    FrameLayout rect;

    private Paint background;

    public ButtonView(Context context) {
        super(context);
        init(context);
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initColor(context, attrs);
    }

    public ButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initColor(context, attrs);
    }

    private void initColor(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ButtonView, 0, 0);
        String str = a.getString(R.styleable.ButtonView_button_color);
        background.setColor(Color.parseColor(str));
        a.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.ball_button, this);
        ButterKnife.bind(this, view);
        background = new Paint();
    }

    private Bitmap drawCircle(int cx, int cy) {
        Bitmap tempBitmap = Bitmap.createBitmap(getHeight(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawColor(Color.TRANSPARENT);
        tempCanvas.drawCircle(cx, cy, getHeight() / 2, background);
        return tempBitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        left.setImageDrawable(new BitmapDrawable(getResources(), drawCircle(getHeight(), getHeight() / 2)));
        right.setImageDrawable(new BitmapDrawable(getResources(), drawCircle(0, getHeight() / 2)));
        txt.setBackgroundColor(background.getColor());
        txt.animate().alpha(1).setDuration(0).start();
        AnimatorSet animator;
        animator = new AnimatorSet();
        animator.playTogether(
                ObjectAnimator.ofFloat(txt, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(rect, "scaleX", 0f, 1.01f),
                ObjectAnimator.ofFloat(left, "translationX", getWidth()/2 - getHeight(), 0),
                ObjectAnimator.ofFloat(right, "translationX", -getWidth()/2 + getHeight(), 0)
        );
        animator.setDuration(600);
        animator.setInterpolator(new BounceInterpolator(getContext(), null));
        animator.start();
    }

    public Animator closeButton() {
        AnimatorSet animator;
        animator = new AnimatorSet();
        animator.playTogether(
                ObjectAnimator.ofFloat(txt, "scaleX", 0f),
                ObjectAnimator.ofFloat(rect, "scaleX", 0f),
                ObjectAnimator.ofFloat(txt, "alpha", 0f),
                ObjectAnimator.ofFloat(left, "translationX", getWidth() / 2 - left.getWidth()),
                ObjectAnimator.ofFloat(right, "translationX", -getWidth() / 2 + right.getWidth())
        );
        animator.setDuration(600);
        animator.setInterpolator(new BounceInterpolator(getContext(), null));
        AnimatorSet fade = new AnimatorSet();
        fade.playTogether(ObjectAnimator.ofFloat(this, "alpha", 1f, 0f));
        AnimatorSet finish = new AnimatorSet();
        finish.playSequentially(animator, fade);
        return finish;
    }

}
