package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public interface MapElementLibraryDAO {

	List<MapElementLibrary> listAllLibraries();

	void addMapElementLibrary(final String name, final int majorVersion, final int minorVersion);

}
