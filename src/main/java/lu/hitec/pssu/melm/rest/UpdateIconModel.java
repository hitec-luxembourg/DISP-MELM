package lu.hitec.pssu.melm.rest;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public class UpdateIconModel {

  private final String error;

  private final MapElementIcon icon;

  public UpdateIconModel(@Nonnull final MapElementIcon icon) {
    assert icon != null : "icon is null";
    this.icon = icon;
    error = null;
  }

  public UpdateIconModel(@Nonnull final MapElementIcon icon, @Nonnull final String error) {
    assert icon != null : "icon is null";
    assert error != null : "error is null";
    assert error.length() != 0 : "error is empty";
    this.icon = icon;
    this.error = error;
  }

  public String getError() {
    return error;
  }

  @Nonnull
  public MapElementIcon getIcon() {
    return icon;
  }

}
