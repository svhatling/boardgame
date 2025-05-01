package model.util;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PieceImageLoader {
  private static final String PATH = "/images/";
  private static final Map<String, Image> cache = new HashMap<>();

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
