package lu.hitec.pssu.melm.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import lu.hitec.pssu.melm.exceptions.MELMException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/ctx-config-test.xml", "classpath:/ctx-dao.xml", "classpath:/ctx-persistence-test.xml" })
public class MELMServiceImplTest {

  @Autowired
  private MELMService melmService;
  
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAddZipFile() throws MELMException, URISyntaxException, IOException {
    final File archiveFile = new ClassPathResource("sample/zip/emergency.lu-1.0.zip").getFile();
    final File targetArchiveFile = melmService.addZipFile("emergency.lu", "1.0", archiveFile);
    assertNotNull("Target archive file is null", targetArchiveFile);
    assertTrue("Target archive file should exist", targetArchiveFile.exists());
    assertEquals(archiveFile.length(), targetArchiveFile.length());
    final File baseDirectory = melmService.getLibrariesDirectory();
    FileUtils.deleteQuietly(baseDirectory);
  }

  @Test
  public void testBuildArchiveFilename() {
    final String archiveFilename = melmService.buildArchiveFilename("emergency.lu", "1.1");
    assertEquals("emergency.lu-1.1.zip", archiveFilename);
  }

  @Test
  public void testCopyImportedIcons() throws MELMException, IOException {
    final File libraryFolder = new ClassPathResource("sample/libraries/emergency.lu/1.1/emergency.lu-1.1").getFile();
    melmService.copyImportedIcons(libraryFolder);
  }
  
  @Test
  public void testExtractZipFile() throws MELMException, URISyntaxException, IOException {
    {
      final File targetArchiveFile = melmService.getTargetArchiveFile("emergency.lu", "1.0");
      assertNotNull(targetArchiveFile);
      assertFalse("Target archive file should not exist", targetArchiveFile.exists());
    }
    {
      final File tmpZipFile = new ClassPathResource("sample/zip/emergency.lu-1.0.zip").getFile();
      final File targetArchiveFile = melmService.addZipFile("emergency.lu", "1.0", tmpZipFile);
      assertNotNull("Target archive file is null", targetArchiveFile);
      assertTrue("Target archive file should exist", targetArchiveFile.exists());
      assertEquals(tmpZipFile.length(), targetArchiveFile.length());
      melmService.extractZipFile(targetArchiveFile);
    }
    {
      final File targetArchiveFile = melmService.getTargetArchiveFile("emergency.lu", "1.0");
      assertNotNull(targetArchiveFile);
      assertTrue("Target archive file should exist", targetArchiveFile.exists());
      final File baseDirectory = melmService.getLibrariesDirectory();
      FileUtils.deleteQuietly(baseDirectory);
    }
  }

  @Test
  public void testGetTargetArchiveFile() throws MELMException {
    final File archiveFile = melmService.getTargetArchiveFile("emergency.lu", "1.1");
    assertNotNull("Archive file is null", archiveFile);
    assertFalse("Archive file should not exist", archiveFile.exists());
  }

}
