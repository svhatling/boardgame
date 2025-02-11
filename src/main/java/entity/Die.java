package entity;

public class Die {
    private int value;

    public Die() {
        this.value = 0;
    }

    public int rollDie() {
        this.value = (int) (Math.random() * 6) + 1;
        return this.value;
    }

    public int getValue() {
        return this.value;
    }

}
