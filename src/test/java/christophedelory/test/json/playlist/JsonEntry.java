package christophedelory.test.json.playlist;

import christophedelory.playlist.AbstractPlaylistComponent;
import christophedelory.playlist.Media;
import christophedelory.playlist.Parallel;
import christophedelory.playlist.Sequence;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonEntry
{
    public int repeatCount;
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

    public static List<JsonEntry> toJson(AbstractPlaylistComponent[] components)
    {
        return Arrays.stream(components).map(JsonEntry::toJson).collect(Collectors.toList());
    }
}
