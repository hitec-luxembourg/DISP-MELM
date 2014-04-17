package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public interface MapElementLibraryDAO {

	public List<MapElementLibrary> listAllLibraries();

	public void addMapElementLibrary(final String name, final int majorVersion, final int minorVersion);

}
