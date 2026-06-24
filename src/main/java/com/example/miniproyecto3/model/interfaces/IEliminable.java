package com.example.miniproyecto3.model.interfaces;

import com.example.miniproyecto3.model.Card;
import java.util.List;

/**
 * Contract for any entity that can be eliminated from the match and that
 * exposes the hand of cards it was holding at the time. Implemented by
 * {@code Player} so {@code GameModel} can manage elimination and card
 * recycling without depending on the concrete player subclass.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public interface IEliminable {

    /**
     * @return {@code true} if this entity has already been eliminated from
     *         the match.
     */
    boolean isEliminated();

    /**
     * Marks this entity as eliminated from the match.
     */
    void eliminate();

    /**
     * @return the mutable list of cards currently held in hand.
     */
    List<Card> getHand();
}

