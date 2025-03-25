package entity;

import exception.InvalidDiceRollException;
import java.util.Random;

public class Die {
    private int value;
    private static final Random random = new Random();
    private int sides;

    public Die() {
        this(6);
    }

    public Die(int sides) {
        if (sides <= 0){
            throw new InvalidDiceRollException("Number of sides must be greater than 0");
        }
        this.sides = sides;
        this.value = 0;
    }

    public int rollDie() {
        try {
            this.value = random.nextInt(sides) + 1;
        } catch (Exception e) {
            throw new IllegalStateException("Error rolling die", e);
        }
        return this.value;
    }

    public int getValue() {
        if (this.value == 0) {
            throw new InvalidDiceRollException("Die has not been rolled");
        }
        return this.value;
    }

    public int getSides() {
        return this.sides;
    }

    public void setSides(int sides) {
        if (sides <= 0){
            throw new InvalidDiceRollException("Number of sides must be greater than 0");
        }
        this.sides = sides;
    }

}
