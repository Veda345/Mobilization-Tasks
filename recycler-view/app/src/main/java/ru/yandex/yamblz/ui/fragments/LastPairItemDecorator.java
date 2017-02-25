package ru.yandex.yamblz.ui.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.adapters.ContentAdapter;

/**
 * Created by vorona on 22.07.16.
 */
class LastPairItemDecorator extends RecyclerView.ItemDecoration {

    private int position1 = -1, position2 = -1;
    private Paint paint;

    public LastPairItemDecorator() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() instanceof ContentAdapter) {
            ContentAdapter.LastPair pair = ((ContentAdapter) parent.getAdapter()).getLastMovedPair();
            if (pair != null) {
                position1 = pair.getFirst();
                position2 = pair.getSecond();
            }
        }
        if (position1 >= 0 && position2 >= 0) {
            RecyclerView.ViewHolder child = parent.findViewHolderForAdapterPosition(position1);
            drawCircle(c, child);
            child = parent.findViewHolderForAdapterPosition(position2);
            drawCircle(c, child);
        }
    }

    private void drawCircle(Canvas c, RecyclerView.ViewHolder child) {
        if (child != null) {
            View lastItemFirst = child.itemView;
            c.drawCircle(lastItemFirst.getX() + lastItemFirst.getWidth() / 2,
                    lastItemFirst.getY() + lastItemFirst.getHeight() / 2,
                    30, paint);
        }
    }

}