package com.example.miniproyecto3.model.exceptions;

/**
 * Thrown when a player attempts to play a card that would cause the table
 * sum to exceed the maximum allowed value. Carries the offending card's
 * value and the table sum at the moment of the failed attempt, so callers
 * can build a precise error message or decide how to react (e.g. trigger
 * elimination logic).
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class InvalidCardPlayException extends RuntimeException {

    private final int cardValue;
    private final int currentSum;

    /**
     * @param message    description of why the play is invalid.
     * @param cardValue  the value of the card that was attempted to be played.
     * @param currentSum the table sum at the time of the attempt.
     */
    public InvalidCardPlayException(String message, int cardValue, int currentSum) {
        super(message);
        this.cardValue = cardValue;
        this.currentSum = currentSum;
    }

    /**
     * @return the value of the card that caused this exception.
     */
    public int getCardValue() { return cardValue; }

    /**
     * @return the table sum at the moment this exception was thrown.
     */
    public int getCurrentSum() { return currentSum; }
}