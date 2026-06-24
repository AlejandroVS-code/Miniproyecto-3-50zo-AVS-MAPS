package com.example.miniproyecto3.model.interfaces;

import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import java.util.List;

/**
 * Contract for the basic operations any draw-deck implementation must
 * support: drawing, shuffling, recycling discarded cards back in, and
 * querying its state. Implemented by {@code Deck}.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public interface IDeckOperations {

    /**
     * Removes and returns the card on top of the deck.
     *
     * @return the drawn card.
     * @throws EmptyDeckException if the deck has no cards left.
     */
    Card draw() throws EmptyDeckException;

    /**
     * Randomly shuffles the order of the cards currently in the deck.
     */
    void shuffle();

    /**
     * Returns a set of previously discarded cards back into the deck.
     *
     * @param cards the cards to recycle.
     */
    void recycle(List<Card> cards);

    /**
     * @return {@code true} if the deck has no cards left.
     */
    boolean isEmpty();

    /**
     * @return the number of cards currently remaining in the deck.
     */
    int size();
}