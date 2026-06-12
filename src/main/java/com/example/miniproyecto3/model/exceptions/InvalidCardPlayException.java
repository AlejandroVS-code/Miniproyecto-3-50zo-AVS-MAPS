package com.example.miniproyecto3.model.exceptions;

public class InvalidCardPlayException extends RuntimeException {

    private final int cardValue;
    private final int currentSum;

    public InvalidCardPlayException(String message, int cardValue, int currentSum) {
        super(message);
        this.cardValue = cardValue;
        this.currentSum = currentSum;
    }

    public int getCardValue() { return cardValue; }
    public int getCurrentSum() { return currentSum; }
}