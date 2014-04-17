package lu.hitec.pssu.melm.services;

import java.io.File;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.MELMException;

public interface MELMService {
  File addZipFile(@Nonnull final String name, @Nonnull final String version, @Nonnull final File tmpZipFile) throws MELMException;

  String buildArchiveFilename(@Nonnull final String libraryName, @Nonnull final String version);

  void copyImportedIcons(@Nonnull final File libraryFolder) throws MELMException;

  @CheckReturnValue
  File extractZipFile(@Nonnull final File file) throws MELMException;

  @CheckReturnValue
  File getIconFile(@Nonnull final String id, int size);

  @Nonnull
  File getIconsDirectory();

  @Nonnull
  File getLibrariesDirectory();

  File getTargetArchiveFile(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException;

  XMLSelectionPathParser validateAndParse(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException;

}
