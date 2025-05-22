package model.util;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to load and cache images for game pieces.
 * Images are loaded from the resources directory and cached for reuse.
 */
public class PieceImageLoader {
  private static final String PATH = "/images/";
  private static final Map<String, Image> cache = new HashMap<>();

  /**
   * Loads an image for a given piece type.
   * The image is cached for future use.
   *
   * @param pieceType the type of the game piece (e.g., "car", "hat", "dog")
   * @return the loaded Image object, or null if the image could not be found
   */
  public static Image get(String pieceType) {
    String key = pieceType.toLowerCase();
    if (!cache.containsKey(key)) {
      String resource = PATH + key + ".png";
      InputStream is = PieceImageLoader.class.getResourceAsStream(resource);
      if (is == null) {
        System.err.println("Image not found: " + resource);
        return null;
      }
      Image img = new Image(is);
      cache.put(key, img);
    }
    return cache.get(key);
  }


}
