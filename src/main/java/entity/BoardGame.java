package entity;

public class BoardGame {

  private Board board;
  private List<Player> players;
  private Dice dice;
  private Player currentPlayer;

  // Constructor
  public BoardGame() {
    players = new ArrayList<>();
  }

  // Legg til en spiller
  public void addPlayer(Player player) {
    players.add(player);
  }

  // Sett opp brettet
  public void createBoard() {
    board = new Board();
    for (int i = 1; i <= 100; i++) { // Eksempel: 100 felt
      Tile tile = new Tile(i);
      board.addTile(tile);
    }

    // Eksempel på en stige (ladder action) mellom felt 5 og 20
    Tile startTile = board.getTile(5);
    Tile endTile = board.getTile(20);
    startTile.setLandAction(new LadderAction(20, "Stige til felt 20"));
  }

  // Sett opp terningene
  public void createDice() {
    dice = new Dice(1); // Eksempel: 1 terning
  }

  // Spill selve spillet
  public void play() {
    boolean gameOver = false;

    while (!gameOver) {
      for (Player player : players) {
        currentPlayer = player;
        int diceValue = dice.roll();
        System.out.println(player.getName() + " trillet " + diceValue);

        // Flytt spilleren
        player.move(diceValue);

        // Sjekk om spilleren har vunnet
        if (player.getCurrentTile().getTileId() == 100) { // Eksempel: Mål er felt 100
          System.out.println(player.getName() + " har vunnet spillet!");
          gameOver = true;
          break;
        }
      }
    }
  }

  // Hent vinneren (kan være nyttig for sluttlogikk)
  public Player getWinner() {
    for (Player player : players) {
      if (player.getCurrentTile().getTileId() == 100) { // Eksempel: Mål er felt 100
        return player;
      }
    }
    return null;
  }

}
