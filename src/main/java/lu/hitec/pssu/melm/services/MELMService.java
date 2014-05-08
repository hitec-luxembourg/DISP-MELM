package lu.hitec.pssu.melm.services;

import java.io.File;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.transaction.Transactional;

import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

public interface MELMService {
  MapElementIcon addIconAndFiles(@Nonnull final String displayName, @Nonnull final File iconLargeFile) throws MELMException;

  MapElementLibrary addLibrary(@Nonnull final String libraryName, @Nonnull final String version, @Nonnull final String iconMd5)
      throws MELMException;

  String addLibraryIcon(@Nonnull final File sourceIconFile) throws MELMException;

  void addLibraryIcon(@Nonnull final String libraryName, @Nonnull final String majorVersion, @Nonnull final String minorVersion,
      @Nonnull final String iconIndex, @Nonnull final String iconName, @Nonnull final String iconDescription, @Nonnull final String iconId)
      throws MELMException;

  String buildArchiveFilename(@Nonnull final String libraryName, @Nonnull final String version);

  @Transactional
  void deleteIconAndFiles(final long id) throws MELMException;

  @Transactional
  void deleteLibrary(@Nonnull final String libraryName, final int majorVersion, final int minorVersion);

  void deleteLibraryIcon(@Nonnull final String libraryName, final int majorVersion, final int minorVersion, final long iconId);

  @CheckReturnValue
  File extractImportedLibrary(@Nonnull final File file) throws MELMException;

  File generateZipFile(@Nonnull final File zipFolder) throws MELMException;

  MapElementIcon getIcon(long id);

  File getIconFile(final long id, @Nonnull final String size);

  @Nonnull
  File getIconsDirectory();

  @Nonnull
  File getLibrariesDirectory();

  MapElementLibrary getLibrary(@Nonnull final String libraryName, final int majorVersion, final int minorVersion);

  File getLibraryIconFile(@Nonnull final String libraryName, final int majorVersion, final int minorVersion);

  List<MapElementLibraryIcon> getLibraryIcons(@Nonnull final String libraryName, final int majorVersion, final int minorVersion);

  File getTargetArchiveFile(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException;

  File importLibrary(@Nonnull final String name, @Nonnull final String version, @Nonnull final File libraryFile) throws MELMException;

  List<MapElementIcon> listAllIcons();

  List<MapElementLibrary> listAllLibraries();

  void moveImportedIcons(@Nonnull final MapElementLibrary mapElementLibrary, @Nonnull final XMLSelectionPathParser libraryParser,
      @Nonnull final File libraryFolder) throws MELMException;

  String moveImportedLibraryIcon(@Nonnull final XMLSelectionPathParser libraryParserlibraryIconRelativePath,
      @Nonnull final File libraryFolder) throws MELMException;

  File prepareZipFile(@Nonnull final String name, final int majorVersion, final int minorVersion) throws MELMException;

  void updateLibrary(@Nonnull final String id, @Nonnull final String libraryName, @Nonnull final String version,
      final String iconMd5MaybeNull) throws MELMException;

  XMLSelectionPathParser validateAndParseImportedLibrary(@Nonnull final String libraryName, @Nonnull final String version)
      throws MELMException;
}
