package lu.hitec.pssu.melm.rest;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public class UpdateLibraryIconsModel {

  private final String error;

  private final MapElementLibraryIcon libraryIcon;

  public UpdateLibraryIconsModel(@Nonnull final MapElementLibraryIcon libraryIcon) {
    assert libraryIcon != null : "library icon is null";
    this.libraryIcon = libraryIcon;
    error = null;
  }

  public UpdateLibraryIconsModel(@Nonnull final MapElementLibraryIcon libraryIcon, @Nonnull final String error) {
    assert libraryIcon != null : "library icon is null";
    assert error != null : "error is null";
    this.libraryIcon = libraryIcon;
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public MapElementLibraryIcon getLibraryIcon() {
    return libraryIcon;
  }

}
