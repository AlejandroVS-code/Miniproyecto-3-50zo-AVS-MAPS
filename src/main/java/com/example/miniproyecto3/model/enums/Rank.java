package com.example.miniproyecto3.model.enums;

/**
 * The thirteen ranks of a standard deck of playing cards, each carrying its
 * base point value in Cincuentazo and the base path to its front-face image
 * resource.
 *
 * Value rules:
 *
 * 2 through 8: worth their face value.
 * 9: neutral, worth 0.
 * 10: worth 10.
 * Jack, Queen, King: worth -10 (they subtract from the table sum).
 * Ace: worth 10 if doing so keeps the table sum at 50 or below;
 * otherwise worth 1, so it can always be played safely.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
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

    /**
     * @param baseValue the fixed point value for this rank (ignored for {@link #ACE}).
     * @param imagePath the base classpath prefix used to build this rank's image path.
     */
    Rank(int baseValue, String imagePath) {
        this.baseValue = baseValue;
        this.imagePath = imagePath;
    }

    /**
     * Calculates this rank's point value given the current table sum.
     * Every rank except {@link #ACE} returns a fixed value; the Ace returns
     * 10 if that would keep the sum at 50 or below, or 1 otherwise, so that
     * it is never an invalid play by itself.
     *
     * @param tableSum the current accumulated sum on the table pile.
     * @return the value this rank contributes if played.
     */
    public int getValue(int tableSum) {
        if (this == ACE) {
            return (tableSum + 10 <= 50) ? 10 : 1;
        }
        return baseValue;
    }

    /**
     * Builds the full classpath resource path to this rank's front-face
     * image for the given suit.
     *
     * @param suit the suit to combine with this rank.
     * @return the resource path, e.g. {@code ".../Cartas/ace_of_hearts.png"}.
     */
    public String getImagePath(Suit suit) {
        return imagePath + suit.getLabel() + ".png";
    }
}