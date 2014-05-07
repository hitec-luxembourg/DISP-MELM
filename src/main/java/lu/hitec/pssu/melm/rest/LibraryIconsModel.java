package lu.hitec.pssu.melm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public class LibraryIconsModel {

  private final List<MapElementLibraryIcon> icons = new ArrayList<>();

  private final MapElementLibrary library;

  public LibraryIconsModel(@Nonnull final MapElementLibrary library, @Nonnull final List<MapElementLibraryIcon> icons) {
    assert library != null : "library is null";
    assert icons != null : "Icons are null";
    this.library = library;
    this.icons.addAll(icons);
  }

  @Nonnull
  public List<MapElementLibraryIcon> getIcons() {
    return icons;
  }

  @Nonnull
  public MapElementLibrary getLibrary() {
    return library;
  }

}
