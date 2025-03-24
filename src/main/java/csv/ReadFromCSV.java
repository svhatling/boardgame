package csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFromCSV {
  private String filename;

  public ReadFromCSV(String filename) {
    this.filename = filename;
  }

  public void readPlayers() {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      reader.close();
    } catch (IOException e) {
      System.err.println("Error reading player from " + filename);
    }
  }

}
