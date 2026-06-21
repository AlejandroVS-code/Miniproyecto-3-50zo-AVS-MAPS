package com.example.miniproyecto3.model.exceptions;

/**
 * Thrown when an operation is attempted that is not valid for the current
 * state of the match, such as advancing the turn after the game has already
 * ended.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class GameStateException extends RuntimeException {

    /**
     * @param message description of the invalid state transition that was attempted.
     */
    public GameStateException(String message) {
        super(message);
    }
}
