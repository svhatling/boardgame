package model.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.*;
import model.entity.BackToStartAction;

public class BoardConfigLoader {

  public static Map<Integer, TileConfig> loadConfig(String fileName) {
    Map<Integer, TileConfig> actions = new HashMap<>();

    try {
      ObjectMapper mapper = new ObjectMapper();
      InputStream inputStream = BoardConfigLoader.class
          .getClassLoader()
          .getResourceAsStream(fileName);
      JsonNode root = mapper.readTree(inputStream);

      // All tile-based actions (ladders, snakes, back-to-start)
      for (JsonNode tileNode : root.path("tiles")) {
        int from = tileNode.get("id").asInt();
        JsonNode actionNode = tileNode.get("action");
        int to = actionNode.get("destinationTileId").asInt();
        String message = actionNode.get("description").asText();
        actions.put(from, new TileConfig(to, message));
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

    public TileConfig(BackToStartAction backToStartAction) {
    }
  }
}
