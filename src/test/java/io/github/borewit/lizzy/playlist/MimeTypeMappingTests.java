package io.github.borewit.lizzy.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.borewit.lizzy.test.json.mime.MimeTypeMapping;
import io.github.borewit.lizzy.test.json.mime.MimeTypeMappings;
import jakarta.activation.FileTypeMap;
import jakarta.activation.MimetypesFileTypeMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MIME-Type mapping tests")
public class MimeTypeMappingTests {

  @Test
  @DisplayName("Check if required Media MIME-Type mapping is present")
  public void checkMediaMimeTypeMappingsPresence() throws IOException {
    FileTypeMap fileTypeMap = new MimetypesFileTypeMap();
    MimeTypeMappings mimeTypeMappings = loadMediaMimeTypeMappings();
    Map<String, List<String>> missingMappings = new TreeMap<>();

    for (MimeTypeMapping mimeTypeMapping : mimeTypeMappings.mimeTypeMappings) {
      String contentType = fileTypeMap.getContentType("." + mimeTypeMapping.extension);
      if (!mimeTypeMapping.mimeType.equals(contentType)) {
        List<String> extensions = missingMappings.computeIfAbsent(mimeTypeMapping.mimeType, k -> new LinkedList<>());
        missingMappings.put(mimeTypeMapping.mimeType, extensions);
        extensions.add(mimeTypeMapping.extension);
      }
    }

    if (!missingMappings.isEmpty()) {
      // Print missing mappings, so you can copy past to `META-INF/mime.types`
      String line = "#-------------------------------------------------------------";
      System.out.println(line);
      System.out.println("# Mappings missing in META-INF/mime.types");
      System.out.println(line);
      for (String mimeType : missingMappings.keySet()) {
        String extensions = String.join(" ", missingMappings.get(mimeType));
        System.out.printf("%s %s%n", mimeType, extensions);
      }
      System.out.println(line);
    }
    assertEquals(0, missingMappings.size(), "Number of missing mappings");
  }

  private MimeTypeMappings loadMediaMimeTypeMappings() throws IOException {
    InputStream is = getClass().getResourceAsStream("/mime-type-mappings.json");
    if (is != null) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(is, MimeTypeMappings.class);
      } finally {
        is.close();
      }
    }
    throw new RuntimeException("Failed to load MIME-types");
  }
}
