package ru.yandex.yamblz.ui.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.yandex.yamblz.R;

/**
 * Created by vorona on 07.08.16.
 */

public class BellView extends FrameLayout {

    @BindView(R.id.txt_count)
    TextView txtCount;
    @BindView(R.id.bell)
    ImageView imgBell;
    @BindView(R.id.half_circle)
    ImageView imgHalf;

    private boolean shown = false;
    private final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.sound);

    public BellView(Context context) {
        super(context);
        init(context);
    }

    public BellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public BellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.events_layout, this);
        ButterKnife.bind(this, view);
        txtCount.animate().alpha(0).setDuration(0).start();
    }

    public void animateEvents(int events) {
        shown = true;
        txtCount.setText(Integer.toString(events));
        mp.start();
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        imgBell.setPivotX(imgBell.getWidth()/2);
        imgBell.setPivotY(0);
        AnimatorSet half1;
        half1 = new AnimatorSet();
        half1.playTogether(
                ObjectAnimator.ofFloat(imgHalf, "translationX", 10, 0, -10, 0, 8, 0, -8, 0),
                ObjectAnimator.ofFloat(imgHalf, "translationY", -5, 0, -5, 0, -4, 0, -4, 0),
                ObjectAnimator.ofFloat(imgBell, "rotation", -25, 0, 25, -20, 0, 20, 0)
                );
        half1.setDuration(800);

        AnimatorSet animator;
        animator = new AnimatorSet();
        animator.playTogether(
                ObjectAnimator.ofFloat(txtCount, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(txtCount, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(txtCount, "alpha", 0f, 1f)
        );
        animator.setDuration(200);
        animator.setStartDelay(500);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(half1, animator);
        set.start();
    }

    public void deleteEvents(){
        shown = false;
        txtCount.setText("0");
        AnimatorSet animator;
        animator = new AnimatorSet();
        animator.playTogether(ObjectAnimator.ofFloat(txtCount, "alpha", 1f, 0f));
        animator.setDuration(0);
        animator.start();
    }

    public boolean notificationShown(){
        return shown;
    }
}
