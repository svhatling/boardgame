import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.Die;
import org.junit.jupiter.api.Test;

public class DieTest {

  @Test
  public void testDefaultConstructor() {
    Die die = new Die();
    assertEquals(6, die.getSides());
    assertEquals(0, die.getValue());
  }

  @Test
  public void testCustomConstructor() {
    Die die = new Die(10);
    assertEquals(10, die.getSides());
  }

  @Test
  public void testRollDie() {
    Die die = new Die();
    int roll = die.rollDie();
    assertTrue(roll >= 1 && roll <= 6);
    assertEquals(roll, die.getValue());
  }

  @Test
  public void testSetSides() {
    Die die = new Die(6);
    die.setSides(10);
    assertEquals(10, die.getSides());

    int roll = die.rollDie();
    assertTrue(roll >= 1 && roll <= 10);
  }

  @Test
  public void testRollMultipleTimes() {
    Die die = new Die(6);
    for (int i = 0; i < 100; i++) {
      int roll = die.rollDie();
      assertTrue(roll >= 1 && roll <= 6);
    }
  }

  @Test
  public void testIllegalSides() {
    assertThrows(IllegalArgumentException.class, () -> new Die(0));
    assertThrows(IllegalArgumentException.class, () -> new Die(-1));
  }

  @Test
  public void testInvalidConstructorThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> new Die(0));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }

  @Test
  public void testSetSidesInvalidValueThrowsException() {
    Die die = new Die();
    Exception exception = assertThrows(IllegalArgumentException.class, () -> die.setSides(0));
    assertEquals("Number of sides must be greater than 0", exception.getMessage());
  }

  @Test
  public void testGetValueBeforeRollThrowsException() {
    Die die = new Die();
    Exception exception = assertThrows(IllegalStateException.class, die::getValue);
    assertEquals("Die has not been rolled", exception.getMessage());
  }

}
