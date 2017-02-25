package ru.yandex.yamblz.ui.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xdty.preference.colorpicker.ColorPickerDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.yandex.yamblz.R;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> implements ItemTouchHelperAdapter, RecyclerView.OnItemTouchListener {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();
    private LastPair pair;


    public ContentAdapter(Context context) {
    }

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        holder.bind(createColorForPosition(position));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private Integer createColorForPosition(int position) {

        while (position >= colors.size()) {
            colors.add(Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
        }
        return colors.get(position);
    }

    @Override
    public void onItemDismiss(int position) {
        createColorForPosition(position);
        colors.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        int m = Math.max(fromPosition, toPosition);
        createColorForPosition(m);
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(colors, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(colors, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        pair = new LastPair(fromPosition, toPosition);
    }

    public LastPair getLastMovedPair(){
        return pair;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    static class ContentHolder extends RecyclerView.ViewHolder {
        ContentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                int[] mColors = {Color.parseColor("#c52424"), Color.parseColor("#f4d26c"),
                        Color.parseColor("#aeea00"), Color.parseColor("#1de9b6"),
                        Color.parseColor("#1e88e5"), Color.parseColor("#5e35b1"), Color.parseColor("#e040fb"),
                        Color.parseColor("#e91e63"), Color.parseColor("#f57f17"),
                        Color.CYAN, Color.RED, Color.GREEN, Color.YELLOW};

                ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                        mColors,
                        Color.YELLOW,
                        5, // Number of columns
                        ColorPickerDialog.SIZE_SMALL);

                dialog.setOnColorSelectedListener((color) -> {
                    ((TextView) v).setText("#".concat(Integer.toHexString(color).substring(2)));
                    final ObjectAnimator animator = ObjectAnimator.ofObject((v), "backgroundColor",
                            new ArgbEvaluator(), Color.WHITE, color);
                    animator.setDuration(500);
                    animator.start();
                });
                dialog.show(((Activity) v.getContext()).getFragmentManager(), "color_dialog_test");
            });
        }

        void bind(Integer color) {
            itemView.setBackgroundColor(color);
            ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
        }
    }

    public class LastPair {
        private int first, second;
        LastPair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }
    }
}
