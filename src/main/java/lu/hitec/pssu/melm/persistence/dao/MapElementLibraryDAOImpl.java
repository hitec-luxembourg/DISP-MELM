package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

import org.springframework.transaction.annotation.Transactional;

public class MapElementLibraryDAOImpl implements MapElementLibraryDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<MapElementLibrary> listAllLibraries() {
		final TypedQuery<MapElementLibrary> query = this.em.createQuery("SELECT mel FROM MapElementLibrary mel ORDER BY mel.id", MapElementLibrary.class);
		return query.getResultList();
	}

	@Override
	@Transactional
	public MapElementLibrary addMapElementLibrary(final String name, final int majorVersion, final int minorVersion) {
		final MapElementLibrary mapElementLibrary = new MapElementLibrary(name, majorVersion, minorVersion);
		this.em.persist(mapElementLibrary);
		return getMapElementLibrary(name, majorVersion, minorVersion);
	}

	@Override
	public MapElementLibrary getMapElementLibrary(final String name, final int majorVersion, final int minorVersion) {
		final TypedQuery<MapElementLibrary> query = this.em.createQuery(
				"SELECT mel FROM MapElementLibrary mel WHERE mel.name = :name AND mel.majorVersion = :majorVersion AND mel.minorVersion = :minorVersion", MapElementLibrary.class);
		query.setParameter("name", name);
		query.setParameter("majorVersion", majorVersion);
		query.setParameter("minorVersion", minorVersion);
		return query.getSingleResult();
	}

	@Override
	@Transactional
	public void deleteMapElementLibrary(final String name, final int majorVersion, final int minorVersion) {
		final Query query = this.em
				.createQuery("DELETE FROM MapElementLibrary mel WHERE mel.name = :name AND mel.majorVersion = :majorVersion AND mel.minorVersion = :minorVersion");
		query.setParameter("name", name);
		query.setParameter("majorVersion", majorVersion);
		query.setParameter("minorVersion", minorVersion);
		query.executeUpdate();

	}
}
