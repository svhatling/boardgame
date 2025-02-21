package entity;

public class Tile {
  private int tileId;
  private Tile nextTile;
  private TileAction landAction;

  public Tile(int tileId) {
    this.tileId = tileId;
    this.nextTile = null;
    this.landAction = null;
  }

  public int getTileId() {
    return tileId;
  }

  public Tile getNextTile() {
    return nextTile;
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  public void setLandAction(TileAction action){
    this.landAction = action;
  }

  public void landPlayer(Player player){
    if(landAction != null){
      landAction.perform(player);
    }
  }

}
