package lu.hitec.pssu.melm.persistence.dao;

import java.io.File;
import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public interface MapElementIconDAO {

	public List<MapElementIcon> listAllIcons();

	public void addMapElementIcon(File icon);

}
