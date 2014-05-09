package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public interface MapElementLibraryDAO {

  MapElementLibrary addMapElementLibrary(final String name, final int majorVersion, final int minorVersion, final String iconMd5);

  void deleteMapElementLibrary(final long id);

  void deleteMapElementLibraryForUnitTest(final String name, final int majorVersion, final int minorVersion);

  MapElementLibrary getMapElementLibrary(final long id);

  MapElementLibrary getMapElementLibrary(final String name, final int majorVersion, final int minorVersion);

  List<MapElementLibrary> listAllLibraries();

  void updateMapElementLibrary(final long id, final String name, final int majorVersion, final int minorVersion, final String iconMd5);

}
