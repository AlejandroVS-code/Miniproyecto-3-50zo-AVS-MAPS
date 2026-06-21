package com.example.miniproyecto3.thread;

import com.example.miniproyecto3.model.GameConstants;
import com.example.miniproyecto3.model.GameModel;
import com.example.miniproyecto3.model.Player;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import javafx.application.Platform;

import java.util.Random;

/**
 * Background thread that draws a replacement card for a player after they
 * play one, without blocking the JavaFX application thread.
 *
 * Waits a short randomized delay (for pacing), recycles the table pile's
 * non-top cards back into the deck if it has run out, draws a card for the
 * given player, and then hands control back to the JavaFX thread via
 * {@link Platform#runLater(Runnable)} so the UI can be updated safely.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class DrawCardThread extends Thread {

    private final GameModel gameModel;
    private final Player player;
    private final Runnable onDrawComplete;
    private final Random random;

    /**
     * @param gameModel      the game model to operate on.
     * @param player         the player who needs to draw a card.
     * @param onDrawComplete callback invoked on the JavaFX thread once the
     *                       draw completes (or fails because the deck is empty).
     */
    public DrawCardThread(GameModel gameModel, Player player, Runnable onDrawComplete) {
        this.gameModel = gameModel;
        this.player = player;
        this.onDrawComplete = onDrawComplete;
        this.random = new Random();
    }

    /**
     * Executes the card draw: sleeps for a randomized delay, recycles the
     * deck if needed, draws a card for the player, and notifies the
     * completion callback on the JavaFX thread.
     */
    @Override
    public void run() {
        try {
            int delay = GameConstants.MACHINE_DRAW_MIN_DELAY +
                    random.nextInt(GameConstants.MACHINE_DRAW_MAX_DELAY -
                            GameConstants.MACHINE_DRAW_MIN_DELAY);
            Thread.sleep(delay);

            if (gameModel.getDeck().isEmpty()) {
                gameModel.getDeck().recycle(
                        gameModel.getTablePile().collectForRecycle()
                );
            }

            player.drawCard(gameModel.getDeck());
            Platform.runLater(onDrawComplete);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (EmptyDeckException e) {
            Platform.runLater(onDrawComplete);
        }
    }
}