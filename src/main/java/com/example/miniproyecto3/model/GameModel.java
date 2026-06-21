package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.GameStateException;

import java.util.LinkedList;
import java.util.List;

/**
 * Orchestrates a single Cincuentazo match: holds the list of players, the
 * deck, the table pile, and the turn order, and exposes the operations
 * needed to drive the game loop (advancing turns, eliminating players,
 * detecting the winner).
 *
 * This class is intentionally independent of JavaFX: it contains no
 * references to the UI layer, so it can be unit tested in isolation and
 * reused regardless of how the game is rendered. The controllers
 * (GameController, etc.) are the ones responsible for translating
 * this model's state into what is shown on screen.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class GameModel {

    private final LinkedList<Player> players;
    private final Deck deck;
    private final TablePile tablePile;
    private int currentPlayerIndex;
    private boolean gameOver;

    /**
     * Creates a new game model with an empty player list, a fresh shuffled
     * deck, and an empty table pile. {@link #initGame(int)} must be called
     * afterward to actually start a match.
     */
    public GameModel() {
        players = new LinkedList<>();
        deck = new Deck();
        tablePile = new TablePile();
        currentPlayerIndex = 0;
        gameOver = false;
    }

    /**
     * Sets up a new match: creates the human player plus the requested
     * number of AI players, deals {@link GameConstants#HAND_SIZE} cards to
     * each one, and reveals the first card on the table pile to start the
     * running sum.
     *
     * @param machineCount number of AI opponents to create
     *                     (between {@link GameConstants#MIN_MACHINES} and
     *                     {@link GameConstants#MAX_MACHINES}).
     * @throws EmptyDeckException if the deck runs out of cards while dealing
     *         the initial hands (should not happen with a fresh 52-card deck).
     */
    public void initGame(int machineCount) throws EmptyDeckException {
        players.clear();
        players.add(new HumanPlayer("Tu"));
        for (int i = 1; i <= machineCount; i++) {
            players.add(new MachinePlayer("IA " + i));
        }
        for (Player player : players) {
            for (int i = 0; i < GameConstants.HAND_SIZE; i++) {
                player.drawCard(deck);
            }
        }
        Card initialCard = deck.draw();
        tablePile.addCard(initialCard);
    }

    /**
     * @return the player whose turn is currently active.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Advances {@link #currentPlayerIndex} to the next player in turn order,
     * skipping any player who has already been eliminated.
     *
     * @throws GameStateException if the game has already ended.
     */
    public void nextTurn() throws GameStateException {
        if (gameOver) {
            throw new GameStateException("Cannot advance turn, the game is already over.");
        }
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isEliminated());
    }

    /**
     * Eliminates the player whose turn is currently active: marks them as
     * eliminated, and returns their remaining hand back into the deck so
     * those cards can be drawn again.
     *
     * @throws EmptyDeckException propagated if recycling the cards fails
     *         (not expected under normal conditions).
     */
    public void eliminateCurrentPlayer() throws EmptyDeckException {
        Player eliminated = getCurrentPlayer();
        eliminated.eliminate();
        List<Card> hand = eliminated.getHand();
        deck.recycle(hand);
        hand.clear();
    }

    /**
     * Checks whether the match has reached a winning condition, i.e. only
     * one non-eliminated player remains. If so, marks the game as over.
     *
     * @return the winning player if exactly one active player remains;
     *         {@code null} if the match should continue.
     */
    public Player checkWinner() {
        Player lastPlayer = null;
        int activePlayers = 0;
        for (Player player : players) {
            if (!player.isEliminated()) {
                activePlayers++;
                lastPlayer = player;
            }
        }
        if (activePlayers == 1) {
            gameOver = true;
            return lastPlayer;
        }
        return null;
    }

    /**
     * @return {@code true} if the match has already ended (a winner was determined).
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @return the list of all players participating in the match, in turn order.
     */
    public LinkedList<Player> getPlayers() {
        return players;
    }

    /**
     * @return the deck used to draw and recycle cards during the match.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @return the table pile holding the current running sum and played cards.
     */
    public TablePile getTablePile() {
        return tablePile;
    }

    /**
     * @return the index, within {@link #getPlayers()}, of the player whose
     *         turn is currently active.
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
