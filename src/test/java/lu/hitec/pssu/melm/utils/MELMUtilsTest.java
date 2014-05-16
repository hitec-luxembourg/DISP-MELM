package lu.hitec.pssu.melm.utils;

import static org.junit.Assert.assertEquals;
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

}
