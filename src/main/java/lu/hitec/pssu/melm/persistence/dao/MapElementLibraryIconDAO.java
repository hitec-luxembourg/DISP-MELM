package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;
import java.util.Set;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public interface MapElementLibraryIconDAO {

  // We still keep indexOfIconInLibrary for further usages as add icon after another icon if we know its index.
  MapElementLibraryIcon addIconToLibrary(MapElementLibrary library, MapElementIcon icon, int indexOfIconInLibrary,
      String iconNameInLibrary, String iconDescriptionInLibrary);

  boolean checkIconInLibrary(@Nonnull MapElementLibrary library, @Nonnull MapElementIcon icon);

  void deleteLibraryIcon(long id);

  List<MapElementLibraryIcon> getIconsInLibrary(MapElementLibrary library);

  MapElementLibraryIcon getLibraryIcon(long id);

  @Nonnull
  Set<MapElementLibrary> getLinkedLibraries(@Nonnull MapElementIcon icon);

  @CheckReturnValue
  MapElementLibraryIcon getNextLibraryIcon(MapElementLibraryIcon libraryIcon);

  @CheckReturnValue
  MapElementLibraryIcon getPreviousLibraryIcon(MapElementLibraryIcon libraryIcon);

  void updateLibraryIcon(long id, MapElementIcon icon, int indexOfIconInLibrary, String iconNameInLibrary, String iconDescriptionInLibrary);
}
