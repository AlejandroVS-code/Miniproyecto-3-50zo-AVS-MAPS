package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.enums.PlayerType;
import com.example.miniproyecto3.model.exceptions.InvalidCardPlayException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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

        // Fase agresiva: suma baja, juega alto para presionar
        if (tableSum <= 20) {
            // 70% agresivo, 30% aleatorio
            if (random.nextInt(100) < 70) {
                return validCards.stream()
                        .max(Comparator.comparingInt(c -> c.getValue(tableSum)))
                        .orElse(validCards.get(0));
            }
            return validCards.get(random.nextInt(validCards.size()));
        }

        // Fase táctica: suma media, mezcla estrategia
        if (tableSum <= 35) {
            // Prioriza cartas que dejen el total entre 40-45 para presionar sin arriesgar
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
            // Si no hay táctica perfecta, 50% agresivo 50% conservador
            if (random.nextBoolean()) {
                return validCards.stream()
                        .max(Comparator.comparingInt(c -> c.getValue(tableSum)))
                        .orElse(validCards.get(0));
            }
            return validCards.stream()
                    .min(Comparator.comparingInt(c -> c.getValue(tableSum)))
                    .orElse(validCards.get(0));
        }

        // Fase defensiva: suma alta, prioriza neutrales y negativos
        if (tableSum > 35) {
            // Primero busca cartas que resten o sean neutras
            List<Card> safeCards = new ArrayList<>();
            for (Card card : validCards) {
                int value = card.getValue(tableSum);
                if (value <= 0) safeCards.add(card);
            }
            if (!safeCards.isEmpty()) {
                return safeCards.get(random.nextInt(safeCards.size()));
            }

            // Si no hay seguras, juega la que menos sume
            // pero con 20% de probabilidad juega aleatoria para ser impredecible
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