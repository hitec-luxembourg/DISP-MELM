package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

import org.springframework.transaction.annotation.Transactional;

public class MapElementLibraryDAOImpl implements MapElementLibraryDAO {

  @PersistenceContext
  private EntityManager em;

  @Override
  @Transactional
  public MapElementLibrary addMapElementLibrary(final String name, final int majorVersion, final int minorVersion, final String iconMd5) {
    final MapElementLibrary mapElementLibrary = new MapElementLibrary(name, majorVersion, minorVersion, iconMd5);
    em.persist(mapElementLibrary);
    return getMapElementLibrary(name, majorVersion, minorVersion);
  }

  @Override
  @Transactional
  public void deleteMapElementLibrary(final long id) {
    final MapElementLibrary library = em.find(MapElementLibrary.class, id);
    em.remove(library);
  }

  @Override
  @Transactional
  public void deleteMapElementLibraryForUnitTest(final String name, final int majorVersion, final int minorVersion) {
    final Query query = em
        .createQuery("DELETE FROM MapElementLibrary mel WHERE mel.name = :name AND mel.majorVersion = :majorVersion AND mel.minorVersion = :minorVersion");
    query.setParameter("name", name);
    query.setParameter("majorVersion", majorVersion);
    query.setParameter("minorVersion", minorVersion);
    query.executeUpdate();
  }

  @Override
  public MapElementLibrary getMapElementLibrary(final long id) {
    return em.find(MapElementLibrary.class, id);
  }

  @Override
  @CheckReturnValue
  public MapElementLibrary getMapElementLibrary(final String name, final int majorVersion, final int minorVersion) {
    final TypedQuery<MapElementLibrary> query = em
        .createQuery(
            "SELECT mel FROM MapElementLibrary mel WHERE mel.name = :name AND mel.majorVersion = :majorVersion AND mel.minorVersion = :minorVersion",
            MapElementLibrary.class);
    query.setParameter("name", name);
    query.setParameter("majorVersion", majorVersion);
    query.setParameter("minorVersion", minorVersion);
    try {
      return query.getSingleResult();
    } catch (final NoResultException e) {
      return null;
    }
  }

  @Override
  public List<MapElementLibrary> listAllLibraries() {
    final TypedQuery<MapElementLibrary> query = em.createQuery("SELECT mel FROM MapElementLibrary mel ORDER BY mel.id",
        MapElementLibrary.class);
    return query.getResultList();
  }

  @Override
  @Transactional
  public void updateMapElementLibrary(final long id, @Nonnull final String name, final int majorVersion, final int minorVersion,
      final String iconMd5MaybeNull) {
    assert name != null : "name is null";
    final MapElementLibrary foundLibrary = em.find(MapElementLibrary.class, id);
    foundLibrary.setName(name);
    foundLibrary.setMajorVersion(majorVersion);
    foundLibrary.setMinorVersion(minorVersion);
    if (iconMd5MaybeNull != null) {
      foundLibrary.setIconMd5(iconMd5MaybeNull);
    }
    em.merge(foundLibrary);
  }
}
