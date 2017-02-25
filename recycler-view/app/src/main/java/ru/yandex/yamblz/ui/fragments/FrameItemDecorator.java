package ru.yandex.yamblz.ui.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vorona on 22.07.16.
 */
public class FrameItemDecorator extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private static final int[] BORDER_WIDTH = {10, 20, 30, 40};
    private static final int[] BORDER_COLOR = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};

    private Drawable mDivider;
    private Paint mPaintStroke;

    /**
     * Default divider will be used
     */
    public FrameItemDecorator(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        mDivider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
        mPaintStroke = new Paint();
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setColor(Color.WHITE);
    }

    private int getBorderWidth(RecyclerView parent, View child) {
        int pos = Math.max(parent.getChildAdapterPosition(child),0);
        return BORDER_WIDTH[pos % BORDER_WIDTH.length];
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            // Get the bounds
            int cnt = getBorderWidth(parent, child);
            mPaintStroke.setStrokeWidth(cnt);
            int pos = Math.max(parent.getChildAdapterPosition(child), 0);
            mPaintStroke.setColor(BORDER_COLOR[pos % BORDER_COLOR.length]);
            RectF rect = getBounds(child, cnt);
            c.drawRect(rect, mPaintStroke);

        }
    }

    private RectF getBounds(View child, int cnt) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int left = child.getLeft() + child.getPaddingLeft() + cnt/2 + (int)child.getTranslationX();
        int right = child.getRight() - child.getPaddingRight() - cnt/2 + (int)child.getTranslationX();

        int top = child.getTop() + params.topMargin + cnt/2 + (int)child.getTranslationY();
        int bottom = child.getBottom() - params.bottomMargin - cnt/2 + (int)child.getTranslationY();

        return new RectF(left, top, right, bottom);
    }

}