package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.PlayerType;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;
import com.example.miniproyecto3.model.GameConstants;

/**
 * Represents the player controlled by the person in front of the computer.
 *
 * Unlike {@link MachinePlayer}, this class does not decide by itself
 * which card to play: the card selection and, in the case of the Ace, the
 * value to use, is made from the graphical interface (GameController)
 * and arrives here already resolved.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class HumanPlayer extends Player {


    /**
     * Creates a human player with type {@link PlayerType#HUMAN}.
     *
     * @param name the player's display name (by convention, {@code "Tu"}).
     */
    public HumanPlayer(String name) {
        super(name, PlayerType.HUMAN);
    }

    /**
     * Plays a card whose value is calculated automatically from its
     * {@link com.example.miniproyecto3.model.enums.Rank}. Used for every
     * card except the Ace, whose ambiguous value (10 or 1) requires the
     * player to choose it explicitly via {@link #playCardWithValue}.
     *
     * @param card  the card to play; must be in the player's hand.
     * @param table the table pile onto which the sum is accumulated.
     * @throws InvalidCardPlayException if the resulting sum would exceed
     *         {@link GameConstants#MAX_SUM}.
     */
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

    /**
     * Plays a card forcing a specific value, instead of calculating it
     * automatically from its {@link com.example.miniproyecto3.model.enums.Rank}.
     * Used exclusively for the Ace, whose value (10 or 1) is chosen by the
     * player through a dialog in the graphical interface.
     *
     * @param card        the card to play (normally an Ace).
     * @param table       the table pile onto which the sum is accumulated.
     * @param forcedValue the value the player chose for the card.
     * @throws InvalidCardPlayException if the resulting sum would exceed
     *         {@link GameConstants#MAX_SUM}.
     */
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
