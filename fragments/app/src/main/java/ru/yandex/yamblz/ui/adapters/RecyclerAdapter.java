package ru.yandex.yamblz.ui.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.yandex.yamblz.R;

/**
 * Adapter for RecycleView
 */
public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.GroupsViewHolder>
        implements View.OnClickListener {

    PerformerSelectedListener performerSelectedListener;

    RecyclerAdapter(PerformerSelectedListener listener) {
        performerSelectedListener = listener;
    }

    public void setPerformerSelectedListener(PerformerSelectedListener listener) {
        Log.w("Adapter", "Listener Set");
        performerSelectedListener = listener;
    }

    class GroupsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView groupName, genres;
        ImageView cover;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.name);
            genres = (TextView) itemView.findViewById(R.id.genres);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.w("Adapter", "OnClick");
            int singer = (int) v.getTag(R.id.tag);
            if (performerSelectedListener != null) {
                performerSelectedListener.onPerformerSelected(singer);
            }
        }
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new GroupsViewHolder(view);
    }


    @Override
    public void onClick(View v) {

    }

}
