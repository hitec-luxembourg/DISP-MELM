package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public interface MapElementIconDAO {

  MapElementIcon addMapElementIcon(final String hash, final long size, final String displayName);

  void delete(long id);

  void deleteMapElementIconForUnitTest(final String hash, final long size);

  boolean exist(String hash, long size);

  MapElementIcon getMapElementIcon(long id);

  MapElementIcon getMapElementIcon(final String hash, final long size);

  List<MapElementIcon> listAllIcons();

}
