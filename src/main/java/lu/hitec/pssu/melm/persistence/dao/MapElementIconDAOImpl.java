package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public class MapElementIconDAOImpl implements MapElementIconDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<MapElementIcon> listAllIcons() {
		final TypedQuery<MapElementIcon> query = this.em.createQuery("SELECT mei FROM MapElementIcon mei ORDER BY mei.id", MapElementIcon.class);
		return query.getResultList();
	}

	@Override
	public void addMapElementIcon(final String path, final String hash, final long length) {
		final MapElementIcon mapElementIcon = new MapElementIcon();
		mapElementIcon.setPath(path);
		mapElementIcon.setPic100pxMd5(hash);
		mapElementIcon.setSizeInBytes(length);
		this.em.persist(mapElementIcon);

	}

	@Override
	public void delete(final long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exist(final String hash, final long size) {
		// TODO Auto-generated method stub
		return false;
	}

}
