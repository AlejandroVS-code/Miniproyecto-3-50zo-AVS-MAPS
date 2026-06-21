package com.example.miniproyecto3.model;

/**
 * Centralizes the configuration constants used throughout the game, so that
 * game-balance values are not scattered as magic numbers across the
 * codebase. Cannot be instantiated.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class GameConstants {

    /** Maximum value the table sum may reach before a player is eliminated. */
    public static final int MAX_SUM = 50;

    /** Number of cards each player holds in hand at any given time. */
    public static final int HAND_SIZE = 4;

    /** Minimum number of AI opponents allowed in a match. */
    public static final int MIN_MACHINES = 1;

    /** Maximum number of AI opponents allowed in a match. */
    public static final int MAX_MACHINES = 3;

    /** Minimum artificial delay (ms) before an AI plays a card, for pacing. */
    public static final int MACHINE_PLAY_MIN_DELAY = 1500;

    /** Maximum artificial delay (ms) before an AI plays a card, for pacing. */
    public static final int MACHINE_PLAY_MAX_DELAY = 3000;

    /** Minimum artificial delay (ms) before an AI draws a card, for pacing. */
    public static final int MACHINE_DRAW_MIN_DELAY = 500;

    /** Maximum artificial delay (ms) before an AI draws a card, for pacing. */
    public static final int MACHINE_DRAW_MAX_DELAY = 1000;

    /** Private constructor to prevent instantiation of this constants holder. */
    private GameConstants() {}
}