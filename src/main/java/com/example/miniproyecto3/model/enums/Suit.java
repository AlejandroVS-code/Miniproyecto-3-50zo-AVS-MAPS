package com.example.miniproyecto3.model.enums;

/**
 * The four suits of a standard deck of playing cards.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public enum Suit {

    HEARTS,
    DIAMONDS,
    CLUBS,
    SPADES;

    /**
     * @return the Unicode symbol associated with this suit (e.g. {@code "♥"}).
     */
    public String getSymbol() {
        return switch (this) {
            case HEARTS   -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS    -> "♣";
            case SPADES   -> "♠";
        };
    }

    /**
     * @return the lowercase name of this suit, used to build resource file
     *         names (e.g. {@code "hearts"}).
     */
    public String getLabel() {
        return this.name().toLowerCase();
    }
}

