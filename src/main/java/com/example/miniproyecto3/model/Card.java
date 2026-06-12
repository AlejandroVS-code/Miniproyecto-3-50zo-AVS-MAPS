package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.enums.Suit;

public class Card {

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue(int tableSum) {
        return rank.getValue(tableSum);
    }

    public String getImagePath() {
        return rank.getImagePath(suit);
    }

    public String getBackImagePath() {
        return "/com/example/miniproyecto3/Imagenes/reverse.png";
    }

    @Override
    public String toString() {
        return rank.name().toLowerCase() + " of " + suit.getLabel();
    }
}