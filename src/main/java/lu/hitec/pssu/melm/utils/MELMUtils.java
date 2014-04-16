package lu.hitec.pssu.melm.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

  public static String getHashForFile(final File file) throws IOException {
    final FileInputStream fis = new FileInputStream(new File("foo"));
    return DigestUtils.md5Hex(fis);
  }

}
