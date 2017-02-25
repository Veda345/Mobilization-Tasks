package com.example.vorona.carrousel.list;

import com.example.vorona.carrousel.file.File;

/**
 *  Interface for receiving events from RecycleView
 */
public interface PerformerSelectedListener {
    /**
     * Creates new activity with full information about singer
     * @param singer chosen in RecycleView singer
     */
    void onPerformerSelected(File singer);
}
