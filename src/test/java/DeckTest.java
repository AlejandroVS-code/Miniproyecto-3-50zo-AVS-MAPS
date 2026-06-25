import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.Deck;
import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.enums.Suit;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link Deck} class.
 *
 * <p>Validates deck initialization, card drawing, recycling behaviour,
 * shuffle integrity, and the exception thrown when drawing from an
 * empty deck.</p>
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class DeckTest {

    private Deck deck;
    /**
     * Creates a fresh {@link Deck} before each test to ensure
     * test isolation.
     */
    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }
    /**
     * Verifies that a newly created deck contains exactly 52 cards,
     * one for each rank and suit combination.
     */
    @Test
    public void testDeckInitialSize() {
        assertEquals(52, deck.size());
    }
    /**
     * Verifies that a newly created deck is not empty.
     */
    @Test
    public void testDeckIsNotEmptyAfterInit() {
        assertFalse(deck.isEmpty());
    }
    /**
     * Verifies that drawing a card decreases the deck size by exactly one.
     *
     * @throws EmptyDeckException if the deck is unexpectedly empty.
     */
    @Test
    public void testDrawReducesSize() throws EmptyDeckException {
        int initialSize = deck.size();
        deck.draw();
        assertEquals(initialSize - 1, deck.size());
    }
    /**
     * Verifies that {@code draw()} returns a non-null {@link Card} instance.
     *
     * @throws EmptyDeckException if the deck is unexpectedly empty.
     */
    @Test
    public void testDrawReturnsCard() throws EmptyDeckException {
        Card card = deck.draw();
        assertNotNull(card);
    }
    /**
     * Verifies that drawing all 52 cards leaves the deck empty.
     *
     * @throws EmptyDeckException if an unexpected error occurs during drawing.
     */
    @Test
    public void testDrawAllCards() throws EmptyDeckException {
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        assertTrue(deck.isEmpty());
    }
    /**
     * Verifies that drawing from an empty deck throws
     * {@link EmptyDeckException} as expected.
     *
     * @throws EmptyDeckException during the setup draws (expected to succeed).
     */
    @Test
    public void testDrawFromEmptyDeckThrowsException() throws EmptyDeckException {
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        assertThrows(EmptyDeckException.class, () -> deck.draw());
    }
    /**
     * Verifies that recycling a list of cards increases the deck size
     * by the exact number of cards recycled.
     *
     * @throws EmptyDeckException if the deck is unexpectedly empty.
     */
    @Test
    public void testRecycleIncreasesSize() throws EmptyDeckException {
        List<Card> cards = new ArrayList<>();
        cards.add(deck.draw());
        cards.add(deck.draw());
        cards.add(deck.draw());
        int sizeAfterDraw = deck.size();
        deck.recycle(cards);
        assertEquals(sizeAfterDraw + 3, deck.size());
    }
    /**
     * Verifies that recycling cards into a completely empty deck makes
     * the deck non-empty again, simulating the in-game reshuffle mechanic.
     */
    @Test
    public void testRecycleFromEmptyDeck() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Rank.ACE, Suit.SPADES));
        cards.add(new Card(Rank.KING, Suit.HEARTS));

        Deck emptyDeck = new Deck();
        try {
            for (int i = 0; i < 52; i++) emptyDeck.draw();
        } catch (EmptyDeckException e) {
            fail("Should not throw here");
        }

        emptyDeck.recycle(cards);
        assertFalse(emptyDeck.isEmpty());
    }
    /**
     * Verifies that shuffling the deck does not change its size,
     * ensuring no cards are lost or duplicated during the operation.
     */
    @Test
    public void testShuffleDoesNotChangeDeckSize() {
        int sizeBefore = deck.size();
        deck.shuffle();
        assertEquals(sizeBefore, deck.size());
    }
}