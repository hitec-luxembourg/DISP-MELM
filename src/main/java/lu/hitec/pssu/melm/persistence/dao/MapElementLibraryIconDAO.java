package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public interface MapElementLibraryIconDAO {

  MapElementLibraryIcon addIconToLibrary(MapElementLibrary library, MapElementIcon icon, int indexOfIconInLibrary,
      String iconNameInLibrary, String iconDescriptionInLibrary);
  
  void updateLibraryIcon(long id, MapElementIcon icon, int indexOfIconInLibrary,
      String iconNameInLibrary, String iconDescriptionInLibrary);

  void deleteLibraryIcon(long id);

  List<MapElementLibraryIcon> getIconsInLibrary(MapElementLibrary library);
  
  MapElementLibraryIcon getLibraryIcon(long id);
  
  @Nonnull
  Set<MapElementLibrary> getLinkedLibraries(@Nonnull MapElementIcon icon);
  
  boolean checkIconInLibrary(@Nonnull MapElementLibrary library, @Nonnull MapElementIcon icon);
}
