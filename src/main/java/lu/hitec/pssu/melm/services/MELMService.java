package lu.hitec.pssu.melm.services;

import java.io.File;

import javax.annotation.Nonnull;

import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.MELMException;

public interface MELMService {
  File addZipFile(@Nonnull final String name, @Nonnull final String version, @Nonnull final File tmpZipFile) throws MELMException;

  String buildArchiveFilename(@Nonnull final String libraryName, @Nonnull final String version);

  void extractZipFile(@Nonnull final File file) throws MELMException;

  File getBaseDirectory();

  File getTargetArchiveFile(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException;

  XMLSelectionPathParser validateAndParse(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException;
}
