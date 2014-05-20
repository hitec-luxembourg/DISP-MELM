package lu.hitec.pssu.melm.utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

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
		try {
			final String majorVersion = version.substring(0, version.indexOf('.'));
			return Integer.parseInt(majorVersion);
		} catch (final NumberFormatException | StringIndexOutOfBoundsException e) {
			final String msg = String.format("Invalid version %s, should be something like 1.0", version);
			throw new MELMException(msg, e);
		}
	}

	public static int getMinorVersion(@Nonnull final String version) throws MELMException {
		try {
			final String minorVersion = version.substring(version.indexOf('.') + 1);
			return Integer.parseInt(minorVersion);
		} catch (final NumberFormatException | StringIndexOutOfBoundsException e) {
			final String msg = String.format("Invalid version %s, should be something like 1.0", version);
			throw new MELMException(msg, e);
		}
	}

	/**
	 * Image resizing: Either width or height may be null but not both. For example if width is set and height is null then the ratio will be kept.
	 */
	@Nonnull
	public static byte[] scaleImage(final byte[] fileData, final Integer widthPx, final Integer heightPx) throws IOException {
		assert fileData != null;
		assert fileData.length > 0;
		assert (widthPx == null) || (widthPx > 0);
		assert (heightPx == null) || (heightPx > 0);
		final BufferedImage img = getImageFromBytesMaybeNull(fileData);
		if (img == null) {
			throw new IOException("scaleImage: Could not read image bytes.");
		}
		final int newHeight;
		final int newWidth;
		if ((heightPx != null) && (widthPx != null)) {
			if ((img.getWidth() == widthPx) && (img.getHeight() == heightPx)) {
				return fileData;
			}
			newWidth = widthPx;
			newHeight = heightPx;
		} else {
			if ((heightPx == null) && (widthPx != null)) {
				newWidth = widthPx;
				newHeight = (widthPx * img.getHeight()) / img.getWidth();
			} else if ((widthPx == null) && (heightPx != null)) {
				newWidth = (heightPx * img.getWidth()) / img.getHeight();
				newHeight = heightPx;
			} else {
				assert false : "Should not happen";
				newWidth = -1;
				newHeight = -1;
			}
		}
		final Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		final BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		final boolean notChanging = bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
		assert !notChanging;
		return getBytesFromImage(bufferedImage);
	}

	@Nonnull
	private static byte[] getBytesFromImage(final BufferedImage bufferedImage) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		final boolean foundWriter = ImageIO.write(bufferedImage, "jpg", baos);
		assert foundWriter;
		return baos.toByteArray();
	}

	@CheckForNull
	private static final BufferedImage getImageFromBytesMaybeNull(final byte[] data) throws IOException {
		assert data != null;
		assert data.length > 0;
		final BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
		// The image type is unknown when 0. This may cause problems down the line, return null instead.
		return image.getType() != 0 ? image : null;
	}

	public static int getNewIndex(final int i1, final int i2) {
		assert i1 >= 0;
		assert i2 >= 0;
		
		int result = -1;
		final int distance = Math.abs(i1 - i2);
		if(1 < distance) {
			result = Math.min(i1, i2) + (distance / 2);
		}
		return result;
	}
}
