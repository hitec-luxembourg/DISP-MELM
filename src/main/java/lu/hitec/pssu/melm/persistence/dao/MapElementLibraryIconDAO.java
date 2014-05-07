package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public interface MapElementLibraryIconDAO {

  void addIconToLibrary(final MapElementLibrary library, final MapElementIcon icon, final int indexOfIconInLibrary,
      final String iconNameInLibrary, final String iconDescriptionInLibrary);

  void removeIconFromLibrary(final MapElementLibrary library, final MapElementIcon icon);

  List<MapElementLibraryIcon> getIconsInLibrary(final MapElementLibrary library);
  
  boolean checkIconInLibrary(@Nonnull final MapElementIcon icon);
  
  boolean checkIconInLibrary(@Nonnull final MapElementLibrary library, @Nonnull final MapElementIcon icon);
}
