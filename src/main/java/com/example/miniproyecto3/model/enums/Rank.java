package com.example.miniproyecto3.model.enums;

public enum Rank {

    TWO(2, "/com/example/miniproyecto3/Cartas/2_of_"),
    THREE(3, "/com/example/miniproyecto3/Cartas/3_of_"),
    FOUR(4, "/com/example/miniproyecto3/Cartas/4_of_"),
    FIVE(5, "/com/example/miniproyecto3/Cartas/5_of_"),
    SIX(6, "/com/example/miniproyecto3/Cartas/6_of_"),
    SEVEN(7, "/com/example/miniproyecto3/Cartas/7_of_"),
    EIGHT(8, "/com/example/miniproyecto3/Cartas/8_of_"),
    NINE(0, "/com/example/miniproyecto3/Cartas/9_of_"),
    TEN(10, "/com/example/miniproyecto3/Cartas/10_of_"),
    JACK(-10, "/com/example/miniproyecto3/Cartas/jack_of_"),
    QUEEN(-10, "/com/example/miniproyecto3/Cartas/queen_of_"),
    KING(-10, "/com/example/miniproyecto3/Cartas/king_of_"),
    ACE(0, "/com/example/miniproyecto3/Cartas/ace_of_");

    private final int baseValue;
    private final String imagePath;

    Rank(int baseValue, String imagePath) {
        this.baseValue = baseValue;
        this.imagePath = imagePath;
    }

    public int getValue(int tableSum) {
        if (this == ACE) {
            return (tableSum + 10 <= 50) ? 10 : 1;
        }
        return baseValue;
    }

    public String getImagePath(Suit suit) {
        return imagePath + suit.getLabel() + ".png";
    }
}