package com.example.miniproyecto3.model.interfaces;

import com.example.miniproyecto3.model.Card;

@FunctionalInterface
public interface ICardClickHandler {
    void onCardClicked(Card card, int index);
}