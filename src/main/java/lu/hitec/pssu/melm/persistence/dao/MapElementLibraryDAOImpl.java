package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

import org.springframework.transaction.annotation.Transactional;

public class MapElementLibraryDAOImpl implements MapElementLibraryDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public void addMapElementLibrary(final String name, final int majorVersion, final int minorVersion) {
		final MapElementLibrary mapElementLibrary = new MapElementLibrary(name, majorVersion, minorVersion);
		this.em.persist(mapElementLibrary);

	}

	@Override
	public List<MapElementLibrary> listAllLibraries() {
		final TypedQuery<MapElementLibrary> query = this.em.createQuery("SELECT mel FROM MapElementLibrary mel ORDER BY mel.id", MapElementLibrary.class);
		return query.getResultList();
	}
}
