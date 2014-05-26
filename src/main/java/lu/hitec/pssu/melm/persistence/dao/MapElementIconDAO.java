package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.utils.MapElementIconAnchor;

public interface MapElementIconDAO {

  MapElementIcon addMapElementIcon(String hash, long size, String displayName, MapElementIconAnchor anchor);

  void delete(long id);

  void deleteMapElementIconForUnitTest(String hash, long size);

  boolean exist(String hash, long size);

  MapElementIcon getMapElementIcon(long id);

  MapElementIcon getMapElementIcon(String hash, long size);

  boolean iconsAvailable(long libraryId);

  List<MapElementIcon> listAllIcons();

  MapElementIcon updateMapElementIcon(long id, String hash, long size, String displayName,
      MapElementIconAnchor anchor);

  MapElementIcon updateMapElementIcon(long id, String displayName, MapElementIconAnchor anchor);

}
