package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.PlayerType;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;
import com.example.miniproyecto3.model.interfaces.IEliminable;
import com.example.miniproyecto3.model.interfaces.IPlayable;
import com.example.miniproyecto3.model.GameConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a player in a Cincuentazo match.
 * Encapsulates the state common to any type of player (name, type, hand of
 * cards, accumulated score, move count, and elimination status), leaving it
 * to the concrete subclasses ({@link HumanPlayer} and {@link MachinePlayer})
 * to decide how each one selects and executes its play.
 *
 * Implements {@link IPlayable} (playing and drawing cards) and
 * {@link IEliminable} (elimination state management), which allows the rest
 * of the system ({@link GameModel}, the JavaFX controllers) to work
 * polymorphically on {@code Player} references without knowing the concrete
 * subclass.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public abstract class Player implements IPlayable, IEliminable {
    private int totalPoints;
    private final String name;
    private final PlayerType playerType;
    private final ArrayList<Card> hand;
    private boolean eliminated;
    private int totalMoves;

    /**
     * Increments by one the count of moves (plays) made by this player
     * during the match. Used for the final statistics shown on the results
     * screen.
     */
    public void addMove() {
        totalMoves++;
    }

    /**
     * @return the total number of cards this player has played throughout
     *         the match.
     */
    public int getTotalMoves() {
        return totalMoves;
    }

    /**
     * Creates a new player with an empty hand and not eliminated.
     *
     * @param name       the player's display name (e.g. {@code "Tu"} or {@code "IA 1"}).
     * @param playerType the player's type ({@link PlayerType#HUMAN} or {@link PlayerType#MACHINE}),
     *                   used to distinguish behavior without resorting to {@code instanceof}.
     */
    public Player(String name, PlayerType playerType) {
        this.name = name;
        this.playerType = playerType;
        this.hand = new ArrayList<>();
        this.eliminated = false;
    }

    /**
     * Adds points to the player's accumulated score. Invoked every time the
     * player plays a card, adding the absolute value of that card.
     *
     * @param points the amount of points to add (non-negative).
     */
    public void addPoints(int points) {
        totalPoints += points;
    }

    /**
     * @return the total score accumulated by the player during the match.
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     * Determines whether the player has at least one card in hand whose
     * value, added to the current table total, would not exceed the maximum
     * allowed limit ({@link GameConstants#MAX_SUM}). Used to decide whether
     * the player must be eliminated on their turn.
     *
     * @param tableSum the current accumulated sum on the table pile.
     * @return {@code true} if at least one valid play exists; {@code false}
     *         if every card in hand would exceed the limit.
     */
    public boolean hasValidPlay(int tableSum) {
        for (Card card : hand) {
            if (tableSum + card.getValue(tableSum) <= GameConstants.MAX_SUM) {
                return true;
            }
        }
        return false;
    }

    /**
     * Plays a card from the player's hand onto the table pile. The concrete
     * behavior (validations, error handling) is defined by each subclass.
     *
     * @param card  the card to play; must belong to the player's hand.
     * @param table the table pile onto which the sum is accumulated.
     * @throws InvalidCardPlayException if the play would cause the table sum
     *         to exceed {@link GameConstants#MAX_SUM}.
     */
    @Override
    public abstract void playCard(Card card, TablePile table) throws InvalidCardPlayException;

    /**
     * Draws a card from the deck and adds it to the player's hand.
     *
     * @param deck the deck to draw from.
     * @throws EmptyDeckException if the deck has no cards available.
     */
    @Override
    public void drawCard(Deck deck) throws EmptyDeckException {
        hand.add(deck.draw());
    }

    /**
     * @return {@code true} if the player has already been eliminated from the match.
     */
    @Override
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Marks the player as eliminated. Does not clear the hand by itself;
     * card cleanup is performed by {@link GameModel#eliminateCurrentPlayer()}.
     */
    @Override
    public void eliminate() {
        this.eliminated = true;
    }

    /**
     * @return the mutable list of cards the player currently holds in hand.
     */
    @Override
    public List<Card> getHand() {
        return hand;
    }

    /**
     * @return the player's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return this player's {@link PlayerType} (human or machine).
     */
    public PlayerType getPlayerType() {
        return playerType;
    }
}