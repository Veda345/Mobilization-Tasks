package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.adapters.ContentAdapter;

public class ContentFragment extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.fab_menu)
    FloatingActionsMenu menu;

    private final String COLUMN_CNT = "cnt";
    private int columnCount = 1;
    private boolean frame = false;
    private FrameItemDecorator decorator;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            columnCount = savedInstanceState.getInt(COLUMN_CNT);
        }
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv.setLayoutManager(new CustomLayoutManager(getContext(), columnCount));
        ContentAdapter adapter = new ContentAdapter(getActivity());
        rv.setAdapter(adapter);
        LastPairItemDecorator dec = new LastPairItemDecorator();
        rv.addItemDecoration(dec);
        ItemTouchHelper.Callback callback = new TouchHelperCallback(adapter, dec);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);

        FloatingActionButton butOneColumn = new FloatingActionButton(getActivity());
        butOneColumn.setTitle("1/2 columns");
        butOneColumn.setIcon(R.drawable.ic_2columns);
        butOneColumn.setOnClickListener(v -> {
            if (columnCount == 1)
                columnCount++;
            else
                columnCount--;
            GridLayoutManager man = ((GridLayoutManager) rv.getLayoutManager());
            man.setSpanCount(columnCount);
            man.requestLayout();
            int pos = man.findFirstVisibleItemPosition();
            rv.getAdapter().notifyItemRangeChanged(pos, 0);
            menu.collapse();
        });
        menu.addButton(butOneColumn);

        FloatingActionButton butManyColumns = new FloatingActionButton(getActivity());
        butManyColumns.setTitle("many columns");
        butManyColumns.setIcon(R.drawable.ic_many_columns);
        butManyColumns.setOnClickListener(v -> {
            GridLayoutManager man = (GridLayoutManager) rv.getLayoutManager();
            man.setSpanCount(30);
            man.requestLayout();
            rv.setLayoutManager(man);
            int pos = man.findFirstVisibleItemPosition();
            rv.getAdapter().notifyItemRangeChanged(pos, 0);
            rv.getRecycledViewPool().setMaxRecycledViews(0, 300); //looks like optimizing, but i'm not sure
            menu.collapse();
        });
        menu.addButton(butManyColumns);

        FloatingActionButton butFrames = new FloatingActionButton(getActivity());
        butFrames.setTitle("frames");
        butFrames.setIcon(R.drawable.ic_frame);
        butFrames.setOnClickListener(v -> {

            if (!frame) {
                decorator = new FrameItemDecorator(getActivity());
                rv.addItemDecoration(decorator);
                frame = true;
            } else {
                rv.removeItemDecoration(decorator);
                frame = false;
            }
            menu.collapse();
        });
        menu.addButton(butFrames);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(COLUMN_CNT, columnCount);
        super.onSaveInstanceState(outState);
    }
}
