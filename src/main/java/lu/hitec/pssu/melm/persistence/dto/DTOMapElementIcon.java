package lu.hitec.pssu.melm.persistence.dto;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public class DTOMapElementIcon {

  private MapElementIcon icon;

  private Set<MapElementLibrary> libraries = new HashSet<>();

  public DTOMapElementIcon(@Nonnull final MapElementIcon icon, @Nonnull final Set<MapElementLibrary> libraries) {
    assert icon != null : "icon is null";
    assert libraries != null : "libraries is null";
    this.icon = icon;
    this.libraries = libraries;
  }

  public MapElementIcon getIcon() {
    return icon;
  }

  public Set<MapElementLibrary> getLibraries() {
    return libraries;
  }

  public void setIcon(final MapElementIcon icon) {
    this.icon = icon;
  }

  public void setLibraries(final Set<MapElementLibrary> libraries) {
    this.libraries = libraries;
  }

  @Override
  public String toString() {
    return "DTOMapElementIcon [icon=" + icon + ", libraries=" + libraries + "]";
  }

}
