package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SaveToCSV {
  private final String filename;

  public SaveToCSV(String filename) {
    this.filename = filename;
  }

  public void savePlayers(List<PlayerRecord> players) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      for (PlayerRecord player : players) {
        writer.write(player.name + "," + player.piece);
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println("Error saving player to " + e.getMessage());
    }
  }

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
