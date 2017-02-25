package com.example.vorona.carrousel.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vorona.carrousel.R;

/**
 * Adapter for RecycleView
 */
public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.GroupsViewHolder>
        implements View.OnClickListener {

    public abstract void setPerformerSelectedListener(PerformerSelectedListener listener);

    class GroupsViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title_file, title_folder, info_file;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.cover);
            title_folder = (TextView) itemView.findViewById(R.id.title_folder);
            title_file = (TextView) itemView.findViewById(R.id.title_file);
            info_file = (TextView) itemView.findViewById(R.id.info_file);
        }
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);
        return new GroupsViewHolder(view);
    }


}
