package edu.ntnu.idatt2003;

public class Dice {
    private int value;

    public Dice() {
        this.value = 0;
    }

    public int rollDice() {
        this.value = (int) (Math.random() * 6) + 1;
        return this.value;
    }

    public int getValue() {
        return this.value;
    }

}
