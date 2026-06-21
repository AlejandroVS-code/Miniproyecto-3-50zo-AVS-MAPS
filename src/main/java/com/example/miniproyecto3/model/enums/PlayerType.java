package com.example.miniproyecto3.model.enums;

/**
 * Identifies whether a Player is controlled by a human or by the
 * computer. Used instead of instanceof checks to determine player
 * behavior across the model and the controllers (for example, to decide
 * whether to show "VICTORIA" or "FALLASTE" on the results screen).
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public enum PlayerType {

    /** A player controlled by the person using the application. */
    HUMAN,

    /** A player controlled by the AI strategy. */
    MACHINE;
}
