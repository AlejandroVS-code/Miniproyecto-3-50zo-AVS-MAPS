package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.enums.Suit;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.interfaces.IDeckOperations;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck implements IDeckOperations {

    private final Stack<Card> cards;

    public Deck() {
        cards = new Stack<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.push(new Card(rank, suit));
            }
        }
        shuffle();
    }

    @Override
    public synchronized Card draw() throws EmptyDeckException {
        if (isEmpty()) {
            throw new EmptyDeckException("The deck is empty, cannot draw a card.");
        }
        return cards.pop();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public synchronized void recycle(List<Card> returnedCards) {
        for (Card card : returnedCards) {
            cards.push(card);
        }
        shuffle();
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public int size() {
        return cards.size();
    }
}
