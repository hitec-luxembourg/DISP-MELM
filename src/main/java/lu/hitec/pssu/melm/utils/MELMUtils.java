package lu.hitec.pssu.melm.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.exceptions.MELMException;

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

  public static int getMajorVersion(@Nonnull final String version) throws MELMException {
    assert version != null : "version is null";
    try {
      final String majorVersion = version.substring(0, version.indexOf('.'));
      return Integer.parseInt(majorVersion);
    } catch (final NumberFormatException | StringIndexOutOfBoundsException e) {
      final String msg = String.format("Invalid version %s, should be something like 1.0", version);
      throw new MELMException(msg, e);
    }
  }

  public static int getMinorVersion(@Nonnull final String version) throws MELMException {
    assert version != null : "version is null";
    try {
      final String minorVersion = version.substring(version.indexOf('.') + 1);
      return Integer.parseInt(minorVersion);
    } catch (final NumberFormatException | StringIndexOutOfBoundsException e) {
      final String msg = String.format("Invalid version %s, should be something like 1.0", version);
      throw new MELMException(msg, e);
    }
  }

  private static BufferedImage blurImage(final BufferedImage image) {
    final float ninth = 1.0f / 9.0f;
    final float[] blurKernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth };

    final Map<RenderingHints.Key, Object> map = new HashMap<>();

    map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    final RenderingHints hints = new RenderingHints(map);
    final BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
    return op.filter(image, null);
  }

  public static BufferedImage resizeImageWithHint(@Nonnull final BufferedImage originalImage, final int widthPx, final int heightPx) {
    assert originalImage != null : "original image is null";
    final BufferedImage blurImage = blurImage(originalImage);
    final int type = blurImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : blurImage.getType();
    final BufferedImage bufferedImage = new BufferedImage(widthPx, heightPx, type);
    final Graphics2D graphics2D = bufferedImage.createGraphics();
    graphics2D.setComposite(AlphaComposite.Src);
    // below three lines are for RenderingHints for better image quality at cost of higher processing time
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics2D.drawImage(blurImage, 0, 0, widthPx, heightPx, null);
    graphics2D.dispose();
    return bufferedImage;
  }
}
