package lu.hitec.pssu.melm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public class IconsModel {

  private List<MapElementIcon> icons = new ArrayList<>();

  public IconsModel(@Nonnull final List<MapElementIcon> icons) {
    assert icons != null : "Icons are null";
    this.icons = icons;
  }

  @Nonnull
  public List<MapElementIcon> getIcons() {
    return icons;
  }

  public void setIcons(@Nonnull final List<MapElementIcon> icons) {
    assert icons != null : "Icons are null";
    this.icons = icons;
  }

}
