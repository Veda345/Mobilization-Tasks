package ru.yandex.yamblz.ui.adapters;

/**
 * Created by vorona on 19.07.16.
 */
public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
