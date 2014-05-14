package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementCustomProperty;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.utils.CustomPropertyType;

public interface MapElementCustomPropertyDAO {

  MapElementCustomProperty addCustomProperty(final MapElementLibraryIcon libraryIcon, final String uniqueName, final CustomPropertyType type);

  void deleteCustomProperty(final long id);

  List<MapElementCustomProperty> getCustomProperties(final MapElementLibraryIcon libraryIcon);

  MapElementCustomProperty getCustomProperty(final long id);

  void updateCustomProperty(final long id, final String uniqueName, final CustomPropertyType type);

}
