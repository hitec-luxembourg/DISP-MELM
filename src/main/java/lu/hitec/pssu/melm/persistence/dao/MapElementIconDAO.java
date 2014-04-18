package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public interface MapElementIconDAO {

	List<MapElementIcon> listAllIcons();

	void addMapElementIcon(final String path, final String hash, final long length, final String displayName);

	MapElementIcon getMapElementIcon(long id);

	void delete(long id);

	boolean exist(String hash, long size);

}
