package com.example.miniproyecto3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TablePile {

    private final Stack<Card> pile;
    private int currentSum;

    public TablePile() {
        pile = new Stack<>();
        currentSum = 0;
    }

    public void addCard(Card card) {
        int cardValue = card.getValue(currentSum);
        currentSum += cardValue;
        pile.push(card);
    }

    public Card getTopCard() {
        if (pile.isEmpty()) {
            return null;
        }
        return pile.peek();
    }

    public int getCurrentSum() {
        return currentSum;
    }

    public List<Card> collectForRecycle() {
        List<Card> recycled = new ArrayList<>(pile);
        Card top = pile.peek();
        recycled.remove(top);
        return recycled;
    }

    public boolean isEmpty() {
        return pile.isEmpty();
    }
}