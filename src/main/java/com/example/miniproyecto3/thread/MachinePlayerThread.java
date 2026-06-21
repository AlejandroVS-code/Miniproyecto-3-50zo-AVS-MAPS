package com.example.miniproyecto3.thread;

import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.GameConstants;
import com.example.miniproyecto3.model.GameModel;
import com.example.miniproyecto3.model.MachinePlayer;
import javafx.application.Platform;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Background thread that resolves a single AI player's turn without
 * blocking the JavaFX application thread.
 *
 * Waits a randomized delay (for pacing/realism), asks the current
 * {@link MachinePlayer} to choose its best card, plays it, and then hands
 * control back to the JavaFX thread via {@link Platform#runLater(Runnable)}
 * so the UI can be updated safely. If the AI has no valid card to play, the
 * current player is eliminated instead.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class MachinePlayerThread extends Thread {

    private final GameModel gameModel;
    private final Consumer<Integer> onTurnComplete;
    private final Random random;

    /**
     * @param gameModel      the game model to operate on.
     * @param onTurnComplete callback invoked on the JavaFX thread once the
     *                       turn is resolved, receiving the index of the
     *                       card that was played within the AI's hand, or
     *                       {@code -1} if the player was eliminated or an
     *                       error occurred.
     */
    public MachinePlayerThread(GameModel gameModel, Consumer<Integer> onTurnComplete) {
        this.gameModel = gameModel;
        this.onTurnComplete = onTurnComplete;
        this.random = new Random();
    }

    /**
     * Executes the AI turn: sleeps for a randomized delay, selects and plays
     * a card (or triggers elimination if no valid play exists), and
     * notifies the completion callback on the JavaFX thread.
     */
    @Override
    public void run() {
        try {
            int delay = GameConstants.MACHINE_PLAY_MIN_DELAY +
                    random.nextInt(GameConstants.MACHINE_PLAY_MAX_DELAY -
                            GameConstants.MACHINE_PLAY_MIN_DELAY);
            Thread.sleep(delay);

            MachinePlayer machine = (MachinePlayer) gameModel.getCurrentPlayer();
            Card cardToPlay = machine.selectBestCard(gameModel.getTablePile().getCurrentSum());

            if (cardToPlay != null) {
                int cardIndex = machine.getHand().indexOf(cardToPlay);
                machine.playCard(cardToPlay, gameModel.getTablePile());
                Platform.runLater(() -> onTurnComplete.accept(cardIndex));
            } else {
                gameModel.eliminateCurrentPlayer();
                Platform.runLater(() -> onTurnComplete.accept(-1));
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            Platform.runLater(() -> onTurnComplete.accept(-1));
        }
    }
}