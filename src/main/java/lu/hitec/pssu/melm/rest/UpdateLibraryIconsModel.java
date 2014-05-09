package lu.hitec.pssu.melm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public class UpdateLibraryIconsModel {

  private final List<MapElementIcon> icons = new ArrayList<>();

  private final MapElementLibraryIcon libraryIcon;

  public UpdateLibraryIconsModel(@Nonnull final MapElementLibraryIcon libraryIcon, @Nonnull final List<MapElementIcon> icons) {
    assert libraryIcon != null : "libraryIcon is null";
    assert icons != null : "Icons are null";
    this.libraryIcon = libraryIcon;
    this.icons.addAll(icons);
  }

  @Nonnull
  public List<MapElementIcon> getIcons() {
    return icons;
  }

  public MapElementLibraryIcon getLibraryIcon() {
    return libraryIcon;
  }

}
