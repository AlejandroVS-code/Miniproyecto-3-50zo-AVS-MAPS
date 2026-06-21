package com.example.miniproyecto3.model.exceptions;

/**
 * Thrown when a card is requested from a deck that has no cards left to draw.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class EmptyDeckException extends RuntimeException {

    /**
     * @param message description of the operation that failed because the
     *                deck was empty.
     */
    public EmptyDeckException(String message) {
        super(message);
    }
}