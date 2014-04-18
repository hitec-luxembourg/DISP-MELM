package lu.hitec.pssu.melm.persistence.entity;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lu.hitec.pssu.melm.services.MELMServiceImpl.IconSize;

@Entity
@Table(name = "map_element_icon")
@SequenceGenerator(name = "map_element_icon_seq", sequenceName = "map_element_icon_seq")
public class MapElementIcon {

  @Column(name = "display_name", nullable = false, updatable = true)
  private String displayName;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "map_element_icon_seq")
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "path", nullable = false, updatable = true)
  private String path;

  @Column(name = "pic_100px_md5", nullable = false, updatable = true)
  private String pic100pxMd5;

  @Column(name = "size_in_bytes", nullable = false, updatable = true)
  private long sizeInBytes;

  @Transient
  public String getAlbumName() {
    return new File(path).getName();
  }

  public String getDisplayName() {
    return displayName;
  }

  @Transient
  public String getFilePath(final IconSize iconSize) {
    return String.format("%s/%s%s.png", path, pic100pxMd5, iconSize.getSuffix()).toString();
  }

  public long getId() {
    return id;
  }

  public String getPath() {
    return path;
  }

  public String getPic100pxMd5() {
    return pic100pxMd5;
  }

  public long getSizeInBytes() {
    return sizeInBytes;
  }

  public void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public void setPic100pxMd5(final String pic100pxMd5) {
    this.pic100pxMd5 = pic100pxMd5;
  }

  public void setSizeInBytes(final long sizeInBytes) {
    this.sizeInBytes = sizeInBytes;
  }

}
