package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Class that reads player records from a CSV file.
 */
public class ReadFromCSV {
  private final String filename;

  /**
   * Constructor for ReadFromCSV.
   *
   * @param filename the name of the CSV file
   */
  public ReadFromCSV(String filename) {
    this.filename = filename;
  }

  /**
   * Reads player records from the CSV file.
   *
   * @return a list of PlayerRecord objects
   */
  public List <PlayerRecord> readPlayers() {
    List<PlayerRecord> list = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) {
          continue;
        }
        if (line.toLowerCase().startsWith("name,")) {
          continue;
        }
        String[] data = line.split(",", 2);
        if (data.length < 2) {
          continue;
        }
        String name = data[0].trim();
        String piece = data[1].trim();
        list.add(new PlayerRecord(name, piece));
      }
    } catch (IOException e) {
      System.err.println("Error reading player from " + e.getMessage());
    }
    return list;
  }

}
