package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public interface MapElementLibraryIconDAO {

	public void addIconToLibrary(final MapElementLibrary library, final MapElementIcon icon, final int indexOfIconInLibrary, final String iconNameInLibrary,
			final String iconDescriptionInLibrary);

	public void removeIconFromLibrary(final MapElementLibrary library, final MapElementIcon icon, final int indexOfIconInLibrary);

	public List<MapElementLibraryIcon> getIconsInLibrary(final MapElementLibrary library);
}
