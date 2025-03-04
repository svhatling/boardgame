package entity;
import java.util.*;

public class BoardGame {
  private Board board;
  private List<Player> players;
  private Dice dice;
  private boolean gameOver;
  private Player winner;

  public BoardGame() {
    this.board = new Board();
    this.players = new ArrayList<>();
    this.gameOver = false;
    this.winner = null;
    createBoard();
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard() {
    for (int i = 1; i <= 90; i++) {
      board.addTile(new Tile(i));
    }

    //Stiger opp
    addLadder(2, 45);
    addLadder(8, 29);
    addLadder(34, 53);
    addLadder(40, 58);
    addLadder(55, 74);
    addLadder(65, 86);
    addLadder(68, 89);

    //Stiger ned
    addLadder(88, 46);
    addLadder(84, 63);
    addLadder(82, 14);
    addLadder(71, 31);
    addLadder(62, 25);
    addLadder(27, 6);
  }

  private void addLadder(int start, int end) {
    String message = (start < end) ? "climbing up the ladder!" : "falling down the ladder!";
    board.getTile(start).setTileAction(new LadderAction(end, message));
  }

  public void createDice(int numDice) {
    dice = new Dice(numDice, 6);
  }

  public Board getBoard() {
    return board;
  }

  public void players() {
    System.out.println("The following players are playing the game: ");
    for (Player player : players) {
      System.out.println("Name: " + player.getName());
    }
    System.out.println();
  }

  public boolean isFinished() {
    return gameOver;
  }

  public void playOneRound(){
    for (Player player : players) {
      int roll = dice.rollDice();
      System.out.println(player.getName() + " rolled " + roll);

      int targetTileId = Math.min(player.getCurrentTile().getTileId() + roll, 90);

      player.move(roll);

      System.out.println(player.getName() + " is on tile " + player.getCurrentTile().getTileId());
      System.out.println();

      if (player.getCurrentTile().getTileId() >= 90) {
        winner = player;
        gameOver = true;
        System.out.println("The winner is " + winner.getName());
        return;
      }
    }
  }

  public void showPlayerStatus () {
    for (Player player : players) {
      System.out.println(player.getName() + " is on tile " + player.getCurrentTile().getTileId());
    }
    System.out.println();
  }

  public Player getWinner() {
    return winner;
  }
}