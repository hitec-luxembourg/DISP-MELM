package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

import org.springframework.transaction.annotation.Transactional;

public class MapElementIconDAOImpl implements MapElementIconDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<MapElementIcon> listAllIcons() {
		final TypedQuery<MapElementIcon> query = this.em.createQuery("SELECT mei FROM MapElementIcon mei ORDER BY mei.id", MapElementIcon.class);
		return query.getResultList();
	}

	@Override
	@Transactional
	public void addMapElementIcon(final String path, final String hash, final long length, final String displayName) {
		final MapElementIcon mapElementIcon = new MapElementIcon();
		mapElementIcon.setPath(path);
		mapElementIcon.setPic100pxMd5(hash);
		mapElementIcon.setSizeInBytes(length);
		mapElementIcon.setDisplayName(displayName);
		this.em.persist(mapElementIcon);

	}

	@Override
	public MapElementIcon getMapElementIcon(final long id) {
		return this.em.find(MapElementIcon.class, id);

	}

	@Override
	@Transactional
	public void delete(final long id) {
		this.em.remove(getMapElementIcon(id));

	}

	@Override
	public boolean exist(final String hash, final long size) {
		final TypedQuery<MapElementIcon> query = this.em
				.createQuery("SELECT mei FROM MapElementIcon mei WHERE mei.pic100pxMd5 = :hash AND sizeInBytes = :size", MapElementIcon.class).setParameter("hash", hash)
				.setParameter("size", size);

		return query.getResultList().size() > 0;
	}

}
