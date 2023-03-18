package io.github.borewit.lizzy.test.json.playlist;

import io.github.borewit.lizzy.playlist.AbstractPlaylistComponent;
import io.github.borewit.lizzy.playlist.Media;
import io.github.borewit.lizzy.playlist.Parallel;
import io.github.borewit.lizzy.playlist.Sequence;

import java.util.List;
import java.util.stream.Collectors;

public class JsonEntry
{
  public float repeatCount;
  public String source;
  public Long duration;
  public List<JsonEntry> sequence;
  public List<JsonEntry> parallel;

  public static JsonEntry toJson(AbstractPlaylistComponent component)
  {
    JsonEntry jsonEntry = new JsonEntry();
    if (component instanceof Media)
    {
      Media media = (Media) component;
      jsonEntry.source = media.getSource().toString();
      jsonEntry.duration = media.getDuration();
    }
    else if (component instanceof Parallel)
    {
      Parallel parallel = (Parallel) component;
      jsonEntry.parallel = toJson(parallel.getComponents());
    }
    else if (component instanceof Sequence)
    {
      Sequence sequence = (Sequence) component;
      jsonEntry.sequence = toJson(sequence.getComponents());
    }
    jsonEntry.repeatCount = component.getRepeatCount();
    return jsonEntry;
  }

  public static List<JsonEntry> toJson(List<AbstractPlaylistComponent> components)
  {
    return components.stream().map(JsonEntry::toJson).collect(Collectors.toList());
  }
}
