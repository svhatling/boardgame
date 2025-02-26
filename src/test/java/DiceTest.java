import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.Dice;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DiceTest {

  @Test
  public void testValidConstructor() {
    Dice dice = new Dice(3, 6);
    dice.rollDice();
    List<Integer> values = dice.getDiceValues();
    assertEquals(3, values.size());
  }

  @Test
  public void testInvalidConstructorThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> new Dice(0, 6));
    assertEquals("Number of dice must be greater than 0", exception.getMessage());
  }

  @Test
  public void testRollDice() {
    Dice dice = new Dice(2, 6);
    int sum = dice.rollDice();
    assertTrue(sum >= 2 && sum <= 12);
    List<Integer> values = dice.getDiceValues();
    assertEquals(sum, values.stream().mapToInt(Integer::intValue).sum());
  }

  @Test
  public void testSetSides() {
    Dice dice = new Dice(2, 6);
    dice.setSides(10);
    dice.rollDice();
    for (int value : dice.getDiceValues()) {
      assertTrue(value >= 1 && value <= 10);
    }
  }

  @Test
  public void testRollDiceMultipleTimes() {
    Dice dice = new Dice(2, 6);
    int previousSum = dice.rollDice();
    boolean valuesChanged = false;
    for (int i = 0; i < 10; i++) {
      int currentSum = dice.rollDice();
      if (currentSum != previousSum) {
        valuesChanged = true;
        break;
      }
    }
    assertTrue(valuesChanged);
  }

  @Test
  public void testGetDiceValues() {
    Dice dice = new Dice(3, 6);
    dice.rollDice();
    List<Integer> values = dice.getDiceValues();
    assertEquals(3, values.size());
    for (int value : values) {
      assertTrue(value >= 1 && value <= 6);
    }
  }

  @Test
  public void testInvalidSidesThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> new Dice(2, -1));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }

  @Test
  public void testSetSidesInvalidValueThrowsException() {
    Dice dice = new Dice(2, 6);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> dice.setSides(0));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }


}
