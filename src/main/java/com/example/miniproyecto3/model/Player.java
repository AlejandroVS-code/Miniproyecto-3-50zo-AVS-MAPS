package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.PlayerType;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;
import com.example.miniproyecto3.model.interfaces.IEliminable;
import com.example.miniproyecto3.model.interfaces.IPlayable;
import com.example.miniproyecto3.model.GameConstants;

import java.util.ArrayList;
import java.util.List;

public abstract class Player implements IPlayable, IEliminable {
    private int totalPoints;
    private final String name;
    private final PlayerType playerType;
    private final ArrayList<Card> hand;
    private boolean eliminated;
    private int totalMoves;

    public void addMove() {
        totalMoves++;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public Player(String name, PlayerType playerType) {
        this.name = name;
        this.playerType = playerType;
        this.hand = new ArrayList<>();
        this.eliminated = false;
    }
    public void addPoints(int points) {
        totalPoints += points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public boolean hasValidPlay(int tableSum) {
        for (Card card : hand) {
            if (tableSum + card.getValue(tableSum) <= GameConstants.MAX_SUM) {
                return true;
            }
        }
        return false;
    }

    @Override
    public abstract void playCard(Card card, TablePile table) throws InvalidCardPlayException;

    @Override
    public void drawCard(Deck deck) throws EmptyDeckException {
        hand.add(deck.draw());
    }

    @Override
    public boolean isEliminated() {
        return eliminated;
    }

    @Override
    public void eliminate() {
        this.eliminated = true;
    }

    @Override
    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }
}