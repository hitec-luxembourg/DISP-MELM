package lu.hitec.pssu.melm.persistence.dto;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public class DTOMapElementLibraryIcon {

  private MapElementIcon icon;

  private String iconDescriptionInLibrary;

  private String iconNameInLibrary;

  private long id;

  private int indexOfIconInLibrary;

  private MapElementLibrary library;

  public MapElementIcon getIcon() {
    return icon;
  }

  public String getIconDescriptionInLibrary() {
    return iconDescriptionInLibrary;
  }

  public String getIconNameInLibrary() {
    return iconNameInLibrary;
  }

  public long getId() {
    return id;
  }

  public int getIndexOfIconInLibrary() {
    return indexOfIconInLibrary;
  }

  public MapElementLibrary getLibrary() {
    return library;
  }

  public void setIcon(final MapElementIcon icon) {
    this.icon = icon;
  }

  public void setIconDescriptionInLibrary(final String iconDescriptionInLibrary) {
    this.iconDescriptionInLibrary = iconDescriptionInLibrary;
  }

  public void setIconNameInLibrary(final String iconNameInLibrary) {
    this.iconNameInLibrary = iconNameInLibrary;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public void setIndexOfIconInLibrary(final int indexOfIconInLibrary) {
    this.indexOfIconInLibrary = indexOfIconInLibrary;
  }

  public void setLibrary(final MapElementLibrary library) {
    this.library = library;
  }

  @Override
  public String toString() {
    return "DTOMapElementLibraryIcon [icon=" + icon + ", iconDescriptionInLibrary=" + iconDescriptionInLibrary + ", iconNameInLibrary="
        + iconNameInLibrary + ", id=" + id + ", indexOfIconInLibrary=" + indexOfIconInLibrary + ", library=" + library + "]";
  }

}
