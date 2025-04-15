import model.entity.Dice;
import model.exception.InvalidDiceRollException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiceTest {

  @Test
  public void testValidConstructorAndRoll() {
    Dice dice = new Dice(3, 6);
    dice.rollDice();
    List<Integer> values = dice.getDiceValues();
    assertEquals(3, values.size());
  }

  @Test
  public void testConstructorWithZeroDiceThrowsException() {
    Exception exception = assertThrows(InvalidDiceRollException.class, () -> new Dice(0, 6));
    assertEquals("Number of dice must be greater than 0", exception.getMessage());
  }

  @Test
  public void testInvalidSidesThrowsException() {
    Exception exception = assertThrows(InvalidDiceRollException.class, () -> new Dice(2, -1));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }

  @Test
  public void testRollDiceReturnsSumInRange() {
    Dice dice = new Dice(2, 6);
    int sum = dice.rollDice();
    assertTrue(sum >= 2 && sum <= 12);

    List<Integer> values = dice.getDiceValues();
    assertEquals(sum, values.stream().mapToInt(Integer::intValue).sum());
  }

  @Test
  public void testGetDiceValuesBeforeRollThrowsException() {
    Dice dice = new Dice(2, 6);
    Exception exception = assertThrows(InvalidDiceRollException.class, dice::getDiceValues);
    assertEquals("Die has not been rolled", exception.getMessage());
  }

  @Test
  public void testSetSidesChangesAllDice() {
    Dice dice = new Dice(2, 6);
    dice.setSides(10);
    dice.rollDice();
    for (int value : dice.getDiceValues()) {
      assertTrue(value >= 1 && value <= 10);
    }
  }

  @Test
  public void testSetSidesInvalidValueThrowsException() {
    Dice dice = new Dice(2, 6);
    Exception exception = assertThrows(InvalidDiceRollException.class, () -> dice.setSides(0));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }

  @Test
  public void testRollingDiceMultipleTimesChangesValues() {
    Dice dice = new Dice(2, 6);
    int firstSum = dice.rollDice();
    boolean changed = false;

    for (int i = 0; i < 10; i++) {
      int nextSum = dice.rollDice();
      if (nextSum != firstSum) {
        changed = true;
        break;
      }
    }

    assertTrue(changed, "Dice rolls should produce different sums over multiple attempts.");
  }
}
