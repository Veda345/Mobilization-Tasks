package ru.yandex.yamblz.ui.adapters;

import java.util.List;

import ru.yandex.yamblz.R;
import ru.yandex.yamblz.model.Singer;

/**
 * Adapter for given list of performers
 */
public class FirstRecyclerAdapter extends RecyclerAdapter{

    private List<Singer> singers;

    public FirstRecyclerAdapter(List<Singer> s, PerformerSelectedListener listener) {
        super(listener);
        singers = s;
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        holder.groupName.setText(singers.get(position).getName());
        holder.genres.setText(singers.get(position).getGenres());
        holder.itemView.setTag(R.id.tag, position);
    }

    @Override
    public int getItemCount() {
        if (singers == null) return 0;
        return singers.size();
    }
}
