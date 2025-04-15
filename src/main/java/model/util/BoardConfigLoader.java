package model.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.*;

public class BoardConfigLoader {

  public static Map<Integer, TileConfig> loadConfig(String fileName) {
    Map<Integer, TileConfig> actions = new HashMap<>();

    try {
      ObjectMapper mapper = new ObjectMapper();
      InputStream inputStream = BoardConfigLoader.class.getClassLoader().getResourceAsStream(fileName);
      JsonNode root = mapper.readTree(inputStream);

      // Ladders
      for (JsonNode ladder : root.get("ladders")) {
        int from = ladder.get("from").asInt();
        int to = ladder.get("to").asInt();
        actions.put(from, new TileConfig(to, "climbs up the ladder!"));
      }

      // Snakes
      for (JsonNode snake : root.get("snakes")) {
        int from = snake.get("from").asInt();
        int to = snake.get("to").asInt();
        actions.put(from, new TileConfig(to, "slides down the ladder!"));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return actions;
  }

  public static class TileConfig {
    public int to;
    public String message;

    public TileConfig(int to, String message) {
      this.to = to;
      this.message = message;
    }
  }
}
