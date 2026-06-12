package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.GameStateException;

import java.util.LinkedList;
import java.util.List;

public class GameModel {

    private final LinkedList<Player> players;
    private final Deck deck;
    private final TablePile tablePile;
    private int currentPlayerIndex;
    private boolean gameOver;

    public GameModel() {
        players = new LinkedList<>();
        deck = new Deck();
        tablePile = new TablePile();
        currentPlayerIndex = 0;
        gameOver = false;
    }

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

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() throws GameStateException {
        if (gameOver) {
            throw new GameStateException("Cannot advance turn, the game is already over.");
        }
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isEliminated());
    }

    public void eliminateCurrentPlayer() throws EmptyDeckException {
        Player eliminated = getCurrentPlayer();
        eliminated.eliminate();
        List<Card> hand = eliminated.getHand();
        deck.recycle(hand);
        hand.clear();
    }

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

    public boolean isGameOver() {
        return gameOver;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public TablePile getTablePile() {
        return tablePile;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
