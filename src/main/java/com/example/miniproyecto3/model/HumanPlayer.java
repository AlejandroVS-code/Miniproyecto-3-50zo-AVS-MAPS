package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.PlayerType;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;
import com.example.miniproyecto3.model.GameConstants;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name, PlayerType.HUMAN);
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
    public void playCardWithValue(Card card, TablePile table, int forcedValue) {
        if (table.getCurrentSum() + forcedValue > GameConstants.MAX_SUM) {
            throw new InvalidCardPlayException(
                    "Invalid play: would exceed 50.",
                    forcedValue,
                    table.getCurrentSum()
            );
        }
        addPoints(Math.abs(forcedValue));
        getHand().remove(card);
        table.addCardWithValue(card, forcedValue);
    }
}
