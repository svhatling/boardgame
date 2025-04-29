package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ReadFromCSV {
  private String filename;

  public ReadFromCSV(String filename) {
    this.filename = filename;
  }

  public List <String> readPlayers() {
    List<String> players = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (!line.isEmpty()) {
          players.add(line);
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading player from " + e.getMessage());
    }
    return players;
  }

}
