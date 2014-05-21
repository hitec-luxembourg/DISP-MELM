package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementCustomProperty;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.utils.CustomPropertyType;

import org.springframework.transaction.annotation.Transactional;

public class MapElementCustomPropertyDAOImpl implements MapElementCustomPropertyDAO {

  @PersistenceContext
  private EntityManager em;

  @Override
  @Transactional
  public MapElementCustomProperty addCustomProperty(@Nonnull final MapElementLibraryIcon libraryIcon, @Nonnull final String uniqueName,
      @Nonnull final CustomPropertyType type) {
    assert libraryIcon != null : "library icon is null";
    assert uniqueName != null : "unique name is null";
    assert type != null : "type is null";
    final MapElementCustomProperty customProperty = new MapElementCustomProperty();
    customProperty.setMapElementLibraryIcon(libraryIcon);
    customProperty.setUniqueName(uniqueName);
    customProperty.setType(type);
    em.persist(customProperty);
    return customProperty;
  }

  @Override
  public List<MapElementCustomProperty> checkPropertyInIcon(@Nonnull final MapElementLibraryIcon libraryIcon,
      @Nonnull final String uniqueName) {
    assert libraryIcon != null : "library icon is null";
    assert uniqueName != null : "unique name is null";
    final TypedQuery<MapElementCustomProperty> query = em.createQuery(
        "SELECT mecp FROM MapElementCustomProperty mecp WHERE mecp.mapElementLibraryIcon = :libraryIcon AND mecp.uniqueName = :uniqueName",
        MapElementCustomProperty.class);
    query.setParameter("libraryIcon", libraryIcon);
    query.setParameter("uniqueName", uniqueName);
    return query.getResultList();
  }

  @Override
  @Transactional
  public void deleteCustomProperty(final long id) {
    final MapElementCustomProperty customProperty = em.find(MapElementCustomProperty.class, id);
    em.remove(customProperty);
  }

  @Override
  @Transactional
  public List<MapElementCustomProperty> getCustomProperties(@Nonnull final MapElementLibraryIcon libraryIcon) {
    assert libraryIcon != null : "libraryIcon is null";
    final TypedQuery<MapElementCustomProperty> query = em.createQuery(
        "SELECT mecp FROM MapElementCustomProperty mecp WHERE mecp.mapElementLibraryIcon = :libraryIcon ORDER BY mecp.uniqueName",
        MapElementCustomProperty.class);
    query.setParameter("libraryIcon", libraryIcon);
    return query.getResultList();
  }

  @Override
  public MapElementCustomProperty getCustomProperty(final long id) {
    return em.find(MapElementCustomProperty.class, id);
  }

  @Override
  @Transactional
  public void updateCustomProperty(final long id, @Nonnull final String uniqueName, @Nonnull final CustomPropertyType type) {
    assert uniqueName != null : "uniqueName is null";
    assert type != null : "type is null";
    final MapElementCustomProperty customProperty = em.find(MapElementCustomProperty.class, id);
    customProperty.setUniqueName(uniqueName);
    customProperty.setType(type);
    em.merge(customProperty);
  }

}
