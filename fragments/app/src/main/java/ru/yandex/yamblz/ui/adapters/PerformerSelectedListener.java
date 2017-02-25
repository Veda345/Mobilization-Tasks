package ru.yandex.yamblz.ui.adapters;


import ru.yandex.yamblz.model.Singer;

/**
 *  Interface for receiving events from RecycleView
 */
public interface PerformerSelectedListener {
    /**
     * Creates new activity with full information about singer
     * @param singer chosen in RecycleView singer
     */
    void onPerformerSelected(int singer);
}
