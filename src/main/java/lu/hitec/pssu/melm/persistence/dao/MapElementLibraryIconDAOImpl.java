package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
  public void addIconToLibrary(final MapElementLibrary library, final MapElementIcon icon, final int indexOfIconInLibrary,
      final String iconNameInLibrary, final String iconDescriptionInLibrary) {
    final MapElementLibraryIcon libraryIcon = new MapElementLibraryIcon();
    libraryIcon.setLibrary(library);
    libraryIcon.setIcon(icon);
    libraryIcon.setIndexOfIconInLibrary(indexOfIconInLibrary);
    libraryIcon.setIconNameInLibrary(iconNameInLibrary);
    libraryIcon.setIconDescriptionInLibrary(iconDescriptionInLibrary);
    em.persist(libraryIcon);
  }

  // @Override
  // @Transactional
  // public void removeIconFromLibrary(final MapElementLibrary library, final MapElementIcon icon) {
  // final Query query = em.createQuery("DELETE FROM MapElementLibraryIcon meli WHERE meli.library = :library AND meli.icon = :icon");
  // query.setParameter("library", library);
  // query.setParameter("icon", icon);
  // query.executeUpdate();
  // }

  @Override
  public boolean checkIconInLibrary(final MapElementIcon icon) {
    final TypedQuery<MapElementLibraryIcon> query = em.createQuery("SELECT meli FROM MapElementLibraryIcon meli WHERE meli.icon = :icon",
        MapElementLibraryIcon.class);
    query.setParameter("icon", icon);
    return !query.getResultList().isEmpty();
  }

  @Override
  public boolean checkIconInLibrary(final MapElementLibrary library, final MapElementIcon icon) {
    final TypedQuery<MapElementLibraryIcon> query = em.createQuery(
        "SELECT meli FROM MapElementLibraryIcon meli WHERE meli.library = :library AND meli.icon = :icon", MapElementLibraryIcon.class);
    query.setParameter("library", library);
    query.setParameter("icon", icon);
    return !query.getResultList().isEmpty();
  }

  @Override
  public List<MapElementLibraryIcon> getIconsInLibrary(@Nonnull final MapElementLibrary library) {
    assert library != null : "Library is null";
    final TypedQuery<MapElementLibraryIcon> query = em.createQuery(
        "SELECT meli FROM MapElementLibraryIcon meli WHERE meli.library = :library ORDER BY meli.indexOfIconInLibrary",
        MapElementLibraryIcon.class);
    query.setParameter("library", library);
    return query.getResultList();
  }

  @Override
  public MapElementLibraryIcon getLibraryIcon(final long id) {
    return em.find(MapElementLibraryIcon.class, id);
  }

  @Override
  @Transactional
  public void deleteLibraryIcon(final long id) {
    final MapElementLibraryIcon libraryIcon = em.find(MapElementLibraryIcon.class, id);
    em.remove(libraryIcon);
  }

  @Override
  @Transactional
  public void updateLibraryIcon(final long id, @Nonnull final MapElementIcon icon, final int indexOfIconInLibrary,
      @Nonnull final String iconNameInLibrary, @Nonnull final String iconDescriptionInLibrary) {
    assert icon != null : "icon is null";
    assert iconNameInLibrary != null : "iconNameInLibrary is null";
    assert iconDescriptionInLibrary != null : "iconDescriptionInLibrary is null";
    final MapElementLibraryIcon libraryIcon = em.find(MapElementLibraryIcon.class, id);
    libraryIcon.setIcon(icon);
    libraryIcon.setIndexOfIconInLibrary(indexOfIconInLibrary);
    libraryIcon.setIconNameInLibrary(iconNameInLibrary);
    libraryIcon.setIconDescriptionInLibrary(iconDescriptionInLibrary);
    em.merge(libraryIcon);
  }

}
