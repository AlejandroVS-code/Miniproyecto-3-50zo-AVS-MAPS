package com.example.miniproyecto3.model.interfaces;

import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.Deck;
import com.example.miniproyecto3.model.TablePile;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;

/**
 * Contract for any entity capable of taking part in a turn: playing a card
 * onto the table and drawing a replacement card from the deck. Implemented
 * by {@code Player} and inherited by its subclasses, allowing the rest of
 * the system to trigger plays polymorphically without knowing whether the
 * player is human or AI-controlled.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public interface IPlayable {

    /**
     * Plays a card onto the given table pile.
     *
     * @param card  the card to play.
     * @param table the table pile to play onto.
     * @throws InvalidCardPlayException if the play would exceed the maximum
     *         allowed table sum.
     */
    void playCard(Card card, TablePile table) throws InvalidCardPlayException;

    /**
     * Draws a card from the given deck and adds it to the player's hand.
     *
     * @param deck the deck to draw from.
     * @throws EmptyDeckException if the deck has no cards left.
     */
    void drawCard(Deck deck) throws EmptyDeckException;
}