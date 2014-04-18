package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public interface MapElementIconDAO {

	List<MapElementIcon> listAllIcons();

	MapElementIcon addMapElementIcon(final String hash, final long size, final String displayName);

	MapElementIcon getMapElementIcon(final String hash, final long size);

	void deleteMapElementIcon(final String hash, final long size);

	MapElementIcon getMapElementIcon(long id);

	void delete(long id);

	boolean exist(String hash, long size);

}
