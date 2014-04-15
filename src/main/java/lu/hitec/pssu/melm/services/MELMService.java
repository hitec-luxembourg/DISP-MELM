package lu.hitec.pssu.melm.services;

import java.io.File;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.exceptions.MELMException;

public interface MELMService {
  String buildArchiveFilename(@Nonnull final String libraryName, @Nonnull final String version);

  File getTargetArchiveFile(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException;
  
  File addZipFile(@Nonnull final String name, @Nonnull final String version, @Nonnull final File tmpZipFile) throws MELMException;
}
