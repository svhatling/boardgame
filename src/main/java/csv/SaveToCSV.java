package csv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SaveToCSV {
  private String filename;

  public SaveToCSV(String filename) {
    this.filename = filename;
  }

  public void savePlayers(List<String> players) {
    try {
      FileWriter writer = new FileWriter(filename);
      for (String player : players) {
        writer.write(player + System.lineSeparator());
      }
      writer.close();
      System.out.println("Saved player to " + filename);
    } catch (IOException e) {
      System.err.println("Error saving player to " + e.getMessage());
    }
  }

  public void addPlayer(String player) {
    try {
      FileWriter writer = new FileWriter(filename);
      writer.write(player + "\n");
      writer.close();
      System.out.println("Saved player to " + filename);
    } catch (IOException e) {
      System.err.println("Error saving player to " + filename);
    }
  }

}
