package lu.hitec.pssu.melm.services;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import lu.hitec.pssu.melm.exceptions.LibraryValidatorException;
import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.dao.MapElementCustomPropertyDAO;
import lu.hitec.pssu.melm.persistence.dao.MapElementIconDAO;
import lu.hitec.pssu.melm.persistence.dao.MapElementLibraryDAO;
import lu.hitec.pssu.melm.persistence.dao.MapElementLibraryIconDAO;
import lu.hitec.pssu.melm.persistence.entity.MapElementCustomProperty;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.utils.CustomPropertyType;
import lu.hitec.pssu.melm.utils.LibraryValidator;
import lu.hitec.pssu.melm.utils.MELMUtils;
import lu.hitec.pssu.melm.utils.MapElementIconAnchor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MELMServiceImpl implements MELMService {

  public final static String XPATH_LIBRARY_ELEMENTS_CUSTOM_PROPERTY_EXPRESSION = "element/customProperty";

  public final static String XPATH_LIBRARY_ELEMENTS_EXPRESSION = "elements/choice/node";

  public final static String XPATH_LIBRARY_ELEMENTS_ICON_EXPRESSION = "element/point/icon";

  public final static String XPATH_LIBRARY_ICON_EXPRESSION = "elements/description/library-icon";

  public static final String XSD_EXPORT_PATH = "/lu/hitec/pssu/melm/utils/xsd/mapelement-hierarchy-export.xsd";

  private static final Logger LOGGER = LoggerFactory.getLogger(MELMServiceImpl.class);

  private final File iconsDirectory;

  private final File librariesDirectory;

  @Autowired
  private MapElementCustomPropertyDAO mapElementCustomPropertyDAO;

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
  public MapElementIcon addIconAndFiles(@Nonnull final String displayName, @Nonnull final MapElementIconAnchor anchor,
      @Nonnull final File largeIconFile, final File largeIconSelectedFile_) throws MELMException {
    assert displayName != null : "display name is null";
    assert anchor != null : "anchor is null";
    assert displayName.length() != 0 : "display name is empty";
    assert largeIconFile != null : "large icon is null";
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
    final MapElementIcon mapElementIcon = mapElementIconDAO
        .addMapElementIcon(hashForLargeFile, largeIconFile.length(), displayName, anchor);

    try {
      final BufferedImage originalImage = ImageIO.read(largeIconFile);
      BufferedImage selectedImage = null;
      File largeIconSelectedFile = null;
      if (null == largeIconSelectedFile_) {
        largeIconSelectedFile = File.createTempFile("selected", IconSize.LARGE.name());
        ImageIO.write(MELMUtils.createSelectedImage(originalImage, 100, 100), "png", largeIconSelectedFile);
        selectedImage = ImageIO.read(largeIconSelectedFile);
      } else {
        largeIconSelectedFile = largeIconSelectedFile_;
        selectedImage = ImageIO.read(largeIconSelectedFile);
      }
      copyFile(mapElementIcon, largeIconFile, largeIconSelectedFile, IconSize.LARGE);

      final File mediumIconFile = File.createTempFile("fromUpload", IconSize.MEDIUM.name());
      ImageIO.write(MELMUtils.resizeImageWithHint(originalImage, 60, 60), "png", mediumIconFile);
      final File mediumIconSelectedFile = File.createTempFile("selected", IconSize.MEDIUM.name());
      ImageIO.write(MELMUtils.resizeImageWithHint(selectedImage, 60, 60), "png", mediumIconSelectedFile);
      copyFile(mapElementIcon, mediumIconFile, mediumIconSelectedFile, IconSize.MEDIUM);

      final File smallIconFile = File.createTempFile("fromUpload", IconSize.SMALL.name());
      ImageIO.write(MELMUtils.resizeImageWithHint(originalImage, 40, 40), "png", smallIconFile);
      final File smallIconSelectedFile = File.createTempFile("selected", IconSize.SMALL.name());
      ImageIO.write(MELMUtils.resizeImageWithHint(selectedImage, 40, 40), "png", smallIconSelectedFile);
      copyFile(mapElementIcon, smallIconFile, smallIconSelectedFile, IconSize.SMALL);

      final File tinyIconFile = File.createTempFile("fromUpload", IconSize.TINY.name());
      ImageIO.write(MELMUtils.resizeImageWithHint(originalImage, 20, 20), "png", tinyIconFile);
      final File tinyIconSelectedFile = File.createTempFile("selected", IconSize.TINY.name());
      ImageIO.write(MELMUtils.resizeImageWithHint(selectedImage, 20, 20), "png", tinyIconSelectedFile);
      copyFile(mapElementIcon, tinyIconFile, tinyIconSelectedFile, IconSize.TINY);
    } catch (final IOException e) {
      final String msg = "Failed to copy a file";
      throw new MELMException(msg, e);
    }
    return mapElementIcon;
  }

  @Override
  @Transactional
  public MapElementLibrary addLibrary(@Nonnull final String libraryName, final int majorVersion, final int minorVersion,
      @Nonnull final String iconMd5) throws MELMException {
    assert libraryName != null : "library name is null";
    assert libraryName.length() != 0 : "library name is empty";
    assert iconMd5 != null : "iconMd5 is null";
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
    assert sourceIconFile != null : "source icon file is null";
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
  public void addLibraryIcon(final long id, final int iconIndex, @Nonnull final String iconName, @Nonnull final String iconDescription,
      final long iconId) throws MELMException {
    final MapElementIcon mapElementIcon = mapElementIconDAO.getMapElementIcon(iconId);
    final MapElementLibrary library = getLibrary(id);
    if (mapElementLibraryIconDAO.checkIconInLibrary(library, mapElementIcon)) {
      final String msg = String.format("Icon %s is already linked to this library", mapElementIcon.getDisplayName());
      throw new MELMException(msg);
    }
    int indexToSupply = iconIndex;
    if (-1 == iconIndex) {
      final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
      if ((null != iconsInLibrary) && (0 < iconsInLibrary.size())) {
        final MapElementLibraryIcon mapElementLibraryIcon = iconsInLibrary.get(iconsInLibrary.size() - 1);
        indexToSupply = mapElementLibraryIcon.getIndexOfIconInLibrary() + MELMService.ORDER_INCREMENT;
      } else {
        indexToSupply = MELMService.ORDER_INCREMENT;
      }
    }
    mapElementLibraryIconDAO.addIconToLibrary(library, mapElementIcon, indexToSupply, iconName, iconDescription);
  }

  @Override
  public void addProperty(final long id, @Nonnull final String uniqueName, @Nonnull final CustomPropertyType type) throws MELMException {
    final MapElementLibraryIcon libraryIcon = mapElementLibraryIconDAO.getLibraryIcon(id);
    if (!mapElementCustomPropertyDAO.checkPropertyInIcon(libraryIcon, uniqueName).isEmpty()) {
      final String msg = String.format("Property with name %s is already existing for this element %s", uniqueName,
          libraryIcon.getIconNameInLibrary());
      throw new MELMException(msg);
    }
    mapElementCustomPropertyDAO.addCustomProperty(libraryIcon, uniqueName, type);
  }

  /**
   * Creates a unique filename from the given name and version format which is used to store files, and to find files back again with the
   * given information.
   */
  @Nonnull
  @Override
  public String buildArchiveFilename(@Nonnull final String libraryName, final int majorVersion, final int minorVersion) {
    assert libraryName != null : "library name is null";
    assert libraryName.length() != 0 : "library name is empty";
    return String.format("%s-%s.%s.zip", libraryName, majorVersion, minorVersion);
  }

  @Override
  @Transactional
  public MapElementLibrary cloneLibrary(final long id, @Nonnull final String libraryName, final int majorVersion, final int minorVersion,
      @Nonnull final String iconMd5) throws MELMException {
    assert libraryName != null : "library name is null";
    assert libraryName.length() != 0 : "library name is empty";

    try {
      mapElementLibraryDAO.getMapElementLibrary(libraryName, majorVersion, minorVersion);
      final String msg = String.format("Library with name %s and version %d.%d already exist", libraryName, majorVersion, minorVersion);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(msg);
      }
      throw new MELMException(msg);
    } catch (final javax.persistence.NoResultException e) {
      final MapElementLibrary oldMapElementLibrary = mapElementLibraryDAO.getMapElementLibrary(id);
      final MapElementLibrary newMapElementLibrary = mapElementLibraryDAO.addMapElementLibrary(libraryName, majorVersion, minorVersion,
          iconMd5);
      for (final MapElementLibraryIcon mapElementLibraryIcon : mapElementLibraryIconDAO.getIconsInLibrary(oldMapElementLibrary)) {
        mapElementLibraryIconDAO.addIconToLibrary(newMapElementLibrary, mapElementLibraryIcon.getIcon(),
            mapElementLibraryIcon.getIndexOfIconInLibrary(), mapElementLibraryIcon.getIconNameInLibrary(),
            mapElementLibraryIcon.getIconDescriptionInLibrary());
      }
      return newMapElementLibrary;
    }
  }

  @Override
  @Transactional
  public void deleteCustomProperty(final long id) {
    mapElementCustomPropertyDAO.deleteCustomProperty(id);
  }

  @Override
  @Transactional
  public void deleteIconAndFiles(final long id) throws MELMException {
    final MapElementIcon mapElementIcon = mapElementIconDAO.getMapElementIcon(id);
    final Set<MapElementLibrary> linkedLibraries = mapElementLibraryIconDAO.getLinkedLibraries(mapElementIcon);
    if (!linkedLibraries.isEmpty()) {
      final Set<String> libraryNames = new HashSet<>();
      for (final MapElementLibrary mapElementLibrary : linkedLibraries) {
        libraryNames.add(mapElementLibrary.getName());
      }
      final String msg = String.format("Icon %s is still linked to some libraries %s", mapElementIcon.getDisplayName(), libraryNames);
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
  public void deleteLibrary(final long id) {
    final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary(id);
    final List<MapElementLibraryIcon> libraryIcons = mapElementLibraryIconDAO.getIconsInLibrary(library);
    for (final MapElementLibraryIcon libraryIcon : libraryIcons) {
      deleteLibraryIcon(libraryIcon.getId());
    }
    mapElementLibraryDAO.deleteMapElementLibrary(id);
  }

  @Override
  @Transactional
  public void deleteLibraryIcon(final long id) {
    final MapElementLibraryIcon libraryIcon = mapElementLibraryIconDAO.getLibraryIcon(id);
    final List<MapElementCustomProperty> customProperties = mapElementCustomPropertyDAO.getCustomProperties(libraryIcon);
    for (final MapElementCustomProperty customProperty : customProperties) {
      mapElementCustomPropertyDAO.deleteCustomProperty(customProperty.getId());
    }
    mapElementLibraryIconDAO.deleteLibraryIcon(id);
  }

  @Override
  @Transactional
  public void deleteProperty(final long id) {
    mapElementCustomPropertyDAO.deleteCustomProperty(id);
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
  public File generateXmlAndPrepareZipFile(@Nonnull final String name, final int majorVersion, final int minorVersion) throws MELMException {
    final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    try {
      final DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

      // Define root element.
      final Document document = documentBuilder.newDocument();
      final Element rootElement = document.createElementNS("http://hitec.lu/pss/xsd/mapelement/hierarchy", "elements");
      rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      rootElement.setAttribute("version", "1.0");

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
      final String version = String.format("%s.%s", majorVersion, minorVersion);
      descriptionElement.appendChild(createTextElement(document, "library-version", version));
      descriptionElement.appendChild(createTextElement(document, "library-name", mapElementLibrary.getName()));
      descriptionElement.appendChild(createTextElement(document, "library-display-name", mapElementLibrary.getName()));

      // Copy library icon.
      final File libraryIconFolder = new File(librariesDirectory, "icons");
      final File sourceLibraryIconFile = new File(libraryIconFolder, mapElementLibrary.getIconPath());
      final File targetLibraryIconFile = new File(libraryRootFolder, "icon.png");
      FileUtils.copyFile(sourceLibraryIconFile, targetLibraryIconFile);

      final Element choiceElement = document.createElement("choice");
      choiceElement.setAttribute("choice-var", "Type");
      rootElement.appendChild(choiceElement);

      /*
       * <node hierarchy-code="1.1" unique-code="Accomodation" choice-value="Accomodation" description="Accomodation">
       * <element description="Accomodation">
       * <point>
       * <icon file="Accomodation" anchor="NE" />
       * <customProperty key="number_of_casualties" type="integer" />
       * <customProperty key="comment" type="string" />
       * <customProperty key="time" type="date" />
       * </point>
       * </element>
       * </node>
       */

      final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(mapElementLibrary);
      for (final MapElementLibraryIcon mapElementLibraryIcon : iconsInLibrary) {
        final Element nodeElement = document.createElement("node");
        choiceElement.appendChild(nodeElement);
        nodeElement.setAttribute("unique-code", mapElementLibraryIcon.getIconNameInLibrary());
        nodeElement.setAttribute("choice-value", mapElementLibraryIcon.getIconNameInLibrary());
        nodeElement.setAttribute("description", mapElementLibraryIcon.getIconDescriptionInLibrary());
        final Element nodeInnerElement = document.createElement("element");
        nodeElement.appendChild(nodeInnerElement);
        final Element nodePointElement = document.createElement("point");
        nodeInnerElement.appendChild(nodePointElement);
        final Element nodeIconElement = document.createElement("icon");
        nodePointElement.appendChild(nodeIconElement);

        final MapElementIcon icon = mapElementLibraryIcon.getIcon();
        nodeIconElement.setAttribute("file", mapElementLibraryIcon.getIconNameInLibrary());
        nodeIconElement.setAttribute("anchor", mapElementLibraryIcon.getIcon().getAnchor().name());

        for (final IconSize iconSize : IconSize.values()) {
          final File sourceIconFile = new File(iconsDirectory, icon.getFilePath(iconSize, false));
          final File libraryIconSizeFolder = new File(libraryRootFolder, iconSize.getSize());
          if (!libraryIconSizeFolder.mkdirs()) {
            LOGGER.debug("Failed to perform mkdir for libraryIconSizeFolder");
          }
          final File targetIconFile = new File(libraryIconSizeFolder, String.format("%s.png", mapElementLibraryIcon.getIconNameInLibrary()));
          FileUtils.copyFile(sourceIconFile, targetIconFile);
        }

        final List<MapElementCustomProperty> customProperties = mapElementCustomPropertyDAO.getCustomProperties(mapElementLibraryIcon);
        for (final MapElementCustomProperty customProperty : customProperties) {
          final Element propertyElement = document.createElement("customProperty");
          nodeInnerElement.appendChild(propertyElement);
          propertyElement.setAttribute("key", customProperty.getUniqueName());
          propertyElement.setAttribute("type", customProperty.getType().name().toLowerCase());
        }
      }

      // Creating and writing to xml file.
      final TransformerFactory transformerFactory = TransformerFactory.newInstance();
      final Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
      final DOMSource domSource = new DOMSource(document);
      final File xmlFile = new File(libraryRootFolder, String.format("%s-%s.%s.xml", name, majorVersion, minorVersion));
      final StreamResult streamResult = new StreamResult(xmlFile);
      transformer.transform(domSource, streamResult);

      // Validate the xml.
      final String xsdPath = this.getClass().getResource(XSD_EXPORT_PATH).getPath();
      try {
        LibraryValidator.validateLibrary(xsdPath, xmlFile, name, version);
      } catch (final LibraryValidatorException e) {
        final String msg = "Failed to validate library xml file";
        throw new MELMException(msg, e);
      }
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
    final String filePath = mapElementIcon.getFilePath(IconSize.valueOf(size), false);
    return new File(iconsDirectory, filePath);
  }

  @Nonnull
  @Override
  public File getIconsDirectory() {
    return iconsDirectory;
  }

  @Override
  public File getIconSelectedFile(final long id, @Nonnull final String size) {
    assert size != null : "size is null";
    final MapElementIcon mapElementIcon = getIcon(id);
    final String filePath = mapElementIcon.getFilePath(IconSize.valueOf(size), true);
    return new File(iconsDirectory, filePath);
  }

  @Nonnull
  @Override
  public File getLibrariesDirectory() {
    return librariesDirectory;
  }

  @Override
  public MapElementLibrary getLibrary(@Nonnull final long id) {
    return mapElementLibraryDAO.getMapElementLibrary(id);
  }

  @Override
  public MapElementLibrary getLibrary(@Nonnull final String libraryName, final int majorVersion, final int minorVersion) {
    return mapElementLibraryDAO.getMapElementLibrary(libraryName, majorVersion, minorVersion);
  }

  @Override
  public MapElementLibraryIcon getLibraryIcon(final long id) {
    return mapElementLibraryIconDAO.getLibraryIcon(id);
  }

  @Override
  public File getLibraryIconFile(final long id) {
    final MapElementLibrary library = getLibrary(id);
    final String filePath = library.getIconPath();
    final File libraryIconFolder = new File(librariesDirectory, "icons");
    return new File(libraryIconFolder, filePath);
  }

  @Override
  public List<MapElementLibraryIcon> getLibraryIcons(final long id) {
    final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary(id);
    return mapElementLibraryIconDAO.getIconsInLibrary(library);
  }

  @Override
  public List<MapElementLibraryIcon> getLibraryIcons(@Nonnull final String libraryName, final int majorVersion, final int minorVersion) {
    final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary(libraryName, majorVersion, minorVersion);
    return mapElementLibraryIconDAO.getIconsInLibrary(library);
  }

  @Override
  public Set<MapElementLibrary> getLinkedLibraries(@Nonnull final MapElementIcon mapElementIcon) {
    assert mapElementIcon != null : "map element icon is null";
    return mapElementLibraryIconDAO.getLinkedLibraries(mapElementIcon);
  }

  @Override
  public List<MapElementCustomProperty> getProperties(final long id) {
    final MapElementLibraryIcon libraryIcon = mapElementLibraryIconDAO.getLibraryIcon(id);
    return mapElementCustomPropertyDAO.getCustomProperties(libraryIcon);
  }

  @Override
  public String getSelectedFileName(final String fileNameWithExtension) {
    assert fileNameWithExtension != null;
    return fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf(".")) + "_selected"
        + fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf("."));
  }

  @Override
  public File getTargetArchiveFile(@Nonnull final String libraryName, final int majorVersion, final int minorVersion) throws MELMException {
    assert libraryName != null : "library name is null";
    assert libraryName.length() != 0 : "library name is empty";
    final File archiveDirectory = LibraryValidator.buildDirectoryForLibraryVersion(
        new File(librariesDirectory, "imported").getAbsolutePath(), libraryName, String.format("%s.%s", majorVersion, minorVersion));
    if (archiveDirectory == null) {
      throw new MELMException("Failed to create target folder to store new file");
    }

    final String newArchiveFilename = buildArchiveFilename(libraryName, majorVersion, minorVersion);
    final File targetArchiveFile = new File(archiveDirectory, newArchiveFilename);

    return targetArchiveFile;
  }

  @Override
  public boolean iconsAvailable() {
    return mapElementIconDAO.iconsAvailable();
  }

  @Override
  public File importLibrary(@Nonnull final String libraryName, final int majorVersion, final int minorVersion,
      @Nonnull final File libraryFile) throws MELMException {
    assert libraryName != null : "library name is null";
    assert libraryName.length() != 0 : "library name is empty";
    assert libraryFile != null : "library file is null";
    final File targetArchiveFile = getTargetArchiveFile(libraryName, majorVersion, minorVersion);

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
  public void moveImportedIcons(@Nonnull final MapElementLibrary mapElementLibrary, @Nonnull final NodeList nodeList,
      @Nonnull final File libraryFolder) throws MELMException {
    assert mapElementLibrary != null : "map element library si null";
    assert nodeList != null : "node list is null";
    assert libraryFolder != null : "library folder is null";
    final File largeFileFolder = new File(libraryFolder, IconSize.LARGE.getSize());
    final XPath xPath = XPathFactory.newInstance().newXPath();
    try {
      for (int i = 0; i < nodeList.getLength(); i++) {
        final Node node = nodeList.item(i);
        final Element element = (Element) node;
        final String itemName = element.getAttribute("unique-code");
        assert itemName != null;
        final String itemDescription = element.getAttribute("description");
        assert itemDescription != null;
        final Node subNode = (Node) xPath.compile(XPATH_LIBRARY_ELEMENTS_ICON_EXPRESSION).evaluate(node, XPathConstants.NODE);
        final Element subElement = (Element) subNode;
        final String fileName = subElement.getAttribute("file");
        final String anchor = subElement.getAttribute("anchor");
        assert fileName != null;
        final File sourceIconLargeFile = new File(largeFileFolder, String.format("%s.png", fileName));
        final String hashForLargeFile = MELMUtils.getHashForFile(sourceIconLargeFile);
        if (!mapElementIconDAO.exist(hashForLargeFile, sourceIconLargeFile.length())) {
          final MapElementIcon mapElementIcon = mapElementIconDAO.addMapElementIcon(hashForLargeFile, sourceIconLargeFile.length(),
              fileName, MapElementIconAnchor.valueOf(anchor.toUpperCase()));
          for (final IconSize iconSize : IconSize.values()) {
            moveImportedFile(libraryFolder, sourceIconLargeFile.getName(), iconSize, mapElementIcon);
          }
          final MapElementLibraryIcon libraryIcon = mapElementLibraryIconDAO.addIconToLibrary(mapElementLibrary, mapElementIcon, i,
              itemName, itemDescription);
          final NodeList nodeList2 = (NodeList) xPath.compile(XPATH_LIBRARY_ELEMENTS_CUSTOM_PROPERTY_EXPRESSION).evaluate(node,
              XPathConstants.NODESET);
          for (int j = 0; j < nodeList2.getLength(); j++) {
            final Node subNode2 = nodeList2.item(j);
            final Element subElement2 = (Element) subNode2;
            mapElementCustomPropertyDAO.addCustomProperty(libraryIcon, subElement2.getAttribute("key"),
                CustomPropertyType.valueOf(subElement2.getAttribute("type").toUpperCase()));
          }
        } else {
          final MapElementIcon mapElementIcon = mapElementIconDAO.getMapElementIcon(hashForLargeFile, sourceIconLargeFile.length());
          final MapElementLibraryIcon libraryIcon = mapElementLibraryIconDAO.addIconToLibrary(mapElementLibrary, mapElementIcon, i,
              itemName, itemDescription);
          final NodeList nodeList2 = (NodeList) xPath.compile(XPATH_LIBRARY_ELEMENTS_CUSTOM_PROPERTY_EXPRESSION).evaluate(node,
              XPathConstants.NODESET);
          for (int j = 0; j < nodeList2.getLength(); j++) {
            final Node subNode2 = nodeList2.item(j);
            final Element subElement2 = (Element) subNode2;
            mapElementCustomPropertyDAO.addCustomProperty(libraryIcon, subElement2.getAttribute("key"),
                CustomPropertyType.valueOf(subElement2.getAttribute("type").toUpperCase()));
          }
        }
      }
    } catch (final IOException | XPathExpressionException e) {
      final String msg = "Failed to compute the hash and move the files";
      throw new MELMException(msg, e);
    }
  }

  @Override
  public String moveImportedLibraryIcon(@Nonnull final File libraryFolder, @Nonnull final String libraryName, final int majorVersion,
      final int minorVersion) throws MELMException {
    assert libraryFolder != null : "library folder is null";
    assert libraryName != null : "library name is null";
    assert libraryName.length() != 0 : "library name is empty";

    String iconFileName;
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final XPath xPath = XPathFactory.newInstance().newXPath();
    final String xsdPath = this.getClass().getResource(LibraryValidator.XSD_PATH).getPath();
    try {
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final File xmlFile = LibraryValidator.validateLibrary(xsdPath, new File(librariesDirectory, "imported").getAbsolutePath(),
          libraryName, String.format("%s.%s", majorVersion, minorVersion));
      final Document document = builder.parse(xmlFile);
      final Node node = (Node) xPath.compile(XPATH_LIBRARY_ICON_EXPRESSION).evaluate(document, XPathConstants.NODE);
      final Element element = (Element) node;
      iconFileName = element.getAttribute("file");
    } catch (final ParserConfigurationException | LibraryValidatorException | IOException | SAXException | XPathExpressionException e) {
      final String msg = "Failed to copy library icon file";
      throw new MELMException(msg, e);
    }

    if ((iconFileName == null) || ("").equalsIgnoreCase(iconFileName)) {
      iconFileName = "icon.png";
    }
    final File sourceIconFile = new File(libraryFolder, iconFileName);
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
      final String msg = "Failed to copy library icon file";
      throw new MELMException(msg, e);
    }
  }

  @Override
  public void moveLibraryIcon(final long id, final String which) {
    final MapElementLibraryIcon libraryIcon = mapElementLibraryIconDAO.getLibraryIcon(id);
    LOGGER.debug("*** Moving icon [" + libraryIcon.getId() + "] with actual index order [" + libraryIcon.getIndexOfIconInLibrary() + "] "
        + which);
    MapElementLibraryIcon neighbour = null;
    MapElementLibraryIcon neighbourNext = null;
    int neighbourIndex = 0;
    int neighbourNextIndex = 0;
    int newIndex = 0;
    switch (which) {
    case "up":
      neighbour = mapElementLibraryIconDAO.getPreviousLibraryIcon(libraryIcon);
      if (null != neighbour) {
        neighbourIndex = neighbour.getIndexOfIconInLibrary();
        neighbourNext = mapElementLibraryIconDAO.getPreviousLibraryIcon(neighbour);
      } else {
        // Nothing to do, libraryIcon is already the first one
        return;
      }
      if (null != neighbourNext) {
        neighbourNextIndex = neighbourNext.getIndexOfIconInLibrary();
      } else {
        // libraryIcon needs to be put in first place, nothing to do here as neighbourNextIndex is already set to 0;
      }
      // Is there space in between ?
      newIndex = MELMUtils.getNewIndex(neighbourIndex, neighbourNextIndex);
      if (-1 != newIndex) {
        LOGGER.debug("*** Updating given icon [" + libraryIcon.getId() + "] with index order [" + newIndex + "]");
        mapElementLibraryIconDAO.updateLibraryIcon(libraryIcon.getId(), libraryIcon.getIcon(), newIndex,
            libraryIcon.getIconNameInLibrary(), libraryIcon.getIconDescriptionInLibrary());
      } else {
        // Reorder all icons in this library
        final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(libraryIcon.getLibrary());
        for (int i = 0; i < iconsInLibrary.size(); i++) {
          final MapElementLibraryIcon iconInLibrary = iconsInLibrary.get(i);
          if (iconInLibrary.getId() == libraryIcon.getId()) {
            Collections.swap(iconsInLibrary, i, i - 1);
            break;
          }
        }
        int order = 0;
        for (int i = 0; i < iconsInLibrary.size(); i++) {
          final MapElementLibraryIcon iconInLibrary = iconsInLibrary.get(i);
          order += ORDER_INCREMENT;
          LOGGER.debug("*** Updating icon [" + iconInLibrary.getId() + "] with index order [" + order + "]");
          mapElementLibraryIconDAO.updateLibraryIcon(iconInLibrary.getId(), iconInLibrary.getIcon(), order,
              iconInLibrary.getIconNameInLibrary(), iconInLibrary.getIconDescriptionInLibrary());
        }
      }

      break;

    case "down":
      neighbour = mapElementLibraryIconDAO.getNextLibraryIcon(libraryIcon);
      if (null != neighbour) {
        neighbourIndex = neighbour.getIndexOfIconInLibrary();
        neighbourNext = mapElementLibraryIconDAO.getNextLibraryIcon(neighbour);
      } else {
        // Nothing to do, libraryIcon is already the last one
        return;
      }
      if (null != neighbourNext) {
        neighbourNextIndex = neighbourNext.getIndexOfIconInLibrary();
      } else {
        // libraryIcon needs to be put in last place, neighbourNextIndex is the set to neighbourIndex + twice ORDER_INCREMENT
        neighbourNextIndex += (neighbourIndex + (ORDER_INCREMENT * 2));
      }
      // Is there space in between ?
      newIndex = MELMUtils.getNewIndex(neighbourIndex, neighbourNextIndex);
      if (-1 != newIndex) {
        LOGGER.debug("*** Updating given icon [" + libraryIcon.getId() + "] with index order [" + newIndex + "]");
        mapElementLibraryIconDAO.updateLibraryIcon(libraryIcon.getId(), libraryIcon.getIcon(), newIndex,
            libraryIcon.getIconNameInLibrary(), libraryIcon.getIconDescriptionInLibrary());
      } else {
        // Reorder all icons in this library
        final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(libraryIcon.getLibrary());
        for (int i = 0; i < iconsInLibrary.size(); i++) {
          final MapElementLibraryIcon iconInLibrary = iconsInLibrary.get(i);
          if (iconInLibrary.getId() == libraryIcon.getId()) {
            Collections.swap(iconsInLibrary, i, i + 1);
            break;
          }
        }
        int order = 0;
        for (int i = 0; i < iconsInLibrary.size(); i++) {
          final MapElementLibraryIcon iconInLibrary = iconsInLibrary.get(i);
          order += ORDER_INCREMENT;
          LOGGER.debug("*** Updating icon [" + iconInLibrary.getId() + "] with index order [" + order + "]");
          mapElementLibraryIconDAO.updateLibraryIcon(iconInLibrary.getId(), iconInLibrary.getIcon(), order,
              iconInLibrary.getIconNameInLibrary(), iconInLibrary.getIconDescriptionInLibrary());
        }
      }

      break;

    default:
      break;
    }

  }

  @Override
  @Transactional
  public void updateIconAndFiles(final long id, @Nonnull final String displayName, @Nonnull final MapElementIconAnchor anchor,
      final File largeIconFileMaybeNull) throws MELMException {
    assert displayName != null : "display name is null";
    assert anchor != null : "anchor is null";
    assert displayName.length() != 0 : "display name is empty";

    if (largeIconFileMaybeNull == null) {
      mapElementIconDAO.updateMapElementIcon(id, displayName, anchor);
    } else {
      String hashForLargeFile;
      try {
        hashForLargeFile = MELMUtils.getHashForFile(largeIconFileMaybeNull);
      } catch (final IOException e) {
        final String msg = "Failed to compute hash";
        throw new MELMException(msg, e);
      }
      if (mapElementIconDAO.exist(hashForLargeFile, largeIconFileMaybeNull.length())) {
        final String msg = "Updating icon and files aborted because the icon exists";
        LOGGER.warn(msg);
        throw new MELMException(msg);
      }
      final MapElementIcon mapElementIcon = mapElementIconDAO.updateMapElementIcon(id, hashForLargeFile, largeIconFileMaybeNull.length(),
          displayName, anchor);

      try {
        // FIXME manage selected file. For the moment I have duplicated the non selected variable.
        copyFile(mapElementIcon, largeIconFileMaybeNull, largeIconFileMaybeNull, IconSize.LARGE);
        final BufferedImage originalImage = ImageIO.read(largeIconFileMaybeNull);

        final File mediumIconFile = File.createTempFile("fromUpload", IconSize.MEDIUM.name());
        ImageIO.write(MELMUtils.resizeImageWithHint(originalImage, 60, 60), "png", mediumIconFile);
        copyFile(mapElementIcon, mediumIconFile, mediumIconFile, IconSize.MEDIUM);

        final File smallIconFile = File.createTempFile("fromUpload", IconSize.SMALL.name());
        ImageIO.write(MELMUtils.resizeImageWithHint(originalImage, 40, 40), "png", smallIconFile);
        copyFile(mapElementIcon, smallIconFile, smallIconFile, IconSize.SMALL);

        final File tinyIconFile = File.createTempFile("fromUpload", IconSize.TINY.name());
        ImageIO.write(MELMUtils.resizeImageWithHint(originalImage, 20, 20), "png", tinyIconFile);
        copyFile(mapElementIcon, tinyIconFile, tinyIconFile, IconSize.TINY);
      } catch (final IOException e) {
        final String msg = "Failed to copy a file";
        throw new MELMException(msg, e);
      }
    }
  }

  @Override
  public void updateLibrary(final long id, @Nonnull final String libraryName, final int majorVersion, final int minorVersion,
      final String iconMd5MaybeNull) throws MELMException {
    mapElementLibraryDAO.updateMapElementLibrary(id, libraryName, majorVersion, minorVersion, iconMd5MaybeNull);
  }

  @Override
  public void updateLibraryIcon(final long id, final int iconIndex, @Nonnull final String iconName, @Nonnull final String iconDescription,
      final long iconId) throws MELMException {
    final MapElementIcon newIcon = mapElementIconDAO.getMapElementIcon(iconId);
    final MapElementLibraryIcon libraryIcon = mapElementLibraryIconDAO.getLibraryIcon(id);
    final MapElementIcon oldIcon = libraryIcon.getIcon();
    if ((newIcon.getId() == oldIcon.getId()) || !mapElementLibraryIconDAO.checkIconInLibrary(libraryIcon.getLibrary(), newIcon)) {
      mapElementLibraryIconDAO.updateLibraryIcon(id, newIcon, iconIndex, iconName, iconDescription);
    } else {
      final String msg = String.format("Icon %s is already linked to this library", newIcon.getDisplayName());
      throw new MELMException(msg);
    }
  }

  @Override
  public void updateProperty(final long id, @Nonnull final String uniqueName, @Nonnull final CustomPropertyType type) throws MELMException {
    final MapElementCustomProperty property = mapElementCustomPropertyDAO.getCustomProperty(id);
    final MapElementLibraryIcon mapElementLibraryIcon = property.getMapElementLibraryIcon();
    final List<MapElementCustomProperty> list = mapElementCustomPropertyDAO.checkPropertyInIcon(mapElementLibraryIcon, uniqueName);
    if (list.isEmpty() || (list.get(0).getId() == id)) {
      mapElementCustomPropertyDAO.updateCustomProperty(id, uniqueName, type);
    } else {
      final String msg = String.format("Property with name %s is already existing", uniqueName);
      throw new MELMException(msg);
    }
  }

  @Override
  public NodeList validateImportedLibraryAndGetNodeList(@Nonnull final String libraryName, final int majorVersion, final int minorVersion)
      throws MELMException {
    assert libraryName != null : "library name is null";
    assert libraryName.length() != 0 : "library name is empty";
    final String xsdPath = this.getClass().getResource(LibraryValidator.XSD_PATH).getPath();
    final File xmlFile;
    try {
      xmlFile = LibraryValidator.validateLibrary(xsdPath, new File(librariesDirectory, "imported").getAbsolutePath(), libraryName,
          String.format("%s.%s", majorVersion, minorVersion));
    } catch (final LibraryValidatorException e) {
      final String msg = "Failed to validate library xml file";
      throw new MELMException(msg, e);
    }

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final XPath xPath = XPathFactory.newInstance().newXPath();
    try {
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final Document document = builder.parse(xmlFile);
      return (NodeList) xPath.compile(XPATH_LIBRARY_ELEMENTS_EXPRESSION).evaluate(document, XPathConstants.NODESET);
    } catch (final IOException | SAXException | ParserConfigurationException | XPathExpressionException e) {
      final String msg = "Failed to parse library xml file";
      LOGGER.error(msg, e);
      throw new MELMException(msg, e);
    }
  }

  private void copyFile(@Nonnull final MapElementIcon mapElementIcon, @Nonnull final File file, @Nonnull final File selectedFile,
      @Nonnull final IconSize size) throws IOException {
    assert mapElementIcon != null : "map element icon is null";
    assert file != null : "file is null";
    assert selectedFile != null : "selected file is null";
    assert size != null : "size is null";
    final File targetIconFile = new File(iconsDirectory, mapElementIcon.getFilePath(size, false));
    final File targetIconSelectedFile = new File(iconsDirectory, mapElementIcon.getFilePath(size, true));
    if (targetIconFile.getParentFile().mkdirs()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Parent folders were created");
      }
    }
    FileUtils.copyFile(file, targetIconFile);
    FileUtils.copyFile(selectedFile, targetIconSelectedFile);
  }

  private void moveImportedFile(@Nonnull final File libraryFolder, @Nonnull final String fileNameWithExtension,
      @Nonnull final IconSize size, @Nonnull final MapElementIcon mapElementIcon) throws IOException {
    assert libraryFolder != null : "library folder is null";
    assert fileNameWithExtension != null : "file name with extension is null";
    assert size != null : "size is null";
    assert mapElementIcon != null : "map element icon is null";
    final File sourceIconFolder = new File(libraryFolder, size.getSize());
    final File sourceIconFile = new File(sourceIconFolder, fileNameWithExtension);
    final File sourceIconSelectedFile = new File(sourceIconFolder, getSelectedFileName(fileNameWithExtension));
    final String targetIconFilePath = mapElementIcon.getFilePath(size, false);
    final File targetIconFile = new File(iconsDirectory, targetIconFilePath);

    if (targetIconFile.getParentFile().mkdirs()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Parent folders were created");
      }
    }
    FileUtils.copyFile(sourceIconFile, targetIconFile);

    File targetIconSelectedFile = null;
    if (sourceIconSelectedFile.exists()) {
      targetIconSelectedFile = new File(iconsDirectory, getSelectedFileName(targetIconFilePath));
      FileUtils.copyFile(sourceIconSelectedFile, targetIconSelectedFile);
    }
  }

  private static Element createTextElement(@Nonnull final Document document, @Nonnull final String key, @Nonnull final String value) {
    final Element element = document.createElement(key);
    element.appendChild(document.createTextNode(value));
    return element;
  }

  private static void deleteFile(@Nonnull final MapElementIcon mapElementIcon, @Nonnull final IconSize size) {
    assert mapElementIcon != null : "map element icon si null";
    assert size != null : "size is null";
    final String filePath = mapElementIcon.getFilePath(size, false);
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
