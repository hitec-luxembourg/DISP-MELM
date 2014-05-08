package lu.hitec.pssu.melm.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lu.hitec.pssu.mapelement.library.xml.BaseNodeType;
import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.LibraryValidatorException;
import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.dao.MapElementIconDAO;
import lu.hitec.pssu.melm.persistence.dao.MapElementLibraryDAO;
import lu.hitec.pssu.melm.persistence.dao.MapElementLibraryIconDAO;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.utils.LibraryValidator;
import lu.hitec.pssu.melm.utils.MELMUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MELMServiceImpl implements MELMService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MELMServiceImpl.class);

  private final File iconsDirectory;

  private final File librariesDirectory;

  @Autowired
  private MapElementIconDAO mapElementIconDAO;

  @Autowired
  private MapElementLibraryDAO mapElementLibraryDAO;

  @Autowired
  private MapElementLibraryIconDAO mapElementLibraryIconDAO;

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
    this.iconsDirectory = iconsDirectory;
  }

  @Override
  @Transactional
  public MapElementIcon addIconAndFiles(@Nonnull final String displayName, @Nonnull final File largeIconFile) throws MELMException {
    assert displayName != null : "Display name is null";
    assert largeIconFile != null : "Large icon is null";
    String hashForLargeFile;
    try {
      hashForLargeFile = MELMUtils.getHashForFile(largeIconFile);
    } catch (final IOException e) {
      final String msg = "Failed to compute hash";
      throw new MELMException(msg, e);
    }
    if (mapElementIconDAO.exist(hashForLargeFile, largeIconFile.length())) {
      final String msg = "Adding icon and files aborted because the icon exists";
      LOGGER.warn(msg);
      throw new MELMException(msg);
    }
    final MapElementIcon mapElementIcon = mapElementIconDAO.addMapElementIcon(hashForLargeFile, largeIconFile.length(), displayName);

    FileInputStream fileInputStream = null;
    try {
      copyFile(mapElementIcon, largeIconFile, IconSize.LARGE);
      fileInputStream = new FileInputStream(largeIconFile);
      final byte[] fileData = IOUtils.toByteArray(fileInputStream);
      final File mediumIconFile = File.createTempFile("fromUpload", null);
      FileUtils.writeByteArrayToFile(mediumIconFile, MELMUtils.scaleImage(fileData, 60, 60));
      copyFile(mapElementIcon, mediumIconFile, IconSize.MEDIUM);
      final File smallIconFile = File.createTempFile("fromUpload", null);
      FileUtils.writeByteArrayToFile(smallIconFile, MELMUtils.scaleImage(fileData, 40, 40));
      copyFile(mapElementIcon, smallIconFile, IconSize.SMALL);
      final File tinyIconFile = File.createTempFile("fromUpload", null);
      FileUtils.writeByteArrayToFile(tinyIconFile, MELMUtils.scaleImage(fileData, 20, 20));
      copyFile(mapElementIcon, tinyIconFile, IconSize.TINY);
    } catch (final IOException e) {
      final String msg = "Failed to copy a file";
      throw new MELMException(msg, e);
    } finally {
      MELMUtils.closeResource(fileInputStream);
    }
    return mapElementIcon;
  }

  @Override
  @Transactional
  public MapElementLibrary addLibrary(@Nonnull final String libraryName, @Nonnull final String version, @Nonnull final String iconMd5)
      throws MELMException {
    assert libraryName != null : "libraryName is null";
    assert version != null : "version is null";
    assert iconMd5 != null : "iconMd5 is null";

    final int majorVersion = MELMUtils.getMajorVersion(version);
    final int minorVersion = MELMUtils.getMinorVersion(version);
    try {
      mapElementLibraryDAO.getMapElementLibrary(libraryName, majorVersion, minorVersion);
      final String msg = String.format("Library with name %s and version %d.%d already exist", libraryName, majorVersion, minorVersion);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(msg);
      }
      throw new MELMException(msg);
    } catch (final javax.persistence.NoResultException e) {
      return mapElementLibraryDAO.addMapElementLibrary(libraryName, majorVersion, minorVersion, iconMd5);
    }
  }

  @Override
  public String addLibraryIcon(@Nonnull final File sourceIconFile) throws MELMException {
    assert sourceIconFile != null : "sourceIconFile is null";
    final File libraryIconFolder = new File(librariesDirectory, "icons");
    try {
      final String hashForFile = MELMUtils.getHashForFile(sourceIconFile);
      final MapElementLibrary tempMapElementLibrarHelper = new MapElementLibrary();
      tempMapElementLibrarHelper.setIconMd5(hashForFile);
      final String iconPath = tempMapElementLibrarHelper.getIconPath();
      final File targetIconFile = new File(libraryIconFolder, iconPath);
      if (targetIconFile.getParentFile().mkdirs()) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Parent folders were created");
        }
      }
      FileUtils.copyFile(sourceIconFile, targetIconFile);
      return hashForFile;
    } catch (final IOException e) {
      final String msg = "Failed to compute the hash and move the files";
      throw new MELMException(msg, e);
    }
  }

  @Override
  public void addLibraryIcon(@Nonnull final String libraryName, @Nonnull final String majorVersion, @Nonnull final String minorVersion,
      @Nonnull final String iconIndex, @Nonnull final String iconName, @Nonnull final String iconDescription, @Nonnull final String iconId)
      throws MELMException {
    final MapElementIcon mapElementIcon = mapElementIconDAO.getMapElementIcon(Long.parseLong(iconId));
    final MapElementLibrary library = getLibrary(libraryName, Integer.parseInt(majorVersion), Integer.parseInt(minorVersion));
    if (mapElementLibraryIconDAO.checkIconInLibrary(library, mapElementIcon)) {
      final String msg = String.format("Icon %s is already linked to this library", mapElementIcon.getDisplayName());
      throw new MELMException(msg);
    }
    mapElementLibraryIconDAO.addIconToLibrary(library, mapElementIcon, Integer.parseInt(iconIndex), iconName, iconDescription);
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
  @Transactional
  public void deleteIconAndFiles(final long id) throws MELMException {
    final MapElementIcon mapElementIcon = mapElementIconDAO.getMapElementIcon(id);
    if (mapElementLibraryIconDAO.checkIconInLibrary(mapElementIcon)) {
      // FIXME we should tell to which libraries the icon is linked.
      final String msg = String.format("Icon %s is still linked to some libraries", mapElementIcon.getDisplayName());
      throw new MELMException(msg);
    }
    mapElementIconDAO.delete(id);
    deleteFile(mapElementIcon, IconSize.LARGE);
    deleteFile(mapElementIcon, IconSize.MEDIUM);
    deleteFile(mapElementIcon, IconSize.SMALL);
    deleteFile(mapElementIcon, IconSize.TINY);
  }

  @Override
  @Transactional
  public void deleteLibrary(@Nonnull final String libraryName, final int majorVersion, final int minorVersion) {
    final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary(libraryName, majorVersion, minorVersion);
    final List<MapElementLibraryIcon> libraryIcons = mapElementLibraryIconDAO.getIconsInLibrary(library);
    for (final MapElementLibraryIcon libraryIcon : libraryIcons) {
      mapElementLibraryIconDAO.removeIconFromLibrary(library, libraryIcon.getIcon());
    }
    mapElementLibraryDAO.deleteMapElementLibrary(libraryName, majorVersion, minorVersion);
  }

  @Override
  @Transactional
  public void deleteLibraryIcon(@Nonnull final String libraryName, final int majorVersion, final int minorVersion, final long iconId) {
    final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary(libraryName, majorVersion, minorVersion);
    final MapElementIcon icon = mapElementIconDAO.getMapElementIcon(iconId);
    mapElementLibraryIconDAO.removeIconFromLibrary(library, icon);
  }

  @CheckReturnValue
  @Override
  public File extractImportedLibrary(@Nonnull final File file) throws MELMException {
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

  @Override
  public File generateZipFile(@Nonnull final File zipFolder) throws MELMException {
    assert zipFolder != null : "zipFolder is null";
    assert zipFolder.exists();
    assert zipFolder.isDirectory();
    final ZipService zipService = new ZipService(zipFolder.getAbsolutePath());
    zipService.generateFileList(zipFolder);
    final File result = new File(zipFolder, "library.zip");
    zipService.zipIt(result.getAbsolutePath());
    return result;
  }

  @Override
  public MapElementIcon getIcon(final long id) {
    return mapElementIconDAO.getMapElementIcon(id);
  }

  @Override
  public File getIconFile(final long id, @Nonnull final String size) {
    assert size != null : "size is null";
    final MapElementIcon mapElementIcon = getIcon(id);
    final String filePath = mapElementIcon.getFilePath(IconSize.valueOf(size));
    return new File(iconsDirectory, filePath);
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
  public MapElementLibrary getLibrary(@Nonnull final String libraryName, final int majorVersion, final int minorVersion) {
    return mapElementLibraryDAO.getMapElementLibrary(libraryName, majorVersion, minorVersion);
  }

  @Override
  public File getLibraryIconFile(@Nonnull final String libraryName, final int majorVersion, final int minorVersion) {
    assert libraryName != null : "libraryName is null";
    final MapElementLibrary library = getLibrary(libraryName, majorVersion, minorVersion);
    final String filePath = library.getIconPath();
    final File libraryIconFolder = new File(librariesDirectory, "icons");
    return new File(libraryIconFolder, filePath);
  }

  @Override
  public List<MapElementLibraryIcon> getLibraryIcons(@Nonnull final String libraryName, final int majorVersion, final int minorVersion) {
    final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary(libraryName, majorVersion, minorVersion);
    return mapElementLibraryIconDAO.getIconsInLibrary(library);
  }

  @Override
  public File getTargetArchiveFile(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";
    final File archiveDirectory = LibraryValidator.buildDirectoryForLibraryVersion(
        new File(librariesDirectory, "imported").getAbsolutePath(), libraryName, version);
    if (archiveDirectory == null) {
      throw new MELMException("Failed to create target folder to store new file");
    }

    final String newArchiveFilename = buildArchiveFilename(libraryName, version);
    final File targetArchiveFile = new File(archiveDirectory, newArchiveFilename);

    return targetArchiveFile;
  }

  @Override
  public File importLibrary(@Nonnull final String libraryName, @Nonnull final String version, @Nonnull final File libraryFile)
      throws MELMException {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";
    assert libraryFile != null : "Library file is null";
    final File targetArchiveFile = getTargetArchiveFile(libraryName, version);

    if (targetArchiveFile.isFile()) {
      LOGGER.warn(String.format("Target file for picture : %s exists, will be overwritten", targetArchiveFile.getName()));
      if (!targetArchiveFile.delete()) {
        LOGGER.debug(String.format("Could not delete file : %s", targetArchiveFile.getAbsolutePath()));
      }
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(String.format("About to copy tmp. archive file %s to %s", libraryFile.getAbsolutePath(),
          targetArchiveFile.getAbsolutePath()));
    }
    FileOutputStream out = null;
    FileInputStream in = null;
    try {
      in = new FileInputStream(libraryFile);
      out = new FileOutputStream(targetArchiveFile);
      IOUtils.copy(in, out);
      if (LOGGER.isDebugEnabled()) {
        LOGGER
            .debug(String.format("Copied tmp. archive file %s to %s", libraryFile.getAbsolutePath(), targetArchiveFile.getAbsolutePath()));
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

  @Override
  public List<MapElementIcon> listAllIcons() {
    return mapElementIconDAO.listAllIcons();
  }

  @Override
  public List<MapElementLibrary> listAllLibraries() {
    return mapElementLibraryDAO.listAllLibraries();
  }

  @Override
  @Transactional
  public void moveImportedIcons(@Nonnull final MapElementLibrary mapElementLibrary, @Nonnull final XMLSelectionPathParser libraryParser,
      @Nonnull final File libraryFolder) throws MELMException {
    assert mapElementLibrary != null : "mapElementLibrary si null";
    assert libraryParser != null : "libraryParser is null";
    assert libraryFolder != null : "libraryFolder is null";
    final File largeFileFolder = new File(libraryFolder, IconSize.LARGE.getSize());
    try {
      final Map<String, BaseNodeType> mapOfNodesByUniqueCode = libraryParser.getMapOfNodesByUniqueCode();
      final Iterator<Map.Entry<String, BaseNodeType>> iterator = mapOfNodesByUniqueCode.entrySet().iterator();
      int i = 0;
      while (iterator.hasNext()) {
        final Map.Entry<String, BaseNodeType> mapEntry = iterator.next();
        final String fileName = mapEntry.getValue().getElement().getPoint().getIcon().getFile();
        final File sourceIconLargeFile = new File(largeFileFolder, String.format("%s.png", fileName));
        final String hashForLargeFile = MELMUtils.getHashForFile(sourceIconLargeFile);
        if (!mapElementIconDAO.exist(hashForLargeFile, sourceIconLargeFile.length())) {
          final MapElementIcon mapElementIcon = mapElementIconDAO.addMapElementIcon(hashForLargeFile, sourceIconLargeFile.length(),
              fileName);
          for (final IconSize iconSize : IconSize.values()) {
            moveImportedFile(libraryFolder, sourceIconLargeFile.getName(), iconSize, mapElementIcon);
          }
          mapElementLibraryIconDAO.addIconToLibrary(mapElementLibrary, mapElementIcon, i++, fileName, mapEntry.getValue().getDescription());
        } else {
          final MapElementIcon mapElementIcon = mapElementIconDAO.getMapElementIcon(hashForLargeFile, sourceIconLargeFile.length());
          mapElementLibraryIconDAO.addIconToLibrary(mapElementLibrary, mapElementIcon, i++, fileName, mapEntry.getValue().getDescription());
        }
      }
    } catch (final IOException e) {
      final String msg = "Failed to compute the hash and move the files";
      throw new MELMException(msg, e);
    }
  }

  @Override
  public String moveImportedLibraryIcon(@Nonnull final XMLSelectionPathParser libraryParserlibraryIconRelativePath,
      @Nonnull final File libraryFolder) throws MELMException {
    assert libraryParserlibraryIconRelativePath != null : "libraryParserlibraryIconRelativePath is null";
    assert libraryFolder != null : "libraryFolder is null";
    final File sourceIconFile = new File(libraryFolder, libraryParserlibraryIconRelativePath.getLibraryIconRelativePath());
    final File libraryIconFolder = new File(librariesDirectory, "icons");
    try {
      final String hashForFile = MELMUtils.getHashForFile(sourceIconFile);
      final MapElementLibrary tempMapElementLibrarHelper = new MapElementLibrary();
      tempMapElementLibrarHelper.setIconMd5(hashForFile);
      final String iconPath = tempMapElementLibrarHelper.getIconPath();
      final File targetIconFile = new File(libraryIconFolder, iconPath);
      if (targetIconFile.getParentFile().mkdirs()) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Parent folders were created");
        }
      }
      FileUtils.copyFile(sourceIconFile, targetIconFile);
      return hashForFile;
    } catch (final IOException e) {
      final String msg = "Failed to compute the hash and move the files";
      throw new MELMException(msg, e);
    }
  }

  @Override
  public File prepareZipFile(@Nonnull final String name, final int majorVersion, final int minorVersion) throws MELMException {
    final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    try {
      final DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

      // Define root element.
      final Document document = documentBuilder.newDocument();
      final Element rootElement = document.createElement("elements");
      document.appendChild(rootElement);

      /*
       * <description>
       * <library-type>points</library-type>
       * <library-version>1.1</library-version>
       * <library-name>emergency.lu</library-name>
       * <library-display-name>emergency.lu</library-display-name>
       * <library-icon file="icon.png" />
       * </description>
       */

      final MapElementLibrary mapElementLibrary = mapElementLibraryDAO.getMapElementLibrary(name, majorVersion, minorVersion);
      final File generatedFolder = new File(librariesDirectory, "generated");
      final File libraryRootFolder = new File(generatedFolder, String.format("%s/%s.%s/%s-%s.%s", mapElementLibrary.getName(),
          mapElementLibrary.getMajorVersion(), mapElementLibrary.getMinorVersion(), mapElementLibrary.getName(),
          mapElementLibrary.getMajorVersion(), mapElementLibrary.getMinorVersion()));
      FileUtils.deleteQuietly(libraryRootFolder);
      if (!libraryRootFolder.mkdirs()) {
        LOGGER.debug("Failed to perform mkdir for libraryRootFolder");
      }

      final Element descriptionElement = document.createElement("description");
      rootElement.appendChild(descriptionElement);
      descriptionElement.appendChild(createTextElement(document, "library-type", "points"));
      descriptionElement.appendChild(createTextElement(document, "library-version",
          String.format("%s.%s", mapElementLibrary.getMajorVersion(), mapElementLibrary.getMinorVersion())));
      descriptionElement.appendChild(createTextElement(document, "library-name", mapElementLibrary.getName()));
      descriptionElement.appendChild(createTextElement(document, "library-display-name", mapElementLibrary.getName()));
      final Element libraryIconElement = document.createElement("library-icon");
      libraryIconElement.setAttribute("file", String.format("%s.png", mapElementLibrary.getIconMd5()));
      descriptionElement.appendChild(libraryIconElement);

      // Copy library icon.
      final File libraryIconFolder = new File(librariesDirectory, "icons");
      final File sourceLibraryIconFile = new File(libraryIconFolder, mapElementLibrary.getIconPath());
      final File targetLibraryIconFile = new File(libraryRootFolder, String.format("%s.png", mapElementLibrary.getIconMd5()));
      FileUtils.copyFile(sourceLibraryIconFile, targetLibraryIconFile);

      final Element choiceElement = document.createElement("choice");
      choiceElement.setAttribute("choice-var", "Type");
      rootElement.appendChild(choiceElement);

      /*
       * <node hierarchy-code="1.1" unique-code="Accomodation" choice-value="Accomodation" description="Accomodation">
       * <element description="Accomodation">
       * <point>
       * <icon file="Accomodation" anchor="NE" />
       * </point>
       * </element>
       * </node>
       */

      final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(mapElementLibrary);
      for (final MapElementLibraryIcon mapElementLibraryIcon : iconsInLibrary) {
        final Element nodeElement = document.createElement("node");
        choiceElement.appendChild(nodeElement);
        nodeElement.setAttribute("hierarchy-code", new Integer(mapElementLibraryIcon.getIndexOfIconInLibrary()).toString());
        nodeElement.setAttribute("unique-code", mapElementLibraryIcon.getIconNameInLibrary());
        nodeElement.setAttribute("choice-value", mapElementLibraryIcon.getIconNameInLibrary());
        nodeElement.setAttribute("description", mapElementLibraryIcon.getIconDescriptionInLibrary());
        final Element nodeInnerElement = document.createElement("element");
        nodeElement.appendChild(nodeInnerElement);
        nodeInnerElement.setAttribute("description", mapElementLibraryIcon.getIconNameInLibrary());
        final Element nodePointElement = document.createElement("point");
        nodeInnerElement.appendChild(nodePointElement);
        final Element nodeIconElement = document.createElement("icon");
        nodePointElement.appendChild(nodeIconElement);

        final MapElementIcon icon = mapElementLibraryIcon.getIcon();
        nodeIconElement.setAttribute("file", icon.getPic100pxMd5());
        nodeIconElement.setAttribute("anchor", "NE");

        for (final IconSize iconSize : IconSize.values()) {
          final File sourceIconFile = new File(iconsDirectory, icon.getFilePath(iconSize));
          final File libraryIconSizeFolder = new File(libraryRootFolder, iconSize.getSize());
          if (!libraryIconSizeFolder.mkdirs()) {
            LOGGER.debug("Failed to perform mkdir for libraryIconSizeFolder");
          }
          final File targetIconFile = new File(libraryIconSizeFolder, String.format("%s.png", icon.getPic100pxMd5()));
          FileUtils.copyFile(sourceIconFile, targetIconFile);
        }
      }

      // Creating and writing to xml file.
      final TransformerFactory transformerFactory = TransformerFactory.newInstance();
      final Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
      final DOMSource domSource = new DOMSource(document);
      final StreamResult streamResult = new StreamResult(new File(libraryRootFolder, String.format("%s-%s.%s.xml", name, majorVersion,
          minorVersion)));

      transformer.transform(domSource, streamResult);

      return libraryRootFolder;

    } catch (final ParserConfigurationException e) {
      final String msg = "Failed to generate zip file";
      throw new MELMException(msg, e);
    } catch (final TransformerException e) {
      final String msg = "Failed to generate zip file";
      throw new MELMException(msg, e);
    } catch (final IOException e) {
      final String msg = "Failed to generate zip file";
      throw new MELMException(msg, e);
    }
  }

  @Override
  public void updateLibrary(@Nonnull final String id, @Nonnull final String libraryName, @Nonnull final String version,
      final String iconMd5MaybeNull) throws MELMException {
    final int majorVersion = MELMUtils.getMajorVersion(version);
    final int minorVersion = MELMUtils.getMinorVersion(version);
    mapElementLibraryDAO.updateMapElementLibrary(Long.parseLong(id), libraryName, majorVersion, minorVersion, iconMd5MaybeNull);
  }

  @Override
  public XMLSelectionPathParser validateAndParseImportedLibrary(@Nonnull final String libraryName, @Nonnull final String version)
      throws MELMException {
    assert libraryName != null : "Library name is null";
    assert version != null : "Version is null";

    final String xsdPath = this.getClass().getResource(LibraryValidator.XSD_PATH).getPath();
    final File xmlFile;
    try {
      xmlFile = LibraryValidator.validateLibrary(xsdPath, new File(librariesDirectory, "imported").getAbsolutePath(), libraryName, version);
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

  private void copyFile(@Nonnull final MapElementIcon mapElementIcon, @Nonnull final File file, @Nonnull final IconSize size)
      throws IOException {
    assert mapElementIcon != null : "mapElementIcon is null";
    assert file != null : "File is null";
    assert size != null : "Size is null";
    final File targetIconFile = new File(iconsDirectory, mapElementIcon.getFilePath(size));
    if (targetIconFile.getParentFile().mkdirs()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Parent folders were created");
      }
    }
    FileUtils.copyFile(file, targetIconFile);
  }

  private void moveImportedFile(@Nonnull final File libraryFolder, @Nonnull final String fileNameWithExtension,
      @Nonnull final IconSize size, @Nonnull final MapElementIcon mapElementIcon) throws IOException {
    assert libraryFolder != null : "Library folder is null";
    assert fileNameWithExtension != null : "File name with extension is null";
    assert size != null : "Size is null";
    assert mapElementIcon != null : "mapElementIcon is null";
    final File sourceIconFolder = new File(libraryFolder, size.getSize());
    final File sourceIconFile = new File(sourceIconFolder, fileNameWithExtension);
    final File targetIconFile = new File(iconsDirectory, mapElementIcon.getFilePath(size));
    if (targetIconFile.getParentFile().mkdirs()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Parent folders were created");
      }
    }
    FileUtils.copyFile(sourceIconFile, targetIconFile);
  }

  private static Element createTextElement(@Nonnull final Document document, @Nonnull final String key, @Nonnull final String value) {
    final Element element = document.createElement(key);
    element.appendChild(document.createTextNode(value));
    return element;
  }

  private static void deleteFile(@Nonnull final MapElementIcon mapElementIcon, @Nonnull final IconSize size) {
    assert mapElementIcon != null : "mapElementIcon si null";
    assert size != null : "Size is null";
    final String filePath = mapElementIcon.getFilePath(size);
    final File file = new File(filePath);
    FileUtils.deleteQuietly(file);
  }

  public enum IconSize {
    LARGE("100px"),

    MEDIUM("60px"),

    SMALL("40px"),

    TINY("20px");

    private final String size;

    private IconSize(final String size) {
      this.size = size;
    }

    public String getSize() {
      return size;
    }

    public String getSuffix() {
      return String.format("-%s", size);
    }
  }

}
