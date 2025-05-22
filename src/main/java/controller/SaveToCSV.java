package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Class that saves player records to a CSV file.
 */
public class SaveToCSV {
  private final String filename;

  /**
   * Constructor for SaveToCSV.
   *
   * @param filename the name of the CSV file
   */
  public SaveToCSV(String filename) {
    this.filename = filename;
  }

  /**
   * Adds a single player record to the CSV file.
   *
   * @param player the PlayerRecord object
   */
  public void addPlayer(PlayerRecord player) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
      writer.write(player.name + "," + player.piece);
      writer.newLine();
      System.out.println("Added player to: " + filename);
    } catch (IOException e) {
      System.err.println("Error adding player to " + filename);
    }
  }

}
