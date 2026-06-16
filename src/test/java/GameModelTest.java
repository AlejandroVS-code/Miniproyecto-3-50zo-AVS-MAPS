import com.example.miniproyecto3.model.*;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.GameStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    private GameModel gameModel;

    @BeforeEach
    public void setUp() throws EmptyDeckException {
        gameModel = new GameModel();
        gameModel.initGame(2);
    }

    @Test
    public void testInitGameCreatesCorrectNumberOfPlayers() {
        assertEquals(3, gameModel.getPlayers().size());
    }

    @Test
    public void testInitGameHumanPlayerIsFirst() {
        assertTrue(gameModel.getPlayers().get(0) instanceof HumanPlayer);
    }

    @Test
    public void testInitGameMachinePlayersAreCorrect() {
        assertTrue(gameModel.getPlayers().get(1) instanceof MachinePlayer);
        assertTrue(gameModel.getPlayers().get(2) instanceof MachinePlayer);
    }

    @Test
    public void testInitGameEachPlayerHasFourCards() {
        for (Player player : gameModel.getPlayers()) {
            assertEquals(GameConstants.HAND_SIZE, player.getHand().size());
        }
    }

    @Test
    public void testInitGameTablePileHasOneCard() {
        assertNotNull(gameModel.getTablePile().getTopCard());
    }

    @Test
    public void testInitGameDeckHasCorrectSize() {
        int expectedSize = 52 - (3 * GameConstants.HAND_SIZE) - 1;
        assertEquals(expectedSize, gameModel.getDeck().size());
    }

    @Test
    public void testNextTurnAdvancesPlayer() throws GameStateException {
        int initialIndex = gameModel.getCurrentPlayerIndex();
        gameModel.nextTurn();
        assertNotEquals(initialIndex, gameModel.getCurrentPlayerIndex());
    }

    @Test
    public void testNextTurnSkipsEliminatedPlayer() throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        int eliminatedIndex = gameModel.getCurrentPlayerIndex();
        gameModel.nextTurn();
        assertNotEquals(eliminatedIndex, gameModel.getCurrentPlayerIndex());
    }

    @Test
    public void testEliminatePlayerSendsCardsToDeck() throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        int deckSizeBefore = gameModel.getDeck().size();
        int handSize = gameModel.getCurrentPlayer().getHand().size();
        gameModel.eliminateCurrentPlayer();
        assertEquals(deckSizeBefore + handSize, gameModel.getDeck().size());
    }

    @Test
    public void testEliminatedPlayerHasNoCards() throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        Player toEliminate = gameModel.getCurrentPlayer();
        gameModel.eliminateCurrentPlayer();
        assertTrue(toEliminate.getHand().isEmpty());
    }

    @Test
    public void testCheckWinnerReturnsNullWhenMultiplePlayers() {
        assertNull(gameModel.checkWinner());
    }

    @Test
    public void testCheckWinnerReturnsWinnerWhenOnePlayerLeft()
            throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        assertNotNull(gameModel.checkWinner());
    }

    @Test
    public void testIsGameOverFalseAtStart() {
        assertFalse(gameModel.isGameOver());
    }

    @Test
    public void testIsGameOverTrueAfterWinner()
            throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        gameModel.checkWinner();
        assertTrue(gameModel.isGameOver());
    }

    @Test
    public void testNextTurnThrowsWhenGameOver()
            throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        gameModel.checkWinner();
        assertThrows(GameStateException.class, () -> gameModel.nextTurn());
    }
}