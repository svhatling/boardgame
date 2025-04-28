package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SaveToCSV {
  private String filename;

  public SaveToCSV(String filename) {
    this.filename = filename;
  }

  public void savePlayers(List<String> players) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      for (String player : players) {
        writer.write(player);
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println("Error saving player to " + e.getMessage());
    }
  }

  public void addPlayer(String player) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
      writer.write(player);
      writer.newLine();
      System.out.println("Added player to" + filename);
    } catch (IOException e) {
      System.err.println("Error adding player to " + filename);
    }
  }

}
