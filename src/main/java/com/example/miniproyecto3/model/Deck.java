package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.enums.Suit;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.interfaces.IDeckOperations;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents the draw deck used during a match: a standard 52-card deck
 * (one {@link Card} per combination of {@link Rank} and {@link Suit}),
 * implemented as a {@link Stack} so that drawing always removes the card on
 * top.
 *
 * Implements {@link IDeckOperations}, the contract shared by any object
 * capable of providing draw/recycle operations on a set of cards.
 *
 * All mutating operations are synchronized because the deck is
 * accessed concurrently by the JavaFX thread and by the background threads
 * that resolve AI turns and card draws
 * (MachinePlayerThread, DrawCardThread).
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class Deck implements IDeckOperations {

    private final Stack<Card> cards;

    /**
     * Creates a new deck containing all 52 standard cards and shuffles it.
     */
    public Deck() {
        cards = new Stack<>();
        initializeDeck();
    }

    /**
     * Populates the deck with one card per combination of suit and rank,
     * then shuffles it.
     */
    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.push(new Card(rank, suit));
            }
        }
        shuffle();
    }

    /**
     * Removes and returns the card on top of the deck.
     *
     * @return the drawn card.
     * @throws EmptyDeckException if the deck has no cards left.
     */
    @Override
    public synchronized Card draw() throws EmptyDeckException {
        if (isEmpty()) {
            throw new EmptyDeckException("The deck is empty, cannot draw a card.");
        }
        return cards.pop();
    }

    /**
     * Randomly shuffles the order of the cards currently in the deck.
     */
    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Returns a previously discarded set of cards back into the deck (for
     * example, the hand of an eliminated player, or the table pile when the
     * deck runs out), and shuffles the deck afterward.
     *
     * @param returnedCards the cards to add back into the deck.
     */
    @Override
    public synchronized void recycle(List<Card> returnedCards) {
        for (Card card : returnedCards) {
            cards.push(card);
        }
        shuffle();
    }

    /**
     * @return {@code true} if the deck has no cards left.
     */
    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * @return the number of cards currently remaining in the deck.
     */
    @Override
    public int size() {
        return cards.size();
    }
}
