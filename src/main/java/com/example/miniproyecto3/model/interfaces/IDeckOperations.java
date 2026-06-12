package com.example.miniproyecto3.model.interfaces;

import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import java.util.List;

public interface IDeckOperations {

    Card draw() throws EmptyDeckException;
    void shuffle();
    void recycle(List<Card> cards);
    boolean isEmpty();
    int size();
}