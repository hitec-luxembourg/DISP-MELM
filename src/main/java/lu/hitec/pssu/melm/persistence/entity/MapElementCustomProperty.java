package lu.hitec.pssu.melm.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lu.hitec.pssu.melm.utils.CustomPropertyType;

@Entity
@Table(name = "map_element_custom_property", uniqueConstraints = { @UniqueConstraint(columnNames = { "map_element_library_icon_id",
    "unique_name" }) })
@SequenceGenerator(name = "map_element_custom_property_seq", sequenceName = "map_element_custom_property_seq")
public class MapElementCustomProperty {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "map_element_custom_property_seq")
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "map_element_library_icon_id", nullable = false)
  private MapElementLibraryIcon mapElementLibraryIcon;

  @Column(name = "type", nullable = false, updatable = true)
  @Enumerated(EnumType.STRING)
  private CustomPropertyType type;

  @Column(name = "unique_name", nullable = false, updatable = true)
  private String uniqueName;

  public long getId() {
    return id;
  }

  public MapElementLibraryIcon getMapElementLibraryIcon() {
    return mapElementLibraryIcon;
  }

  public CustomPropertyType getType() {
    return type;
  }

  public String getUniqueName() {
    return uniqueName;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public void setMapElementLibraryIcon(final MapElementLibraryIcon mapElementLibraryIcon) {
    this.mapElementLibraryIcon = mapElementLibraryIcon;
  }

  public void setType(final CustomPropertyType type) {
    this.type = type;
  }

  public void setUniqueName(final String uniqueName) {
    this.uniqueName = uniqueName;
  }

  @Override
  public String toString() {
    return "MapElementCustomProperty [id=" + id + ", mapElementLibraryIcon=" + mapElementLibraryIcon + ", type=" + type + ", uniqueName="
        + uniqueName + "]";
  }

}
