package model.entity;

import model.exception.InvalidDiceRollException;
import java.util.Random;

/**
 * Represents a die with a specified number of sides.
 * The die can be rolled to get a random value between 1 and the number of sides.
 */
public class Die {
    private int value;
    private static final Random random = new Random();
    private int sides;

    /**
     * Default constructor for a normal die with 6 sides.
     */
    public Die() {
        this(6);
    }

    /**
     * Constructor for a die with a specified number of sides.
     *
     * @param sides the number of sides on the die
     * @throws InvalidDiceRollException if the number of sides is less than or equal to 0
     */
    public Die(int sides) {
        if (sides <= 0){
            throw new InvalidDiceRollException("Number of sides must be greater than 0");
        }
        this.sides = sides;
        this.value = 0;
    }

    /**
     * Rolls the die and returns the value.
     *
     * @return the rolled value
     * @throws IllegalStateException if an error occurs while rolling the die
     */
    public int rollDie() {
        try {
            this.value = random.nextInt(sides) + 1;
        } catch (Exception e) {
            throw new IllegalStateException("Error rolling die", e);
        }
        return this.value;
    }

    /**
     * Returns the last rolled value.
     *
     * @return the last rolled value
     * @throws InvalidDiceRollException if the die has not been rolled yet
     */
    public int getValue() {
        if (this.value == 0) {
            throw new InvalidDiceRollException("Die has not been rolled");
        }
        return this.value;
    }

    /**
     * Returns the number of sides on the die.
     *
     * @return the number of sides
     */
    public int getSides() {
        return this.sides;
    }

    /**
     * Sets the number of sides on the die.
     *
     * @param sides the number of sides to set
     * @throws InvalidDiceRollException if the number of sides is less than or equal to 0
     */
    public void setSides(int sides) {
        if (sides <= 0){
            throw new InvalidDiceRollException("Number of sides must be greater than 0");
        }
        this.sides = sides;
    }

}
