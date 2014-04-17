package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public interface MapElementIconDAO {

	public List<MapElementIcon> listAllIcons();

	public void addMapElementIcon(final String path, final String hash, final long length, final String displayName);

	public MapElementIcon getMapElementIcon(long id);

	public void delete(long id);

	public boolean exist(String hash, long size);

}
