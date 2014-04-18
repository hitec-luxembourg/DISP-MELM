package lu.hitec.pssu.melm.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.transaction.Transactional;

import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.LibraryValidatorException;
import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.dao.MapElementIconDAO;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.utils.LibraryValidator;
import lu.hitec.pssu.melm.utils.MELMUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MELMServiceImpl implements MELMService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MELMServiceImpl.class);

	private final File iconsDirectory;

	private final File iconsImportedDirectory;

	private final File librariesDirectory;

	private final File librariesImportedDirectory;

	@Autowired
	private MapElementIconDAO mapElementIconDAO;

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
		this.librariesImportedDirectory = new File(librariesDirectory, "imported");
		this.iconsDirectory = iconsDirectory;
		this.iconsImportedDirectory = new File(iconsDirectory, "imported");
	}

	@Override
	@Transactional
	public void addIconAndFiles(@Nonnull final String displayName, @Nonnull final File largeIconFile) throws MELMException {
		assert displayName != null : "Display name is null";
		assert largeIconFile != null : "Large icon is null";
		String hashForLargeFile;
		try {
			hashForLargeFile = MELMUtils.getHashForFile(largeIconFile);
		} catch (final IOException e) {
			final String msg = "Failed to compute hash";
			throw new MELMException(msg, e);
		}
		if (this.mapElementIconDAO.exist(hashForLargeFile, largeIconFile.length())) {
			final String msg = "Adding icon and files aborted because the icon exists";
			LOGGER.warn(msg);
			throw new MELMException(msg);
		}
		this.mapElementIconDAO.addMapElementIcon(hashForLargeFile, largeIconFile.length(), displayName);

		FileInputStream fileInputStream = null;
		try {
			copyFile(hashForLargeFile, largeIconFile, IconSize.LARGE);
			fileInputStream = new FileInputStream(largeIconFile);
			final byte[] fileData = IOUtils.toByteArray(fileInputStream);
			final File mediumIconFile = File.createTempFile("fromUpload", null);
			FileUtils.writeByteArrayToFile(mediumIconFile, MELMUtils.scaleImage(fileData, 60, 60));
			copyFile(hashForLargeFile, mediumIconFile, IconSize.MEDIUM);
			final File smallIconFile = File.createTempFile("fromUpload", null);
			FileUtils.writeByteArrayToFile(smallIconFile, MELMUtils.scaleImage(fileData, 40, 40));
			copyFile(hashForLargeFile, smallIconFile, IconSize.SMALL);
			final File tinyIconFile = File.createTempFile("fromUpload", null);
			FileUtils.writeByteArrayToFile(tinyIconFile, MELMUtils.scaleImage(fileData, 20, 20));
			copyFile(hashForLargeFile, tinyIconFile, IconSize.TINY);
		} catch (final IOException e) {
			final String msg = "Failed to copy a file";
			throw new MELMException(msg, e);
		} finally {
			MELMUtils.closeResource(fileInputStream);
		}
	}

	/**
	 * Creates a unique filename from the given name and version format which is used to store files, and to find files back again with the given
	 * information.
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
	public void deleteIconAndFiles(final long id) {
		final MapElementIcon mapElementIcon = this.mapElementIconDAO.getMapElementIcon(id);
		final String hash = mapElementIcon.getPic100pxMd5();
		this.mapElementIconDAO.delete(id);
		deleteFile(hash, IconSize.LARGE);
		deleteFile(hash, IconSize.MEDIUM);
		deleteFile(hash, IconSize.SMALL);
		deleteFile(hash, IconSize.TINY);
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
	public MapElementIcon getIcon(final long id) {
		return this.mapElementIconDAO.getMapElementIcon(id);
	}

	@Override
	public File getIconFile(final long id, @Nonnull final String size) {
		assert size != null : "size is null";
		final MapElementIcon mapElementIcon = getIcon(id);
		final String filePath = mapElementIcon.getFilePath(IconSize.valueOf(size));
		return new File(filePath);
	}

	@Nonnull
	@Override
	public File getIconsDirectory() {
		return this.iconsDirectory;
	}

	@Nonnull
	@Override
	public File getLibrariesDirectory() {
		return this.librariesDirectory;
	}

	@Override
	public File getTargetArchiveFile(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException {
		assert libraryName != null : "Library name is null";
		assert version != null : "Version is null";
		final File archiveDirectory = LibraryValidator.buildDirectoryForLibraryVersion(this.librariesImportedDirectory.getAbsolutePath(), libraryName, version);
		if (archiveDirectory == null) {
			throw new MELMException("Failed to create target folder to store new file");
		}

		final String newArchiveFilename = buildArchiveFilename(libraryName, version);
		final File targetArchiveFile = new File(archiveDirectory, newArchiveFilename);

		return targetArchiveFile;
	}

	@Override
	public File importLibrary(@Nonnull final String libraryName, @Nonnull final String version, @Nonnull final File libraryFile) throws MELMException {
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
			LOGGER.debug(String.format("About to copy tmp. archive file %s to %s", libraryFile.getAbsolutePath(), targetArchiveFile.getAbsolutePath()));
		}
		FileOutputStream out = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(libraryFile);
			out = new FileOutputStream(targetArchiveFile);
			IOUtils.copy(in, out);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Copied tmp. archive file %s to %s", libraryFile.getAbsolutePath(), targetArchiveFile.getAbsolutePath()));
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
		return this.mapElementIconDAO.listAllIcons();
	}

	@Override
	public void moveImportedIcons(@Nonnull final File libraryFolder) throws MELMException {
		final File largeFileFolder = new File(libraryFolder, IconSize.LARGE.getSize());
		try {
			final File[] iconFiles = largeFileFolder.listFiles();
			for (final File sourceIconLargeFile : iconFiles) {
				final String hashForLargeFile = MELMUtils.getHashForFile(sourceIconLargeFile);
				if (!this.mapElementIconDAO.exist(hashForLargeFile, sourceIconLargeFile.length())) {
					moveImportedFile(hashForLargeFile, libraryFolder, sourceIconLargeFile.getName(), IconSize.LARGE);
					moveImportedFile(hashForLargeFile, libraryFolder, sourceIconLargeFile.getName(), IconSize.MEDIUM);
					moveImportedFile(hashForLargeFile, libraryFolder, sourceIconLargeFile.getName(), IconSize.SMALL);
					moveImportedFile(hashForLargeFile, libraryFolder, sourceIconLargeFile.getName(), IconSize.TINY);
					this.mapElementIconDAO.addMapElementIcon(hashForLargeFile, sourceIconLargeFile.length(), FilenameUtils.getBaseName(sourceIconLargeFile.getName()));
				}
			}
		} catch (final IOException e) {
			final String msg = "Failed to compute the hash";
			throw new MELMException(msg, e);
		}
	}

	@Override
	public XMLSelectionPathParser validateAndParseImportedLibrary(@Nonnull final String libraryName, @Nonnull final String version) throws MELMException {
		assert libraryName != null : "Library name is null";
		assert version != null : "Version is null";

		final String xsdPath = this.getClass().getResource(LibraryValidator.XSD_PATH).getPath();
		final File xmlFile;
		try {
			xmlFile = LibraryValidator.validateLibrary(xsdPath, this.librariesImportedDirectory.getAbsolutePath(), libraryName, version);
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

	private void moveImportedFile(@Nonnull final String hashForLargeFile, @Nonnull final File libraryFolder, @Nonnull final String fileNameWithExtension,
			@Nonnull final IconSize size) throws IOException {
		assert hashForLargeFile != null : "Hash for large file is null";
		assert libraryFolder != null : "Library folder is null";
		assert fileNameWithExtension != null : "File name with extension is null";
		assert size != null : "Size is null";
		final File sourceIconFolder = new File(libraryFolder, size.getSize());
		final File sourceIconFile = new File(sourceIconFolder, fileNameWithExtension);
		final File targetIconFile = new File(this.iconsImportedDirectory, String.format("%s%s.png", hashForLargeFile, size.getSuffix()));
		FileUtils.copyFile(sourceIconFile, targetIconFile);
	}

	private static void copyFile(@Nonnull final String hashForLargeFile, @Nonnull final File file, @Nonnull final IconSize size) throws IOException {
		assert hashForLargeFile != null : "Hash for large file is null";
		assert file != null : "File is null";
		assert size != null : "Size is null";
		final File targetIconFile = new File(String.format("%s%s.png", hashForLargeFile, size.getSuffix()));
		FileUtils.copyFile(file, targetIconFile);
	}

	private static void deleteFile(@Nonnull final String hash, @Nonnull final IconSize size) {
		assert hash != null : "Hash is null";
		assert size != null : "Size is null";
		// final File folder = new File(path);
		final String fileName = String.format("%s%s.png", hash, size.getSuffix());
		// final File file = new File(folder, fileName);
		// FileUtils.deleteQuietly(file);
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
			return this.size;
		}

		public String getSuffix() {
			return String.format("-%s", this.size);
		}
	}

}
