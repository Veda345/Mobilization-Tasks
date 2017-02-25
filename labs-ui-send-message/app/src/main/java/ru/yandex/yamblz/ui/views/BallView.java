package ru.yandex.yamblz.ui.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.yandex.yamblz.R;

/**
 * Created by vorona on 07.08.16.
 */

public class BallView extends LinearLayout {

    @BindView(R.id.ball_img)
    ImageView ball;
    @BindView(R.id.my_button)
    ButtonView button;
    @BindView(R.id.txtSuccess)
    TextView txtSuccess;

    private Paint background;
    private ObjectAnimator anim;

    public BallView(Context context) {
        super(context);
        init(context);
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initColor(context, attrs);
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initColor(context, attrs);
    }

    private void initColor(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BallView, 0, 0);
        String str = a.getString(R.styleable.BallView_ball_color);
        background.setColor(Color.parseColor(str));
        a.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.loading_ball, this);
        ButterKnife.bind(this, view);
        background = new Paint();
        txtSuccess.setAlpha(0);
    }

    private Bitmap drawCircle() {
        Bitmap tempBitmap = Bitmap.createBitmap(getHeight(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawColor(Color.TRANSPARENT);
        tempCanvas.drawCircle(getHeight() / 2, getHeight() / 2, getHeight() / 2, background);
        return tempBitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ball.setImageDrawable(new BitmapDrawable(getResources(), drawCircle()));
        ObjectAnimator.ofFloat(ball, "alpha", 0f).setDuration(0).start();
    }

    public void animateWaiting() {
        ObjectAnimator.ofFloat(ball, "alpha", 1f).setDuration(0).start(); //show ball inspite of button

        anim = ObjectAnimator.ofFloat(ball, "translationY", 300, -300, 300); //jumps of the ball while waiting
        anim.setDuration(700);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setInterpolator(new DecelerateInterpolator(getContext(), null));

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(button.closeButton(),
                ObjectAnimator.ofFloat(ball, "translationY", 0, 300).setDuration(200),
                anim);
        set.start();
    }

    public void finishWaitingWithSuccess() {
        Log.w("BallView", "finishwithsuccess");
        anim.end(); //end jumping

        AnimatorSet setSuccess = new AnimatorSet(); //animate left part of the tick
        setSuccess.playTogether(
                ObjectAnimator.ofFloat(ball, "translationY", 300, -200, 300),
                ObjectAnimator.ofFloat(ball, "translationX", 0, -300, 0)
        );
        setSuccess.setDuration(500);

        ObjectAnimator text = ObjectAnimator.ofFloat(txtSuccess, "alpha", 0, 1);
        text.setInterpolator(new BounceInterpolator(getContext(), null));

        AnimatorSet setSuccessRight = new AnimatorSet(); //animate right part of the tick
        setSuccessRight.playTogether(
                ObjectAnimator.ofFloat(ball, "translationY", 300, -300),
                ObjectAnimator.ofFloat(ball, "translationX", 0, 400),
                ObjectAnimator.ofFloat(ball, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(ball, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(ball, "alpha", 1f, 0f),
                text
        );
        setSuccessRight.setDuration(300);

        AnimatorSet setTick = new AnimatorSet();
        setTick.playSequentially(setSuccess, setSuccessRight);
        setTick.start();
    }
}
