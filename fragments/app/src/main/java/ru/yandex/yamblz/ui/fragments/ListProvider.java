package ru.yandex.yamblz.ui.fragments;

import java.util.List;

import ru.yandex.yamblz.model.Singer;

/**
 * Created by vorona on 03.08.16.
 */

public interface ListProvider{
    List<Singer> getList();
}