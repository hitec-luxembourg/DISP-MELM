package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public interface MapElementLibraryIconDAO {

	MapElementLibraryIcon addIconToLibrary(final MapElementLibrary library, final MapElementIcon icon, final int indexOfIconInLibrary, final String iconNameInLibrary,
			final String iconDescriptionInLibrary);

	void updateLibraryIcon(final long id, final MapElementIcon icon, final int indexOfIconInLibrary, final String iconNameInLibrary, final String iconDescriptionInLibrary);

	void deleteLibraryIcon(final long id);

	List<MapElementLibraryIcon> getIconsInLibrary(final MapElementLibrary library);

	MapElementLibraryIcon getLibraryIcon(final long id);

	MapElementLibraryIcon getPreviousLibraryIcon(MapElementLibraryIcon libraryIcon);

	MapElementLibraryIcon getNextLibraryIcon(MapElementLibraryIcon libraryIcon);

	boolean checkIconInLibrary(@Nonnull final MapElementIcon icon);

	boolean checkIconInLibrary(@Nonnull final MapElementLibrary library, @Nonnull final MapElementIcon icon);

}
