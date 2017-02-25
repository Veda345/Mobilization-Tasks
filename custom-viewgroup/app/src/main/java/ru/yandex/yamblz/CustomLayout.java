package ru.yandex.yamblz;

/**
 * Created by vorona on 18.07.16.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import static android.view.View.MeasureSpec.*;
import static android.view.ViewGroup.LayoutParams.*;

@RemoteViews.RemoteView
public class CustomLayout extends ViewGroup {
    /**
     * The amount of space used by children in the left gutter.
     */
    private int mLeftWidth;

    /**
     * The amount of space used by children in the right gutter.
     */
    private int mRightWidth;

    /**
     * These are used for computing child frames based on their gravity.
     */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int sumWidth = 0;

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int indMatchParent = -1;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() != GONE) {
                // Measure the child.
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                if (lp.width == MATCH_PARENT) {
                        indMatchParent = i;
                    continue;
                } else {
                    sumWidth += child.getMeasuredWidth();
                }
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight());
            }
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        int widthMatchParent = MeasureSpec.getSize(widthMeasureSpec) - sumWidth;

        widthMatchParent = Math.max(0, widthMatchParent);
        LayoutParams childLayoutParams = (LayoutParams) getChildAt(indMatchParent).getLayoutParams();

        measureChild(getChildAt(indMatchParent), MeasureSpec.makeMeasureSpec(widthMatchParent, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY));
        sumWidth += getChildAt(indMatchParent).getMeasuredWidth();
        setMeasuredDimension(resolveSize(sumWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();

        // These are the top and bottom edges in which we are performing layout.
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                // Compute the frame in which we are placing this child.

                mTmpContainerRect.left = leftPos + lp.leftMargin;
                mTmpContainerRect.right = leftPos + width + lp.rightMargin;
                leftPos = mTmpContainerRect.right;

                mTmpContainerRect.top = parentTop + lp.topMargin;
                mTmpContainerRect.bottom = parentBottom - lp.bottomMargin;

                // Use the child's gravity and size to determine its final
                // frame within its container.
                Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);

                // Place the child.
                child.layout(mTmpChildRect.left, mTmpChildRect.top,
                        mTmpChildRect.right, mTmpChildRect.bottom);
            }
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * Custom per-child layout information.
     */
    public static class LayoutParams extends MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
        public int gravity = Gravity.TOP | Gravity.START;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
