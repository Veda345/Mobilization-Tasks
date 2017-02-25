package com.example.vorona.carrousel;

import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.ListParsingHandler;

import java.util.ArrayList;
import java.util.List;


public class ListHandler extends ListParsingHandler {

    List<ListItem> listItems = new ArrayList<>();
    @Override
    public boolean handleItem(ListItem item) {
        listItems.add(item);
        return true;
    }
}
