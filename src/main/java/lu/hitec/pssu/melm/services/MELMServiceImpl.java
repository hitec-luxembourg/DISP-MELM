package lu.hitec.pssu.melm.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.LibraryValidatorException;
import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.utils.LibraryValidator;
import lu.hitec.pssu.melm.utils.MELMUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MELMServiceImpl implements MELMService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MELMServiceImpl.class);

  private static final String[] ICON_SIZES = { "20px", "40px", "60px", "100px" };

  private final File iconsDirectory;

  private final File iconsImportedDirectory;

  private final File librariesDirectory;

  private final File librariesImportedDirectory;

  // @Autowired
  // private MapElementIconDAO mapElementIconDAO;

  public MELMServiceImpl(final File librariesDirectory, final File iconsDirectory) {
    if (!librariesDirectory.isDirectory() && !librariesDirectory.mkdirs()) {
      final String msg = String.format("Libraries directory doesn't exist: %s", librariesDirectory.getAbsolutePath());
      throw new IllegalArgumentException(msg);
    }
    if (!iconsDirectory.isDirectory() && !iconsDirectory.mkdirs()) {
      final String msg = String.format("Icons directory doesn't exist: %s", iconsDirectory.getAbsolutePath());
      throw new IllegalArgumentException(msg);
    }
    this.librariesDirectory = librariesDirectory;
    librariesImportedDirectory = new File(librariesDirectory, "imported");
    this.iconsDirectory = iconsDirectory;
    iconsImportedDirectory = new File(iconsDirectory, "imported");
  }

  @Override
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
      MELMUtils.closeResource(out);
      MELMUtils.closeResource(in);
    }
    return targetArchiveFile;
  }

  /**
   * Creates a unique filename from the given name and version format which is used to store files, and to find files back again with the
   * given information.
   */
  @Nonnull
  @Override
  public String buildArchiveFilename(@Nonnull final String libraryName, @Nonnull final String version) {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";
    return String.format("%s-%s.zip", libraryName, version);
  }

  @Override
  public void copyImportedIcons(@Nonnull final File libraryFolder) throws MELMException {
    try {
      for (final String iconSize : ICON_SIZES) {
        final File iconFolder = new File(libraryFolder, iconSize);
        final File[] iconFiles = iconFolder.listFiles();
        for (final File sourceIconFile : iconFiles) {
          final String hashForFile = MELMUtils.getHashForFile(sourceIconFile);
          // FIXME test with DAO and if not existing then insert in DB.
          System.out.println(hashForFile);
          // if (!mapElementIconDAO.exist(hashForFile, iconFile.length())) {
          final File targetIconFile = new File(iconsImportedDirectory, String.format("%s.png", hashForFile));
          FileUtils.copyFile(sourceIconFile, targetIconFile);
          // }
        }
      }
    } catch (final IOException e) {
      final String msg = "Failed to compute the hash";
      throw new MELMException(msg, e);
    }
  }

  @CheckReturnValue
  @Override
  public File extractZipFile(@Nonnull final File file) throws MELMException {
    assert file != null : "File is null";
    assert file.exists() : "File is not existing";
    final int buffer = 2048;

    File libraryRootFolder = null;
    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(file);
      final String newPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4);

      libraryRootFolder = new File(newPath);
      if (!libraryRootFolder.mkdirs()) {
        LOGGER.debug(String.format("Failed to perform mkdir for path : %s", newPath));
      }
      final Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();

      BufferedInputStream is = null;
      FileOutputStream fos = null;
      BufferedOutputStream dest = null;
      try {
        // Process each entry.
        while (zipFileEntries.hasMoreElements()) {
          // Grab a zip file entry.
          final ZipEntry entry = zipFileEntries.nextElement();
          final String currentEntry = entry.getName();
          final File destFile = new File(newPath, currentEntry);
          final File destinationParent = destFile.getParentFile();

          // Create the parent directory structure if needed.
          if (destinationParent.mkdirs()) {
            LOGGER.debug(String.format("Failed to perform mkdirs for path : %s", destinationParent.getAbsolutePath()));
          }

          if (!entry.isDirectory()) {
            is = new BufferedInputStream(zipFile.getInputStream(entry));
            int currentByte;
            // Establish buffer for writing file.
            final byte data[] = new byte[buffer];

            // Write the current file to disk.
            fos = new FileOutputStream(destFile);
            dest = new BufferedOutputStream(fos, buffer);

            // Read and write until last byte is encountered.
            while ((currentByte = is.read(data, 0, buffer)) != -1) {
              dest.write(data, 0, currentByte);
            }
            dest.flush();
          }

          // TODO: it is useful?
          // if (currentEntry.endsWith(".zip")) {
          // // Found a zip file, try to open.
          // extractZipFile(destFile);
          // }
        }
      } catch (final IOException e) {
        final String msg = "Failed to process zip entry";
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug(msg, e);
        }
        throw new MELMException(msg, e);
      } finally {
        MELMUtils.closeResource(is);
        MELMUtils.closeResource(fos);
        MELMUtils.closeResource(dest);
      }
    } catch (final IOException e) {
      final String msg = "Failed to process zip file";
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(msg, e);
      }
      throw new MELMException(msg, e);
    } finally {
      MELMUtils.closeResource(zipFile);
    }
    return libraryRootFolder;
  }

  @Nonnull
  @Override
  public File getIconsDirectory() {
    return iconsDirectory;
  }

  @Nonnull
  @Override
  public File getLibrariesDirectory() {
    return librariesDirectory;
  }

  @Override
  public File getTargetArchiveFile(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";
    final File archiveDirectory = LibraryValidator.buildDirectoryForLibraryVersion(librariesImportedDirectory.getAbsolutePath(),
        libraryName, version);
    if (archiveDirectory == null) {
      throw new MELMException("Failed to create target folder to store new file");
    }

    final String newArchiveFilename = buildArchiveFilename(libraryName, version);
    final File targetArchiveFile = new File(archiveDirectory, newArchiveFilename);

    return targetArchiveFile;
  }

  @Override
  public XMLSelectionPathParser validateAndParse(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";

    final String xsdPath = this.getClass().getResource(LibraryValidator.XSD_PATH).getPath();
    final File xmlFile;
    try {
      xmlFile = LibraryValidator.validateLibrary(xsdPath, librariesImportedDirectory.getAbsolutePath(), libraryName, version);
    } catch (final LibraryValidatorException e) {
      final String msg = "Failed to validate library xml file";
      throw new MELMException(msg, e);
    }

    InputStream in = null;
    try {
      in = new FileInputStream(xmlFile);
      assert in.available() != 0;
      final XMLSelectionPathParser parser = new XMLSelectionPathParser(in);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(String.format("Parse map library: %s, %s", parser.getLibraryName(), parser.getLibraryDisplayName()));
      }
      return parser;
    } catch (final Exception e) {
      final String msg = "Failed to parse library xml file";
      LOGGER.error(msg, e);
    } finally {
      MELMUtils.closeResource(in);
    }
    throw new MELMException("Failed to parse xml file");
  }

}
