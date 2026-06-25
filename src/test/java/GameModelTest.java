import com.example.miniproyecto3.model.*;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.GameStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link GameModel} class.
 *
 * <p>Validates game initialization, turn management, player elimination,
 * winner detection, and the game-over state, ensuring the core game logic
 * behaves correctly under all expected conditions.</p>
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class GameModelTest {

    private GameModel gameModel;
    /**
     * Initializes a fresh {@link GameModel} with 2 AI opponents before
     * each test to ensure full test isolation.
     *
     * @throws EmptyDeckException if the deck cannot be initialized.
     */
    @BeforeEach
    public void setUp() throws EmptyDeckException {
        gameModel = new GameModel();
        gameModel.initGame(2);
    }
    /**
     * Verifies that initializing with 2 AI opponents creates exactly
     * 3 players (1 human + 2 machines).
     */
    @Test
    public void testInitGameCreatesCorrectNumberOfPlayers() {
        assertEquals(3, gameModel.getPlayers().size());
    }
    /**
     * Verifies that the first player in the list is always
     * a {@link HumanPlayer}.
     */
    @Test
    public void testInitGameHumanPlayerIsFirst() {
        assertTrue(gameModel.getPlayers().get(0) instanceof HumanPlayer);
    }
    /**
     * Verifies that players at indices 1 and 2 are {@link MachinePlayer}
     * instances after initialization with 2 AI opponents.
     */
    @Test
    public void testInitGameMachinePlayersAreCorrect() {
        assertTrue(gameModel.getPlayers().get(1) instanceof MachinePlayer);
        assertTrue(gameModel.getPlayers().get(2) instanceof MachinePlayer);
    }
    /**
     * Verifies that every player starts with exactly {@code HAND_SIZE}
     * cards in their hand after initialization.
     */
    @Test
    public void testInitGameEachPlayerHasFourCards() {
        for (Player player : gameModel.getPlayers()) {
            assertEquals(GameConstants.HAND_SIZE, player.getHand().size());
        }
    }
    /**
     * Verifies that the table pile contains an initial card after
     * game initialization.
     */
    @Test
    public void testInitGameTablePileHasOneCard() {
        assertNotNull(gameModel.getTablePile().getTopCard());
    }
    /**
     * Verifies that the deck size after initialization equals 52 minus
     * the cards dealt to players and the initial table card.
     */
    @Test
    public void testInitGameDeckHasCorrectSize() {
        int expectedSize = 52 - (3 * GameConstants.HAND_SIZE) - 1;
        assertEquals(expectedSize, gameModel.getDeck().size());
    }
    /**
     * Verifies that calling {@code nextTurn()} advances the current
     * player index to a different player.
     *
     * @throws GameStateException if the game is already over.
     */
    @Test
    public void testNextTurnAdvancesPlayer() throws GameStateException {
        int initialIndex = gameModel.getCurrentPlayerIndex();
        gameModel.nextTurn();
        assertNotEquals(initialIndex, gameModel.getCurrentPlayerIndex());
    }
    /**
     * Verifies that {@code nextTurn()} skips eliminated players,
     * never landing on a player who has been eliminated.
     *
     * @throws EmptyDeckException if the deck runs out unexpectedly.
     * @throws GameStateException if the game is already over.
     */
    @Test
    public void testNextTurnSkipsEliminatedPlayer() throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        int eliminatedIndex = gameModel.getCurrentPlayerIndex();
        gameModel.nextTurn();
        assertNotEquals(eliminatedIndex, gameModel.getCurrentPlayerIndex());
    }
    /**
     * Verifies that eliminating a player sends all their cards back to
     * the deck, increasing the deck size by exactly the player's hand size.
     *
     * @throws EmptyDeckException if the deck runs out unexpectedly.
     * @throws GameStateException if the game is already over.
     */
    @Test
    public void testEliminatePlayerSendsCardsToDeck() throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        int deckSizeBefore = gameModel.getDeck().size();
        int handSize = gameModel.getCurrentPlayer().getHand().size();
        gameModel.eliminateCurrentPlayer();
        assertEquals(deckSizeBefore + handSize, gameModel.getDeck().size());
    }
    /**
     * Verifies that an eliminated player's hand is empty after elimination.
     *
     * @throws EmptyDeckException if the deck runs out unexpectedly.
     * @throws GameStateException if the game is already over.
     */
    @Test
    public void testEliminatedPlayerHasNoCards() throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        Player toEliminate = gameModel.getCurrentPlayer();
        gameModel.eliminateCurrentPlayer();
        assertTrue(toEliminate.getHand().isEmpty());
    }
    /**
     * Verifies that {@code checkWinner()} returns {@code null} when
     * more than one player is still active.
     */
    @Test
    public void testCheckWinnerReturnsNullWhenMultiplePlayers() {
        assertNull(gameModel.checkWinner());
    }
    /**
     * Verifies that {@code checkWinner()} returns a non-null winner
     * when only one player remains active.
     *
     * @throws EmptyDeckException if the deck runs out unexpectedly.
     * @throws GameStateException if the game is already over.
     */
    @Test
    public void testCheckWinnerReturnsWinnerWhenOnePlayerLeft()
            throws EmptyDeckException, GameStateException {
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        gameModel.nextTurn();
        gameModel.eliminateCurrentPlayer();
        assertNotNull(gameModel.checkWinner());
    }
    /**
     * Verifies that the game is not over at the start of a new match.
     */
    @Test
    public void testIsGameOverFalseAtStart() {
        assertFalse(gameModel.isGameOver());
    }
    /**
     * Verifies that {@code isGameOver()} returns {@code true} after
     * {@code checkWinner()} detects the last remaining player.
     *
     * @throws EmptyDeckException if the deck runs out unexpectedly.
     * @throws GameStateException if the game is already over.
     */
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
    /**
     * Verifies that calling {@code nextTurn()} after the game is over
     * throws a {@link GameStateException} as expected.
     *
     * @throws EmptyDeckException if the deck runs out unexpectedly.
     * @throws GameStateException during setup eliminations (expected to succeed).
     */
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