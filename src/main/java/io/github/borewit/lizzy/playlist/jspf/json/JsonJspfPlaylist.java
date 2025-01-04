package io.github.borewit.lizzy.playlist.jspf.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonJspfPlaylist {

  private String title;
  private String creator;
  private String annotation;
  private String info;
  private String location;

  @JsonProperty("tracks")
  private List<JsonJspfTrack> tracks = new LinkedList<JsonJspfTrack>();

  // Getters and Setters
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getAnnotation() {
    return annotation;
  }

  public void setAnnotation(String annotation) {
    this.annotation = annotation;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public List<JsonJspfTrack> getTracks() {
    return tracks;
  }

  public void setTracks(List<JsonJspfTrack> tracks) {
    this.tracks = tracks;
  }
}

