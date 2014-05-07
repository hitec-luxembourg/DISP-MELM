package lu.hitec.pssu.melm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public class IconsModel {

  private final List<MapElementIcon> icons = new ArrayList<>();

  public IconsModel(@Nonnull final List<MapElementIcon> icons) {
    assert icons != null : "Icons are null";
    this.icons.addAll(icons);
  }

  @Nonnull
  public List<MapElementIcon> getIcons() {
    return icons;
  }

}
