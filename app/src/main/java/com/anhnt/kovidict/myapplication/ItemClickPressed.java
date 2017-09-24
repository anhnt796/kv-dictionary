package com.anhnt.kovidict.myapplication;

import com.anhnt.kovidict.myapplication.entities.Origin;

public interface ItemClickPressed {
    void onItemPressed(Origin origin);

    void updateFavourite();
}