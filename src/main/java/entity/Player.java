package entity;

public class Player {
  private String playerName;
  private Tile currentTile;

  public Player(String playerName, Tile currentTile) {
    this.playerName = playerName;
    this.currentTile = null;
  }

  public void placeOnTile(Tile tile) {
    this.currentTile = tile;
  }

  public void move(int steps){
    for (int i = 0; i < steps; i++) {
      if (currentTile.getNextTile() != null){
        currentTile = currentTile.getNextTile();
      }else{
        break;
      }
    }
  }

  public Tile getCurrentTile() {
    return currentTile;
  }

  public String getName() {
    return playerName;
  }

}
