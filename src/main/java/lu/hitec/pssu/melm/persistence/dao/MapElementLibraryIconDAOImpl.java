package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	public void removeIconFromLibrary(final MapElementLibrary library, final MapElementIcon icon, final int indexOfIconInLibrary) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<MapElementLibraryIcon> getIconsInLibrary(final MapElementLibrary library) {
		// TODO Auto-generated method stub
		return null;
	}

}
