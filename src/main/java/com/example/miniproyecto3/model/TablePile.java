package com.example.miniproyecto3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents the central discard pile on the table, where every played card
 * is stacked and whose values accumulate into a running total
 * ({@link #getCurrentSum()}). This running total is the core rule of
 * Cincuentazo: it must never exceed {@link GameConstants#MAX_SUM}.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class TablePile {

    private final Stack<Card> pile;
    private int currentSum;

    /**
     * Creates an empty table pile with the running sum at zero.
     */
    public TablePile() {
        pile = new Stack<>();
        currentSum = 0;
    }

    /**
     * Adds a card to the pile, calculating its value automatically based on
     * the current sum, and updates the running total.
     *
     * @param card the card being played.
     */
    public void addCard(Card card) {
        int cardValue = card.getValue(currentSum);
        currentSum += cardValue;
        pile.push(card);
    }

    /**
     * Adds a card to the pile using an explicitly forced value instead of
     * the one calculated from its rank. Used for the Ace, whose value (10 or
     * 1) is chosen by the human player.
     *
     * @param card        the card being played.
     * @param forcedValue the value to add to the running sum.
     */
    public void addCardWithValue(Card card, int forcedValue) {
        currentSum += forcedValue;
        pile.push(card);
    }

    /**
     * @return the card currently on top of the pile, or {@code null} if the
     *         pile is empty.
     */
    public Card getTopCard() {
        if (pile.isEmpty()) {
            return null;
        }
        return pile.peek();
    }

    /**
     * @return the current accumulated sum of the table pile.
     */
    public int getCurrentSum() {
        return currentSum;
    }

    /**
     * Returns every card in the pile except the one currently on top,
     * intended to be returned to the deck when it runs out of cards. The top
     * card is kept on the pile so the game can continue from the same
     * running sum.
     *
     * @return the list of cards eligible to be recycled back into the deck.
     */
    public List<Card> collectForRecycle() {
        List<Card> recycled = new ArrayList<>(pile);
        Card top = pile.peek();
        recycled.remove(top);
        return recycled;
    }

    /**
     * @return {@code true} if no card has been played onto the pile yet.
     */
    public boolean isEmpty() {
        return pile.isEmpty();
    }
}