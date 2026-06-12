package com.example.miniproyecto3.thread;

import com.example.miniproyecto3.model.GameConstants;
import com.example.miniproyecto3.model.GameModel;
import com.example.miniproyecto3.model.Player;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import javafx.application.Platform;

import java.util.Random;

public class DrawCardThread extends Thread {

    private final GameModel gameModel;
    private final Player player;
    private final Runnable onDrawComplete;
    private final Random random;

    public DrawCardThread(GameModel gameModel, Player player, Runnable onDrawComplete) {
        this.gameModel = gameModel;
        this.player = player;
        this.onDrawComplete = onDrawComplete;
        this.random = new Random();
    }

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
