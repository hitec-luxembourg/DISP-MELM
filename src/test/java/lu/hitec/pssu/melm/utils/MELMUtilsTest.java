package lu.hitec.pssu.melm.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import lu.hitec.pssu.melm.exceptions.MELMException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class MELMUtilsTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testVersions() throws MELMException {
    assertEquals(11, MELMUtils.getMajorVersion("11.0"));
    assertEquals(12, MELMUtils.getMinorVersion("11.12"));
  }

  @Test
  public void testGetNewIndex() throws MELMException {
    assertEquals(-1, MELMUtils.getNewIndex(0, 0));
    assertEquals(-1, MELMUtils.getNewIndex(0, 1));
    assertEquals(1, MELMUtils.getNewIndex(0, 2));
    assertEquals(1, MELMUtils.getNewIndex(2, 0));
    assertEquals(-1, MELMUtils.getNewIndex(1, 0));
    assertEquals(-1, MELMUtils.getNewIndex(1, 1));
    assertEquals(-1, MELMUtils.getNewIndex(1, 2));
    assertEquals(-1, MELMUtils.getNewIndex(2, 1));
    assertEquals(2, MELMUtils.getNewIndex(3, 1));
    assertEquals(2, MELMUtils.getNewIndex(1, 3));
    assertEquals(2, MELMUtils.getNewIndex(1, 4));
    assertEquals(2, MELMUtils.getNewIndex(4, 1));
    assertEquals(3, MELMUtils.getNewIndex(1, 5));
    assertEquals(3, MELMUtils.getNewIndex(5, 1));
    assertEquals(286, MELMUtils.getNewIndex(214, 358));
    assertEquals(286, MELMUtils.getNewIndex(358, 214));
    assertEquals(1000, MELMUtils.getNewIndex(0, 2000));
    assertEquals(1000, MELMUtils.getNewIndex(2000, 0));
    assertEquals(2000, MELMUtils.getNewIndex(1000, 3000));
    assertEquals(2000, MELMUtils.getNewIndex(3000, 1000));
  }

  @Test
  public void testCheckImageSize() throws MELMException {
    @SuppressWarnings("serial")
    final Map<String, Boolean> files = new HashMap<String, Boolean>() {
      {
        put("100x100.png", true);
        put("100x150.png", false);
        put("100x50.png", false);
        put("150x150.png", false);
        put("150x200.png", false);
        put("50x100.png", false);
        put("50x50.png", false);

      }
    };

    for (final String fileName : files.keySet()) {
      final URL resource = MELMUtilsTest.class.getResource(fileName);
      final File file = new File(resource.getFile());
      assertEquals(files.get(fileName), MELMUtils.checkImageSize(file));
    }
  }
}
