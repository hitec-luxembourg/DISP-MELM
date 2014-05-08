package lu.hitec.pssu.melm.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ZipService.class);
  private final List<String> fileList;
  private final String sourceFolderPath;

  ZipService(@Nonnull final String sourceFolderPath) {
    assert sourceFolderPath != null : "sourceFolderPath is null";
    fileList = new ArrayList<String>();
    this.sourceFolderPath = sourceFolderPath;
  }

  /**
   * Traverse a directory and get all files, and add the file into fileList
   * 
   * @param node file or directory
   */
  public void generateFileList(@Nonnull final File node) {
    assert node != null : "node is null";
    // add file only.
    if (node.isFile()) {
      fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
    }

    if (node.isDirectory()) {
      final String[] subNote = node.list();
      for (final String filename : subNote) {
        generateFileList(new File(node, filename));
      }
    }
  }

  /**
   * Zip it
   * 
   * @param zipFile output ZIP file location
   */
  public void zipIt(@Nonnull final String zipFile) {
    assert zipFile != null : "zipFile is null";
    final byte[] buffer = new byte[1024];

    try {
      final FileOutputStream fos = new FileOutputStream(zipFile);
      final ZipOutputStream zos = new ZipOutputStream(fos);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(String.format("Output to Zip : %s", zipFile));
      }

      for (final String file : fileList) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug(String.format("File Added : %s", file));
        }

        final ZipEntry ze = new ZipEntry(file);
        zos.putNextEntry(ze);

        final FileInputStream in = new FileInputStream(sourceFolderPath + File.separator + file);

        int len;
        while ((len = in.read(buffer)) > 0) {
          zos.write(buffer, 0, len);
        }

        in.close();
      }

      zos.closeEntry();
      // remember close it.
      zos.close();

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("DONE");
      }
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Format the file path for zip.
   * 
   * @param file file path
   * @return Formatted file path
   */
  private String generateZipEntry(@Nonnull final String file) {
    assert file != null : "file is null";
    return file.substring(sourceFolderPath.length() + 1, file.length());
  }

}
