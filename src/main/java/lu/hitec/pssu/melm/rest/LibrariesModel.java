package lu.hitec.pssu.melm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public class LibrariesModel {

  private final List<MapElementLibrary> libraries = new ArrayList<>();

  public LibrariesModel(@Nonnull final List<MapElementLibrary> libraries) {
    assert libraries != null : "libraries are null";
    this.libraries.addAll(libraries);
  }

  @Nonnull
  public List<MapElementLibrary> getLibraries() {
    return libraries;
  }

}
