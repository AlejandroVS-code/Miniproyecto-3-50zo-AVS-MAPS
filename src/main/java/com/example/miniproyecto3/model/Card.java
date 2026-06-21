package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.enums.Suit;

/**
 * Represents a single playing card, defined by its {@link Rank} (2-10, J, Q,
 * K, A) and {@link Suit} (hearts, diamonds, clubs, spades).
 *
 * A {@code Card} is immutable: once created, its rank and suit never
 * change. Its numeric value depends on the current table sum, since the Ace
 * can be worth either 10 or 1 (see {@link #getValue(int)}).
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class Card {

    private final Rank rank;
    private final Suit suit;

    /**
     * Creates a new card with the given rank and suit.
     *
     * @param rank the card's rank.
     * @param suit the card's suit.
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }


    /**
     * @return this card's rank.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * @return this card's suit.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Calculates this card's numeric value given the current table sum.
     * Delegates to {@link Rank#getValue(int)}, since most ranks have a fixed
     * value while the Ace's value depends on context.
     *
     * @param tableSum the current accumulated sum on the table pile.
     * @return the value this card contributes if played.
     */
    public int getValue(int tableSum) {
        return rank.getValue(tableSum);
    }

    /**
     * @return the classpath resource path of this card's front face image.
     */
    public String getImagePath() {
        return rank.getImagePath(suit);
    }

    /**
     * @return the classpath resource path of the shared card back image,
     *         used to render face-down cards (e.g. AI hands).
     */
    public String getBackImagePath() {
        return "/com/example/miniproyecto3/Imagenes/reverse.png";
    }

    /**
     * @return a human-readable representation of this card, e.g. {@code "ace of hearts"}.
     */
    @Override
    public String toString() {
        return rank.name().toLowerCase() + " of " + suit.getLabel();
    }
}