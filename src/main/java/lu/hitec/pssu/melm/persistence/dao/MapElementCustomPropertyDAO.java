package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.transaction.Transactional;

import lu.hitec.pssu.melm.persistence.entity.MapElementCustomProperty;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.utils.CustomPropertyType;

public interface MapElementCustomPropertyDAO {

  MapElementCustomProperty addCustomProperty(MapElementLibraryIcon libraryIcon, String uniqueName, CustomPropertyType type);

  List<MapElementCustomProperty> checkPropertyInIcon(MapElementLibraryIcon libraryIcon, String uniqueName);

  void deleteCustomProperty(long id);

  List<MapElementCustomProperty> getCustomProperties(MapElementLibraryIcon libraryIcon);

  MapElementCustomProperty getCustomProperty(long id);

  @Transactional
  void updateCustomProperty(long id, String uniqueName, CustomPropertyType type);

}
