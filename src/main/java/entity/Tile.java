package entity;

public class Tile {
  private int tileId;
  private TileAction tileAction;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public int getTileId() {
    return tileId;
  }

  public TileAction getTileAction() {
    return tileAction;
  }

  public void setTileAction(TileAction action) {
    this.tileAction = action;
  }

  public void landPlayer(Player player) {
    if (tileAction != null) {
      tileAction.perform(player);
    }
  }
}