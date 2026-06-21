package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.PlayerType;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Player controlled by the computer (AI). Unlike {@link HumanPlayer}, this
 * class autonomously decides which card to play through
 * {@link #selectBestCard(int)}, applying a heuristic strategy that varies
 * depending on how close the table sum is to the {@link GameConstants#MAX_SUM} limit.
 *
 * The strategy is divided into three phases:
 *
 * Aggressive (sum less than or equal to 20): prioritizes high-value cards
 * to pressure the other players, with a random component to remain unpredictable.
 *
 * Tactical (21 to 35): tries to leave the sum in a pressure range (38-45)
 * without risking exceeding the limit.
 *
 * Defensive (above 35): prioritizes neutral or negative cards (9, J, Q, K)
 * to minimize the risk of running out of valid plays.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class MachinePlayer extends Player {

    /**
     * Creates an AI player with type {@link PlayerType#MACHINE}.
     *
     * @param name the player's display name (e.g. {@code "IA 1"}).
     */
    public MachinePlayer(String name) {
        super(name, PlayerType.MACHINE);
    }


    /**
     * Plays a card whose value is calculated automatically from its
     * {@link com.example.miniproyecto3.model.enums.Rank}. The concrete card
     * to play normally comes from {@link #selectBestCard(int)}.
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
     * Selects, among the valid cards in hand, the most suitable one
     * according to the current phase of the match (aggressive, tactical, or
     * defensive), based on the current table sum.
     *
     * @param tableSum the current accumulated sum on the table pile.
     * @return the card chosen by the AI, or {@code null} if no card in hand
     *         can be played without exceeding {@link GameConstants#MAX_SUM}
     *         (in which case the player must be eliminated).
     */
    public Card selectBestCard(int tableSum) {
        List<Card> validCards = new ArrayList<>();

        for (Card card : getHand()) {
            int value = card.getValue(tableSum);
            int newSum = tableSum + value;
            if (newSum <= GameConstants.MAX_SUM) {
                validCards.add(card);
            }
        }

        if (validCards.isEmpty()) return null;

        Random random = new Random();
        int remaining = GameConstants.MAX_SUM - tableSum;

        // Aggressive phase: low sum, play high to pressure
        if (tableSum <= 20) {
            // 70% aggressive, 30% random
            if (random.nextInt(100) < 70) {
                return validCards.stream()
                        .max(Comparator.comparingInt(c -> c.getValue(tableSum)))
                        .orElse(validCards.get(0));
            }
            return validCards.get(random.nextInt(validCards.size()));
        }

        // Tactical phase: mid sum, mixed strategy
        if (tableSum <= 35) {
            // Prioritize cards that leave the total between 40-45 to pressure without risk
            List<Card> tacticalCards = new ArrayList<>();
            for (Card card : validCards) {
                int newSum = tableSum + card.getValue(tableSum);
                if (newSum >= 38 && newSum <= 45) {
                    tacticalCards.add(card);
                }
            }
            if (!tacticalCards.isEmpty()) {
                return tacticalCards.get(random.nextInt(tacticalCards.size()));
            }
            // If there is no perfect tactical option, 50% aggressive 50% conservative
            if (random.nextBoolean()) {
                return validCards.stream()
                        .max(Comparator.comparingInt(c -> c.getValue(tableSum)))
                        .orElse(validCards.get(0));
            }
            return validCards.stream()
                    .min(Comparator.comparingInt(c -> c.getValue(tableSum)))
                    .orElse(validCards.get(0));
        }

        // Defensive phase: high sum, prioritize neutral and negative cards
        if (tableSum > 35) {
            // First look for cards that subtract or are neutral
            List<Card> safeCards = new ArrayList<>();
            for (Card card : validCards) {
                int value = card.getValue(tableSum);
                if (value <= 0) safeCards.add(card);
            }
            if (!safeCards.isEmpty()) {
                return safeCards.get(random.nextInt(safeCards.size()));
            }

            // If there are no safe cards, play the one that adds the least,
            // but with a 20% chance play a random one to remain unpredictable
            if (random.nextInt(100) < 20) {
                return validCards.get(random.nextInt(validCards.size()));
            }
            return validCards.stream()
                    .min(Comparator.comparingInt(c -> c.getValue(tableSum)))
                    .orElse(validCards.get(0));
        }

        return validCards.get(random.nextInt(validCards.size()));
    }
}