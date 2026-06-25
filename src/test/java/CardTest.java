import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.enums.Suit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Card} class.
 *
 * <p>Validates the value calculation logic for all card ranks,
 * including number cards, face cards, the nine (neutral), the Ace
 * (adaptive), image path generation, and string representation.</p>
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */

public class CardTest {
    /**
     * Verifies that a number card (2–8, 10) returns its face value
     * regardless of the current table sum.
     */
    @Test
    public void testNumberCardValue() {
        Card card = new Card(Rank.FIVE, Suit.HEARTS);
        assertEquals(5, card.getValue(0));
    }
    /**
     * Verifies that a Nine card always returns 0,
     * neither adding nor subtracting from the table sum.
     */
    @Test
    public void testNineValueIsZero() {
        Card card = new Card(Rank.NINE, Suit.CLUBS);
        assertEquals(0, card.getValue(30));
    }
    /**
     * Verifies that face cards (Jack, Queen, King) always return -10,
     * subtracting ten from the table sum.
     */
    @Test
    public void testFaceCardValueIsMinusTen() {
        Card jack = new Card(Rank.JACK, Suit.SPADES);
        Card queen = new Card(Rank.QUEEN, Suit.DIAMONDS);
        Card king = new Card(Rank.KING, Suit.HEARTS);
        assertEquals(-10, jack.getValue(0));
        assertEquals(-10, queen.getValue(0));
        assertEquals(-10, king.getValue(0));
    }
    /**
     * Verifies that an Ace returns 10 when the table sum is low enough
     * that adding 10 does not exceed {@code MAX_SUM} (50).
     */
    @Test
    public void testAceValueTenWhenSumIsLow() {
        Card ace = new Card(Rank.ACE, Suit.SPADES);
        assertEquals(10, ace.getValue(20));
    }
    /**
     * Verifies that an Ace returns 1 when the table sum is high enough
     * that adding 10 would exceed {@code MAX_SUM} (50).
     */
    @Test
    public void testAceValueOneWhenSumIsHigh() {
        Card ace = new Card(Rank.ACE, Suit.SPADES);
        assertEquals(1, ace.getValue(45));
    }
    /**
     * Verifies the Ace boundary case: returns 10 when the table sum is
     * exactly 40 (40 + 10 = 50, which does not exceed the limit).
     */
    @Test
    public void testAceValueTenWhenSumIsExactly40() {
        Card ace = new Card(Rank.ACE, Suit.HEARTS);
        assertEquals(10, ace.getValue(40));
    }
    /**
     * Verifies the Ace boundary case: returns 1 when the table sum is
     * exactly 41 (41 + 10 = 51, which exceeds the limit).
     */
    @Test
    public void testAceValueOneWhenSumIsExactly41() {
        Card ace = new Card(Rank.ACE, Suit.HEARTS);
        assertEquals(1, ace.getValue(41));
    }
    /**
     * Verifies that {@code getImagePath()} returns the correct resource
     * path for a given rank and suit combination.
     */
    @Test
    public void testGetImagePath() {
        Card card = new Card(Rank.JACK, Suit.SPADES);
        assertEquals("/com/example/miniproyecto3/Cartas/jack_of_spades.png",
                card.getImagePath());
    }
    /**
     * Verifies that {@code toString()} returns the card's name in the
     * format {@code "rank of suit"} using lowercase strings.
     */
    @Test
    public void testToString() {
        Card card = new Card(Rank.THREE, Suit.DIAMONDS);
        assertEquals("three of diamonds", card.toString());
    }
}