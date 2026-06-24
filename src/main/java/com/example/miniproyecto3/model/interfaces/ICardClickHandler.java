package com.example.miniproyecto3.model.interfaces;

import com.example.miniproyecto3.model.Card;

/**
 * Functional contract for handling click events on a card displayed in the
 * UI. Used by the game screen controller to decouple the card-selection
 * logic from the JavaFX event-handling boilerplate.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
@FunctionalInterface
public interface ICardClickHandler {

    /**
     * Invoked when the player clicks on a card.
     *
     * @param card  the card that was clicked.
     * @param index the position of that card within the player's hand.
     */
    void onCardClicked(Card card, int index);
}