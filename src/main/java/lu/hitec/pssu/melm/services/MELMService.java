package lu.hitec.pssu.melm.services;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.entity.MapElementCustomProperty;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.utils.CustomPropertyType;
import lu.hitec.pssu.melm.utils.MapElementIconAnchor;

import org.w3c.dom.NodeList;

public interface MELMService {

  public static final int ORDER_INCREMENT = 1000000;

  MapElementIcon addIconAndFiles(@Nonnull String displayName, @Nonnull final MapElementIconAnchor anchor, @Nonnull File iconLargeFile,
      File iconSelectedLargeFile) throws MELMException;

  MapElementLibrary addLibrary(@Nonnull String libraryName, int majorVersion, int minorVersion, @Nonnull String iconMd5)
      throws MELMException;

  String addLibraryIcon(@Nonnull File sourceIconFile) throws MELMException;

  void addLibraryIcon(long id, int iconIndex, @Nonnull String iconName, @Nonnull String iconDescription, long iconId) throws MELMException;

  void addProperty(long id, @Nonnull String uniqueName, @Nonnull CustomPropertyType type) throws MELMException;

  String buildArchiveFilename(@Nonnull String libraryName, int majorVersion, int minorVersion);

  MapElementLibrary cloneLibrary(long id, @Nonnull String libraryName, int majorVersion, int minorVersion, @Nonnull String iconMd5)
      throws MELMException;

  void deleteCustomProperty(long id);

  void deleteIconAndFiles(long id) throws MELMException;

  void deleteLibrary(long id);

  void deleteLibraryIcon(long id);

  void deleteProperty(long id);

  File extractImportedLibrary(@Nonnull File file) throws MELMException;

  File generateXmlAndPrepareZipFile(@Nonnull String name, int majorVersion, int minorVersion) throws MELMException;

  File generateZipFile(@Nonnull File zipFolder) throws MELMException;

  MapElementIcon getIcon(long id);

  File getIconFile(long id, @Nonnull String size);

  File getIconSelectedFile(long id, String size);

  File getIconsDirectory();

  File getLibrariesDirectory();

  MapElementLibrary getLibrary(long id);

  MapElementLibrary getLibrary(@Nonnull String libraryName, int majorVersion, int minorVersion);

  MapElementLibraryIcon getLibraryIcon(long id);

  File getLibraryIconFile(long id);

  List<MapElementLibraryIcon> getLibraryIcons(long id);

  List<MapElementLibraryIcon> getLibraryIcons(@Nonnull String libraryName, int majorVersion, int minorVersion);

  Set<MapElementLibrary> getLinkedLibraries(MapElementIcon icon);

  List<MapElementCustomProperty> getProperties(long id);

  File getTargetArchiveFile(@Nonnull String libraryName, int majorVersion, int minorVersion) throws MELMException;

  boolean iconsAvailable(@Nonnull long id);

  File importLibrary(@Nonnull String name, int majorVersion, int minorVersion, @Nonnull File libraryFile) throws MELMException;

  List<MapElementIcon> listAllIcons();

  List<MapElementLibrary> listAllLibraries();

  void moveImportedIcons(@Nonnull MapElementLibrary mapElementLibrary, @Nonnull NodeList nodeList, @Nonnull File libraryFolder)
      throws MELMException;

  String getSelectedFileName(String fileNameWithExtension);

  String moveImportedLibraryIcon(@Nonnull File libraryFolder, @Nonnull String libraryName, int majorVersion, int minorVersion)
      throws MELMException;

  void moveLibraryIcon(long id, String which);

  void updateIconAndFiles(long id, @Nonnull String displayName, @Nonnull MapElementIconAnchor anchor, File largeIconFile) throws MELMException;

  void updateLibrary(long id, @Nonnull String libraryName, int majorVersion, int minorVersion, String iconMd5MaybeNull)
      throws MELMException;

  void updateLibraryIcon(long id, int iconIndex, @Nonnull String iconName, @Nonnull String iconDescription, long iconId)
      throws MELMException;

  void updateProperty(long id, @Nonnull String uniqueName, @Nonnull CustomPropertyType type) throws MELMException;

  NodeList validateImportedLibraryAndGetNodeList(@Nonnull String libraryName, int majorVersion, int minorVersion) throws MELMException;
}
