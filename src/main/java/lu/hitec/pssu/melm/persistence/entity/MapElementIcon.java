package lu.hitec.pssu.melm.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lu.hitec.pssu.melm.services.MELMServiceImpl.IconSize;
import lu.hitec.pssu.melm.utils.MapElementIconAnchor;

@Entity
@Table(name = "map_element_icon", uniqueConstraints = @UniqueConstraint(columnNames = { "pic_100px_md5", "size_in_bytes" }))
@SequenceGenerator(name = "map_element_icon_seq", sequenceName = "map_element_icon_seq")
public class MapElementIcon {

  @Column(name = "anchor", nullable = false, updatable = true)
  @Enumerated(EnumType.STRING)
  private MapElementIconAnchor anchor;

  @Column(name = "display_name", nullable = false, updatable = true)
  private String displayName;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "map_element_icon_seq")
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "pic_100px_md5", nullable = false, updatable = true)
  private String pic100pxMd5;

  @Column(name = "size_in_bytes", nullable = false, updatable = true)
  private long sizeInBytes;

  public MapElementIconAnchor getAnchor() {
    return anchor;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getFilePath(final IconSize iconSize) {
    // we store the file in a 2 levels folders hierarchy, taking the first and second char of the hash
    // so the typical path we be something like /b/e/bejdfgjdfgjdfgj-20px.png
    return String.format("%s/%s/%s%s.png", pic100pxMd5.substring(0, 1), pic100pxMd5.substring(1, 2), pic100pxMd5, iconSize.getSuffix());
  }

  public long getId() {
    return id;
  }

  public String getPic100pxMd5() {
    return pic100pxMd5;
  }

  public long getSizeInBytes() {
    return sizeInBytes;
  }

  public void setAnchor(final MapElementIconAnchor anchor) {
    this.anchor = anchor;
  }

  public void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public void setPic100pxMd5(final String pic100pxMd5) {
    this.pic100pxMd5 = pic100pxMd5;
  }

  public void setSizeInBytes(final long sizeInBytes) {
    this.sizeInBytes = sizeInBytes;
  }

  @Override
  public String toString() {
    return "MapElementIcon [displayName=" + displayName + ", id=" + id + ", pic100pxMd5=" + pic100pxMd5 + ", sizeInBytes=" + sizeInBytes
        + ", anchor=" + anchor + "]";
  }

}
