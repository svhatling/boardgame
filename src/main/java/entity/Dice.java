package entity;

public class Dice extends Die {
  private Die die1;
  private Die die2;
  private int sum;

    public Dice() {
    this(6);
    }

    public Dice(int sides) {
        this.die1 = new Die(sides);
        this.die2 = new Die(sides);
        this.sum = 0;
    }

    public int rollDice() {
      this.sum = die1.rollDie() + die2.rollDie();
        return this.sum;
    }

    public int getSum() {
        return this.sum;
    }

    public int getDie1Value() {
        return die1.getValue();
    }

    public int getDie2Value() {
        return die2.getValue();
    }

    public void setSides(int sides) {
        die1.setSides(sides);
        die2.setSides(sides);
    }
}
