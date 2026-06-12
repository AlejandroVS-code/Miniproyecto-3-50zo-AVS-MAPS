package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.PlayerType;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;

public class MachinePlayer extends Player {

    public MachinePlayer(String name) {
        super(name, PlayerType.MACHINE);
    }

    @Override
    public void playCard(Card card, TablePile table) throws InvalidCardPlayException {
        int cardValue = card.getValue(table.getCurrentSum());
        if (table.getCurrentSum() + cardValue > GameConstants.MAX_SUM) {
            throw new InvalidCardPlayException(
                    "Invalid play: card value " + cardValue +
                            " would exceed 50. Current sum: " + table.getCurrentSum(),
                    cardValue,
                    table.getCurrentSum()
            );
        }
        getHand().remove(card);
        addPoints(Math.abs(cardValue));
        table.addCard(card);
    }

    public Card selectBestCard(int tableSum) {
        Card bestCard = null;

        if (tableSum <= 25) {
            int highestValue = Integer.MIN_VALUE;
            for (Card card : getHand()) {
                int value = card.getValue(tableSum);
                int newSum = tableSum + value;
                if (newSum <= GameConstants.MAX_SUM && value > highestValue) {
                    highestValue = value;
                    bestCard = card;
                }
            }
        } else if (tableSum <= 40) {
            int lowestValue = Integer.MAX_VALUE;
            for (Card card : getHand()) {
                int value = card.getValue(tableSum);
                int newSum = tableSum + value;
                if (newSum <= GameConstants.MAX_SUM && value < lowestValue) {
                    lowestValue = value;
                    bestCard = card;
                }
            }
        } else {
            for (Card card : getHand()) {
                int value = card.getValue(tableSum);
                int newSum = tableSum + value;
                if (newSum <= GameConstants.MAX_SUM && value <= 0) {
                    bestCard = card;
                    break;
                }
            }
            if (bestCard == null) {
                int lowestValue = Integer.MAX_VALUE;
                for (Card card : getHand()) {
                    int value = card.getValue(tableSum);
                    int newSum = tableSum + value;
                    if (newSum <= GameConstants.MAX_SUM && value < lowestValue) {
                        lowestValue = value;
                        bestCard = card;
                    }
                }
            }
        }
        return bestCard;
    }
}