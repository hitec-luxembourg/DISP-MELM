package lu.hitec.pssu.melm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

public class MELMUtils {

	public static String getHashForFile(final File file) throws IOException {

		final FileInputStream fis = new FileInputStream(new File("foo"));
		return DigestUtils.md5Hex(fis);

	}

}
