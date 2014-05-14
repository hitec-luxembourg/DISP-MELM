package lu.hitec.pssu.melm.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "map_element_library_icon", uniqueConstraints = { @UniqueConstraint(columnNames = { "library_id", "icon_id" }),
    @UniqueConstraint(columnNames = { "library_id", "index_of_icon_in_library" }),
    @UniqueConstraint(columnNames = { "library_id", "icon_name_in_library" }) })
@SequenceGenerator(name = "map_element_library_icon_seq", sequenceName = "map_element_library_icon_seq")
public class MapElementLibraryIcon {

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "mapElementLibraryIcon")
  private Set<MapElementCustomProperty> customProperties = new HashSet<>(0);

  @ManyToOne
  @ForeignKey(name = "fk_meli_to_icon")
  @JoinColumn(name = "icon_id", nullable = false)
  private MapElementIcon icon;

  @Column(name = "icon_description_in_library", nullable = false, updatable = true)
  private String iconDescriptionInLibrary;

  @Column(name = "icon_name_in_library", nullable = false, updatable = true)
  private String iconNameInLibrary;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "map_element_library_icon_seq")
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "index_of_icon_in_library", nullable = false, updatable = true)
  private int indexOfIconInLibrary;

  @ManyToOne
  @ForeignKey(name = "fk_meli_to_ibrary")
  @JoinColumn(name = "library_id", nullable = false)
  private MapElementLibrary library;

  public Set<MapElementCustomProperty> getCustomProperties() {
    return customProperties;
  }

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

  public void setCustomProperties(final Set<MapElementCustomProperty> customProperties) {
    this.customProperties = customProperties;
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
    return "MapElementLibraryIcon [customProperties=" + customProperties + ", icon=" + icon + ", iconDescriptionInLibrary="
        + iconDescriptionInLibrary + ", iconNameInLibrary=" + iconNameInLibrary + ", id=" + id + ", indexOfIconInLibrary="
        + indexOfIconInLibrary + ", library=" + library + "]";
  }

}
