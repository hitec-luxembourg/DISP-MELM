package lu.hitec.pssu.melm.persistence.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
  public MapElementLibraryIcon addIconToLibrary(final MapElementLibrary library, final MapElementIcon icon, final int indexOfIconInLibrary,
      final String iconNameInLibrary, final String iconDescriptionInLibrary) {
    final MapElementLibraryIcon libraryIcon = new MapElementLibraryIcon();
    libraryIcon.setLibrary(library);
    libraryIcon.setIcon(icon);
    libraryIcon.setIndexOfIconInLibrary(indexOfIconInLibrary);
    libraryIcon.setIconNameInLibrary(iconNameInLibrary);
    libraryIcon.setIconDescriptionInLibrary(iconDescriptionInLibrary);
    em.persist(libraryIcon);
    return libraryIcon;
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
  public boolean checkNameInLibrary(final MapElementLibrary library, final String iconName) {
    final TypedQuery<MapElementLibraryIcon> query = em.createQuery(
        "SELECT meli FROM MapElementLibraryIcon meli WHERE meli.library = :library AND meli.iconNameInLibrary = :iconName", MapElementLibraryIcon.class);
    query.setParameter("library", library);
    query.setParameter("iconName", iconName);
    return !query.getResultList().isEmpty();
  }

  @Override
  @Transactional
  public void deleteLibraryIcon(final long id) {
    final MapElementLibraryIcon libraryIcon = em.find(MapElementLibraryIcon.class, id);
    em.remove(libraryIcon);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<Long, Integer> countLibrariesElements() {
    final Map<Long, Integer> result = new HashMap<Long, Integer>();
    final Query query = em.createQuery("SELECT li.library.id, count(li) FROM MapElementLibraryIcon li GROUP BY li.library.id");
    List<Object[]> queryResult = null;
    try {
      queryResult = query.getResultList();
    } catch (final NoResultException e) {
      // It is possible that no value exists
      queryResult = new ArrayList<Object[]>();
    }
    if (null != queryResult) {
      for (final Object[] row : queryResult) {
        result.put((Long)row[0], ((Long)row[1]).intValue());
      }
    }
    return result;
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
  @Nonnull
  public Set<MapElementLibrary> getLinkedLibraries(final MapElementIcon icon) {
    final TypedQuery<MapElementLibraryIcon> query = em.createQuery("SELECT meli FROM MapElementLibraryIcon meli WHERE meli.icon = :icon",
        MapElementLibraryIcon.class);
    query.setParameter("icon", icon);
    final List<MapElementLibraryIcon> list = query.getResultList();
    final Set<MapElementLibrary> results = new HashSet<>();
    for (final MapElementLibraryIcon mapElementLibraryIcon : list) {
      results.add(mapElementLibraryIcon.getLibrary());
    }
    return results;
  }

  @Override
  @CheckReturnValue
  public MapElementLibraryIcon getNextLibraryIcon(final MapElementLibraryIcon libraryIcon) {
    MapElementLibraryIcon result = null;
    assert libraryIcon != null : "Library icon is null";
    final TypedQuery<MapElementLibraryIcon> query = em
        .createQuery(
            "SELECT meli FROM MapElementLibraryIcon meli WHERE meli.indexOfIconInLibrary > :indexOfIconInLibrary ORDER BY meli.indexOfIconInLibrary ASC",
            MapElementLibraryIcon.class).setMaxResults(1);
    query.setParameter("indexOfIconInLibrary", libraryIcon.getIndexOfIconInLibrary());
    try {
      result = query.getSingleResult();
    } catch (final NoResultException e) {
      // Nothing to do
    }
    return result;
  }

  @Override
  @CheckReturnValue
  public MapElementLibraryIcon getPreviousLibraryIcon(final MapElementLibraryIcon libraryIcon) {
    MapElementLibraryIcon result = null;
    assert libraryIcon != null : "Library icon is null";
    final TypedQuery<MapElementLibraryIcon> query = em
        .createQuery(
            "SELECT meli FROM MapElementLibraryIcon meli WHERE meli.indexOfIconInLibrary < :indexOfIconInLibrary ORDER BY meli.indexOfIconInLibrary DESC",
            MapElementLibraryIcon.class).setMaxResults(1);
    query.setParameter("indexOfIconInLibrary", libraryIcon.getIndexOfIconInLibrary());
    try {
      result = query.getSingleResult();
    } catch (final NoResultException e) {
      // Nothing to do
    }
    return result;
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
