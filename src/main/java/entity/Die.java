package entity;

import java.util.Random;

public class Die {
    private int value;
    private static final Random random = new Random();
    private int sides;

    public Die() {
        this(6);
    }

    public Die(int sides) {
        this.sides = sides;
        this.value = 0;
    }

    public int rollDie() {
        this.value = random.nextInt(sides)+1;
        return this.value;
    }

    public int getValue() {
        return this.value;
    }

    public int getSides() {
        return this.sides;
    }

    public void setSides(int sides) {
        this.sides = sides;
    }

}
