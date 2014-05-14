package lu.hitec.pssu.melm.persistence.dto;

import lu.hitec.pssu.melm.utils.CustomPropertyType;

public class DTOMapElementCustomProperty {

  private long id;

  private CustomPropertyType type;

  private String uniqueName;

  public long getId() {
    return id;
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

  public void setType(final CustomPropertyType type) {
    this.type = type;
  }

  public void setUniqueName(final String uniqueName) {
    this.uniqueName = uniqueName;
  }

  @Override
  public String toString() {
    return "DTOMapElementCustomProperty [id=" + id + ", type=" + type + ", uniqueName=" + uniqueName + "]";
  }

}
