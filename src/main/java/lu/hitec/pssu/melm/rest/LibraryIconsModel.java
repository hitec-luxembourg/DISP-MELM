package lu.hitec.pssu.melm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.dto.DTOMapElementLibraryIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public class LibraryIconsModel {

  private final List<DTOMapElementLibraryIcon> icons = new ArrayList<>();

  private final MapElementLibrary library;

  public LibraryIconsModel(@Nonnull final MapElementLibrary library, @Nonnull final List<DTOMapElementLibraryIcon> icons) {
    assert library != null : "library is null";
    assert icons != null : "Icons are null";
    this.library = library;
    this.icons.addAll(icons);
  }

  @Nonnull
  public List<DTOMapElementLibraryIcon> getIcons() {
    return icons;
  }

  @Nonnull
  public MapElementLibrary getLibrary() {
    return library;
  }

}
