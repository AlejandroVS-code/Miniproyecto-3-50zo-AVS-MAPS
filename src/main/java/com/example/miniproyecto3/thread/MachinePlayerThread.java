package com.example.miniproyecto3.thread;

import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.GameConstants;
import com.example.miniproyecto3.model.GameModel;
import com.example.miniproyecto3.model.MachinePlayer;
import javafx.application.Platform;

import java.util.Random;
import java.util.function.Consumer;

public class MachinePlayerThread extends Thread {

    private final GameModel gameModel;
    private final Consumer<Integer> onTurnComplete;
    private final Random random;

    public MachinePlayerThread(GameModel gameModel, Consumer<Integer> onTurnComplete) {
        this.gameModel = gameModel;
        this.onTurnComplete = onTurnComplete;
        this.random = new Random();
    }

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