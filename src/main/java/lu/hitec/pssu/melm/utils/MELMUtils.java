package lu.hitec.pssu.melm.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.commons.codec.digest.DigestUtils;

public final class MELMUtils {

  private MELMUtils() {
  }

  public static void closeResource(final Closeable closeableMaybeNull) {
    if (closeableMaybeNull != null) {
      try {
        closeableMaybeNull.close();
      } catch (final IOException ex) {
        // ignore
      }
    }
  }

  public static String getHashForFile(@Nonnull final File file) throws IOException {
    assert file != null : "File is null";
    final FileInputStream fis = new FileInputStream(file);
    return DigestUtils.md5Hex(fis);
  }

}
