package lu.hitec.pssu.melm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public class AddLibraryIconsModel {

  private final List<MapElementIcon> icons = new ArrayList<>();

  private final MapElementLibrary library;

  public AddLibraryIconsModel(@Nonnull final MapElementLibrary library, @Nonnull final List<MapElementIcon> icons) {
    assert library != null : "library is null";
    assert icons != null : "Icons are null";
    this.library = library;
    this.icons.addAll(icons);
  }

  @Nonnull
  public List<MapElementIcon> getIcons() {
    return icons;
  }

  @Nonnull
  public MapElementLibrary getLibrary() {
    return library;
  }

}
