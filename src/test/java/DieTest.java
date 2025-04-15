import static org.junit.jupiter.api.Assertions.*;

import model.entity.Die;
import model.exception.InvalidDiceRollException;
import org.junit.jupiter.api.Test;

public class DieTest {

  @Test
  public void testDefaultConstructor() {
    Die die = new Die();
    die.rollDie();
    int value = die.getValue();
    assertTrue(value >= 1 && value <= 6);
  }

  @Test
  public void testCustomConstructorSetsCorrectSides() {
    Die die = new Die(10);
    assertEquals(10, die.getSides());
  }

  @Test
  public void testRollDieReturnsValueInRange() {
    Die die = new Die();
    int roll = die.rollDie();
    assertTrue(roll >= 1 && roll <= 6);
    assertEquals(roll, die.getValue());
  }

  @Test
  public void testSetSidesUpdatesSideCount() {
    Die die = new Die(6);
    die.setSides(10);
    assertEquals(10, die.getSides());

    int roll = die.rollDie();
    assertTrue(roll >= 1 && roll <= 10);
  }

  @Test
  public void testRollMultipleTimesStaysInRange() {
    Die die = new Die(6);
    for (int i = 0; i < 100; i++) {
      int roll = die.rollDie();
      assertTrue(roll >= 1 && roll <= 6);
    }
  }

  @Test
  public void testConstructorWithZeroSidesThrowsException() {
    Exception exception = assertThrows(InvalidDiceRollException.class, () -> new Die(0));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }

  @Test
  public void testConstructorWithNegativeSidesThrowsException() {
    Exception exception = assertThrows(InvalidDiceRollException.class, () -> new Die(-1));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }

  @Test
  public void testSetSidesWithInvalidValueThrowsException() {
    Die die = new Die();
    Exception exception = assertThrows(InvalidDiceRollException.class, () -> die.setSides(0));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }

  @Test
  public void testGetValueBeforeRollThrowsException() {
    Die die = new Die();
    Exception exception = assertThrows(InvalidDiceRollException.class, die::getValue);
    assertEquals("Die has not been rolled", exception.getMessage());
  }
}
