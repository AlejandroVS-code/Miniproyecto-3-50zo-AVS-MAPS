package com.example.miniproyecto3.model.enums;

public enum Suit {

    HEARTS,
    DIAMONDS,
    CLUBS,
    SPADES;

    public String getSymbol() {
        return switch (this) {
            case HEARTS   -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS    -> "♣";
            case SPADES   -> "♠";
        };
    }
    public String getLabel() {
        return this.name().toLowerCase();
    }
}


