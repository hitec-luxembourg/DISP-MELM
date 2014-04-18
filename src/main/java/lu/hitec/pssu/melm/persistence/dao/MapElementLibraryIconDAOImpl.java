package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

import org.springframework.transaction.annotation.Transactional;

public class MapElementLibraryIconDAOImpl implements MapElementLibraryIconDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public void addIconToLibrary(final MapElementLibrary library, final MapElementIcon icon, final int indexOfIconInLibrary, final String iconNameInLibrary,
			final String iconDescriptionInLibrary) {
		final MapElementLibraryIcon meli = new MapElementLibraryIcon();
		meli.setLibrary(library);
		meli.setIcon(icon);
		meli.setIndexOfIconInLibrary(indexOfIconInLibrary);
		meli.setIconNameInLibrary(iconNameInLibrary);
		meli.setIconDescriptionInLibrary(iconDescriptionInLibrary);
		this.em.persist(meli);
	}

	@Override
	@Transactional
	public void removeIconFromLibrary(final MapElementLibrary library, final MapElementIcon icon) {
		final Query query = this.em.createQuery("DELETE FROM MapElementLibraryIcon meli WHERE meli.library = :library AND meli.icon = :icon");
		query.setParameter("library", library);
		query.setParameter("icon", icon);
		query.executeUpdate();

	}

	@Override
	public List<MapElementLibraryIcon> getIconsInLibrary(final MapElementLibrary library) {
		final TypedQuery<MapElementLibraryIcon> query = this.em.createQuery(
				"SELECT meli FROM MapElementLibraryIcon meli WHERE meli.library = :library ORDER BY meli.indexOfIconInLibrary", MapElementLibraryIcon.class);
		query.setParameter("library", library);
		return query.getResultList();
	}

}
