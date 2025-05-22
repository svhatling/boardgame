package model.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for loading the board configuration from a JSON file.
 * It reads the file and creates a map of tile IDs with their corresponding actions.
 */
public class BoardConfigLoader {

  private static final Logger logger =
      Logger.getLogger(BoardConfigLoader.class.getName());

  /**
   * Loads the board configuration from a JSON file.
   *
   * @param fileName the name of the JSON file
   * @return a map of tile IDs with their corresponding actions
   */
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
      logger.log(Level.SEVERE, "Error loading board configuration", e);
    }

    return actions;
  }

  /**
   * Static inner class to represent the configuration of a tile.
   */
  public static class TileConfig {
    public int to;
    public String message;

    /**
     * Constructor for TileConfig.
     *
     * @param to      the destination tile ID
     * @param message a message to be displayed when the action is performed
     */
    public TileConfig(int to, String message) {
      this.to = to;
      this.message = message;
    }
  }
}
