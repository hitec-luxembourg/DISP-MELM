package lu.hitec.pssu.melm.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.utils.LibraryValidator;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MELMServiceImpl implements MELMService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MELMServiceImpl.class);

  /**
   * Base directory for the libraries storage
   */
  private File baseDirectory = null;

  public MELMServiceImpl(final File baseDirectory) {
    if (!baseDirectory.isDirectory()) {
      final String msg = String.format("Base directory doesn't exist: %s", baseDirectory.getAbsolutePath());
      throw new IllegalArgumentException(msg);
    }
    this.baseDirectory = baseDirectory;
  }

  /**
   * Creates a unique filename from the given name and version format which is used to store files, and to find files back again with the
   * given information.
   */
  public String buildArchiveFilename(@Nonnull final String libraryName, @Nonnull final String version) {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";
    return String.format("%s-%s.zip", libraryName, version);
  }

  public File getTargetArchiveFile(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";
    final File archiveDirectory = LibraryValidator.buildDirectoryForLibraryVersion(baseDirectory.getAbsolutePath(), libraryName, version);
    if (archiveDirectory == null) {
      throw new MELMException("Failed to create target dir. to store new file");
    }

    final String newArchiveFilename = buildArchiveFilename(libraryName, version);
    final File targetArchiveFile = new File(archiveDirectory, newArchiveFilename);

    return targetArchiveFile;
  }

  public File addZipFile(@Nonnull final String libraryName, @Nonnull final String version, @Nonnull final File tmpZipFile)
      throws MELMException {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";
    assert tmpZipFile != null : "Tmp zip file is null";
    final File targetArchiveFile = getTargetArchiveFile(libraryName, version);

    if (targetArchiveFile.isFile()) {
      LOGGER.warn(String.format("Target file for picture : %s exists, will be overwritten", targetArchiveFile.getName()));
      if (!targetArchiveFile.delete()) {
        LOGGER.debug(String.format("Could not delete file : %s", targetArchiveFile.getAbsolutePath()));
      }
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(String.format("About to copy tmp. archive file %s to %s", tmpZipFile.getAbsolutePath(),
          targetArchiveFile.getAbsolutePath()));
    }
    FileOutputStream out = null;
    FileInputStream in = null;
    try {
      in = new FileInputStream(tmpZipFile);
      out = new FileOutputStream(targetArchiveFile);
      IOUtils.copy(in, out);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(String.format("Copied tmp. archive file %s to %s", tmpZipFile.getAbsolutePath(), targetArchiveFile.getAbsolutePath()));
      }
    } catch (final Exception e) {
      final String msg = String.format("Failed copying archive to target location %s", targetArchiveFile.getName());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(msg);
      }
      throw new MELMException(msg, e);
    } finally {
      closeQuietly(out);
      closeQuietly(in);
    }
    return targetArchiveFile;
    // try {
    // extractFolder(targetArchiveFile.getAbsolutePath());
    // } catch (final Exception e) {
    // final String msg = "Failed to unzip file";
    // if (LOGGER.isDebugEnabled()) {
    // LOGGER.debug(msg);
    // }
    // throw new MELMException(msg, e);
    // }
  }

  protected void extractZipFile(@Nonnull final File file) throws MELMException {
    assert file != null : "Zip file is null";
    final int buffer = 2048;

    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(file);
      final String newPath = file.getName().substring(0, file.getName().length() - 4);

      if (!new File(newPath).mkdir()) {
        LOGGER.debug(String.format("Failed to perform mkdir for path : %s", newPath));
      }
      final Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();

      BufferedInputStream is = null;
      FileOutputStream fos = null;
      BufferedOutputStream dest = null;
      try {
        // Process each entry
        while (zipFileEntries.hasMoreElements()) {
          // grab a zip file entry
          final ZipEntry entry = zipFileEntries.nextElement();
          final String currentEntry = entry.getName();
          final File destFile = new File(newPath, currentEntry);
          final File destinationParent = destFile.getParentFile();

          // create the parent directory structure if needed
          if (destinationParent.mkdirs()) {
            LOGGER.debug(String.format("Failed to perform mkdirs for path : %s", destinationParent.getAbsolutePath()));
          }

          if (!entry.isDirectory()) {
            is = new BufferedInputStream(zipFile.getInputStream(entry));
            int currentByte;
            // establish buffer for writing file
            final byte data[] = new byte[buffer];

            // write the current file to disk
            fos = new FileOutputStream(destFile);
            dest = new BufferedOutputStream(fos, buffer);

            // read and write until last byte is encountered
            while ((currentByte = is.read(data, 0, buffer)) != -1) {
              dest.write(data, 0, currentByte);
            }
            dest.flush();
          }

          if (currentEntry.endsWith(".zip")) {
            // found a zip file, try to open
            extractZipFile(destFile);
          }
        }
      } catch (final IOException e) {
        LOGGER.debug(e.toString(), e);
      } finally {
        closeQuietly(is);
        closeQuietly(fos);
        closeQuietly(dest);
      }
    } catch (final IOException e) {
      throw new MELMException("Failed to process zip file", e);
    } finally {
      closeQuietly(zipFile);
    }
  }

  private static void closeQuietly(final Closeable closeableMaybeNull) {
    if (closeableMaybeNull != null) {
      try {
        closeableMaybeNull.close();
      } catch (final IOException ex) {
        // ignore
      }
    }
  }

}
