package lu.hitec.pssu.melm.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.dao.MapElementIconDAO;
import lu.hitec.pssu.melm.persistence.dao.MapElementLibraryDAO;
import lu.hitec.pssu.melm.persistence.dao.MapElementLibraryIconDAO;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.services.MELMServiceImpl.IconSize;
import lu.hitec.pssu.melm.utils.MapElementIconAnchor;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("static-method")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/ctx-config-test.xml", "classpath:/ctx-dao.xml", "classpath:/ctx-persistence-test.xml" })
public class MELMServiceImplTest {

	@Autowired
	private MapElementIconDAO mapElementIconDAO;

	@Autowired
	private MapElementLibraryDAO mapElementLibraryDAO;

	@Autowired
	private MapElementLibraryIconDAO mapElementLibraryIconDAO;

	@Autowired
	private MELMService melmService;

	private final int testLibrarySize = 6;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		final File librariesDirectory = melmService.getLibrariesDirectory();
		FileUtils.deleteQuietly(librariesDirectory);

		final File iconsDirectory = melmService.getIconsDirectory();
		FileUtils.deleteQuietly(iconsDirectory);

		final List<MapElementLibrary> libraries = melmService.listAllLibraries();
		for (final MapElementLibrary library : libraries) {
			melmService.deleteLibrary(library.getId());
		}

		final List<MapElementIcon> icons = mapElementIconDAO.listAllIcons();
		for (final MapElementIcon icon : icons) {
			mapElementIconDAO.delete(icon.getId());
		}
	}

	@Test
	public void testGetSelectedFilename() {
		assertEquals("filetest_selected.png", melmService.getSelectedFileName("filetest.png"));
		assertEquals("file.test_selected.png", melmService.getSelectedFileName("file.test.png"));
		assertEquals("01_selected.png", melmService.getSelectedFileName("01.png"));
		assertEquals("01.test_selected.png", melmService.getSelectedFileName("01.test.png"));

	}
	
	@Test
	public void testAddIconAndFiles() throws MELMException, IOException {
		final File sampleIcon = new ClassPathResource("sample/libraries/emergency.lu/1.1/emergency.lu-1.1/100px/Accident.png").getFile();
		final MapElementIcon icon = melmService.addIconAndFiles("AccidentFromUnitTest", MapElementIconAnchor.NE, sampleIcon);

		assertEquals("1/b/1b7ccd4aa667f2dd1a9bb5d39772ccd7-100px.png", icon.getFilePath(IconSize.LARGE, false));
		assertEquals("1/b/1b7ccd4aa667f2dd1a9bb5d39772ccd7-100px_selected.png", icon.getFilePath(IconSize.LARGE, true));

		try {
			melmService.addIconAndFiles("AccidentFromUnitTest", MapElementIconAnchor.NE, sampleIcon);
			fail("Should have raised an exception, Icon already Exist");
		} catch (final MELMException e) {
		}
	}

	@Test
	public void testAddZipFile() throws MELMException, URISyntaxException, IOException {
		final File archiveFile = new ClassPathResource("sample/zip/emergency.lu-1.0.zip").getFile();
		final File targetArchiveFile = melmService.importLibrary("emergency.lu", 1, 0, archiveFile);
		assertNotNull("Target archive file is null", targetArchiveFile);
		assertTrue("Target archive file should exist", targetArchiveFile.exists());
		assertEquals(archiveFile.length(), targetArchiveFile.length());
	}

	@Test
	public void testBuildArchiveFilename() {
		final String archiveFilename = melmService.buildArchiveFilename("emergency.lu", 1, 1);
		assertEquals("emergency.lu-1.1.zip", archiveFilename);
	}

	@Test
	public void testExtractZipFile() throws MELMException, URISyntaxException, IOException {
		{
			final File targetArchiveFile = melmService.getTargetArchiveFile("emergency.lu", 1, 0);
			assertNotNull(targetArchiveFile);
			assertFalse("Target archive file should not exist", targetArchiveFile.exists());
		}
		{
			final File tmpZipFile = new ClassPathResource("sample/zip/emergency.lu-1.0.zip").getFile();
			final File targetArchiveFile = melmService.importLibrary("emergency.lu", 1, 0, tmpZipFile);
			assertNotNull("Target archive file is null", targetArchiveFile);
			assertTrue("Target archive file should exist", targetArchiveFile.exists());
			assertEquals(tmpZipFile.length(), targetArchiveFile.length());
			melmService.extractImportedLibrary(targetArchiveFile);
		}
		{
			final File targetArchiveFile = melmService.getTargetArchiveFile("emergency.lu", 1, 0);
			assertNotNull(targetArchiveFile);
			assertTrue("Target archive file should exist", targetArchiveFile.exists());
		}
	}

	@Test
	public void testGetTargetArchiveFile() throws MELMException {
		final File archiveFile = melmService.getTargetArchiveFile("emergency.lu", 1, 1);
		assertNotNull("Archive file is null", archiveFile);
		assertFalse("Archive file should not exist", archiveFile.exists());
	}

	@Test
	public void testMoveIconLibraryUp() {
		final MapElementLibrary library = mapElementLibraryDAO.addMapElementLibrary("IntegrationTest", 1, 0, "");
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementIcon icon = mapElementIconDAO.addMapElementIcon("Hash_0" + i, 100, "Name_0" + i, MapElementIconAnchor.NE);
			mapElementLibraryIconDAO.addIconToLibrary(library, icon, i + 1, "in_0" + i, "id_0" + i);
		}

		final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		List<MapElementLibraryIcon> updatedIconsInLibrary = null;
		assertEquals(testLibrarySize, iconsInLibrary.size());

		MapElementLibraryIcon icon = null;

		// Test moving the first one up --> nothing happens
		icon = iconsInLibrary.get(0);
		melmService.moveLibraryIcon(icon.getId(), "up");
		updatedIconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementLibraryIcon i1 = iconsInLibrary.get(i);
			final MapElementLibraryIcon i2 = updatedIconsInLibrary.get(i);
			assertEquals(i1.getIndexOfIconInLibrary(), i2.getIndexOfIconInLibrary());
		}

		// Test moving the second one up --> the second one becomes first and all the others are shifted back
		icon = iconsInLibrary.get(1);
		melmService.moveLibraryIcon(icon.getId(), "up");
		updatedIconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementLibraryIcon i1 = iconsInLibrary.get(i);
			final MapElementLibraryIcon i2 = updatedIconsInLibrary.get(i);
			if (0 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(1).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			} else if (1 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(0).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			} else {
				assertEquals(i1.getId(), i2.getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			}
		}

		// Test moving the last one up --> the last one becomes last-1, last-1 becomes last and all the others don't move
		icon = iconsInLibrary.get(testLibrarySize - 1);
		melmService.moveLibraryIcon(icon.getId(), "up");
		updatedIconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementLibraryIcon i1 = iconsInLibrary.get(i);
			final MapElementLibraryIcon i2 = updatedIconsInLibrary.get(i);
			if (0 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(1).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			} else if (1 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(0).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			} else if ((testLibrarySize - 2) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 1).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), (MELMService.ORDER_INCREMENT * (1 + i)) - (MELMService.ORDER_INCREMENT / 2));
			} else if ((testLibrarySize - 1) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 2).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (i));
			} else {
				assertEquals(i1.getId(), i2.getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			}
		}

		// Test moving the 4th one up --> the 4th becomes 3rd, 3rd becomes 4th and all the others don't move
		icon = iconsInLibrary.get(3);
		melmService.moveLibraryIcon(icon.getId(), "up");
		updatedIconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementLibraryIcon i1 = iconsInLibrary.get(i);
			final MapElementLibraryIcon i2 = updatedIconsInLibrary.get(i);
			if (0 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(1).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			} else if (1 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(0).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			} else if (2 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(3).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), (MELMService.ORDER_INCREMENT * (1 + i)) - (MELMService.ORDER_INCREMENT / 2));
			} else if (3 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(2).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (i));
			} else if ((testLibrarySize - 2) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 1).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), (MELMService.ORDER_INCREMENT * (1 + i)) - (MELMService.ORDER_INCREMENT / 2));
			} else if ((testLibrarySize - 1) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 2).getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (i));
			} else {
				assertEquals(i1.getId(), i2.getId());
				assertEquals(i2.getIndexOfIconInLibrary(), MELMService.ORDER_INCREMENT * (1 + i));
			}
		}
	}

	@Test
	public void testMoveIconLibraryDown() {
		final MapElementLibrary library = mapElementLibraryDAO.addMapElementLibrary("IntegrationTest", 1, 0, "");
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementIcon icon = mapElementIconDAO.addMapElementIcon("Hash_0" + i, 100, "Name_0" + i, MapElementIconAnchor.NE);
			mapElementLibraryIconDAO.addIconToLibrary(library, icon, i + 1, "in_0" + i, "id_0" + i);
		}

		final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		List<MapElementLibraryIcon> updatedIconsInLibrary = null;
		assertEquals(testLibrarySize, iconsInLibrary.size());

		MapElementLibraryIcon icon = null;

		// Test moving the last one up --> nothing happens
		icon = iconsInLibrary.get(testLibrarySize - 1);
		melmService.moveLibraryIcon(icon.getId(), "down");
		updatedIconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementLibraryIcon i1 = iconsInLibrary.get(i);
			final MapElementLibraryIcon i2 = updatedIconsInLibrary.get(i);
			assertEquals(i1.getIndexOfIconInLibrary(), i2.getIndexOfIconInLibrary());
		}

		// Test moving the penultimate one up --> the penultimate becomes last and all the others don't move
		icon = iconsInLibrary.get(testLibrarySize - 2);
		melmService.moveLibraryIcon(icon.getId(), "down");
		updatedIconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementLibraryIcon i1 = iconsInLibrary.get(i);
			final MapElementLibraryIcon i2 = updatedIconsInLibrary.get(i);
			if ((testLibrarySize - 2) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 1).getId());
			} else if ((testLibrarySize - 1) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 2).getId());
			} else {
				assertEquals(i1.getId(), i2.getId());
			}
		}

		// Test moving the first one down --> the first one becomes second, second becomes first and all the others don't move
		icon = iconsInLibrary.get(0);
		melmService.moveLibraryIcon(icon.getId(), "down");
		updatedIconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementLibraryIcon i1 = iconsInLibrary.get(i);
			final MapElementLibraryIcon i2 = updatedIconsInLibrary.get(i);
			if (0 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(1).getId());
			} else if (1 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(0).getId());
			} else if ((testLibrarySize - 2) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 1).getId());
			} else if ((testLibrarySize - 1) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 2).getId());
			} else {
				assertEquals(i1.getId(), i2.getId());
			}
		}
		// Test moving the third one down --> the 4th becomes 3rd, 3rd becomes 4th and all the others don't move
		icon = iconsInLibrary.get(3);
		melmService.moveLibraryIcon(icon.getId(), "up");
		updatedIconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		for (int i = 0; i < testLibrarySize; i++) {
			final MapElementLibraryIcon i1 = iconsInLibrary.get(i);
			final MapElementLibraryIcon i2 = updatedIconsInLibrary.get(i);
			if (0 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(1).getId());
			} else if (1 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(0).getId());
			} else if (2 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(3).getId());
			} else if (3 == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(2).getId());
			} else if ((testLibrarySize - 2) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 1).getId());
			} else if ((testLibrarySize - 1) == i) {
				assertEquals(i2.getId(), iconsInLibrary.get(testLibrarySize - 2).getId());
			} else {
				assertEquals(i1.getId(), i2.getId());
			}
		}
	}

	@Test
	public void testMoveImportedIcons() throws MELMException, IOException {
		final MapElementLibrary mapElementLibrary = melmService.addLibrary("emergency.lu", 1, 1, "");
		final File tmpZipFile = new ClassPathResource("sample/zip/emergency.lu-1.1.zip").getFile();
		final File targetArchiveFile = melmService.importLibrary("emergency.lu", 1, 1, tmpZipFile);
		final File libraryFolder = melmService.extractImportedLibrary(targetArchiveFile);
		final NodeList nodeList = melmService.validateImportedLibraryAndGetNodeList("emergency.lu", 1, 1);
		melmService.moveImportedIcons(mapElementLibrary, nodeList, libraryFolder);
	}

	@Test
	public void testXPathParsing() throws Exception {
		final File xmlFile = new ClassPathResource("sample/libraries/emergency.lu/1.1/emergency.lu-1.1/emergency.lu-1.1.xml").getFile();
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document document = builder.parse(xmlFile);
		final XPath xPath = XPathFactory.newInstance().newXPath();
		final NodeList nodeList = (NodeList) xPath.compile(MELMServiceImpl.XPATH_LIBRARY_ELEMENTS_EXPRESSION).evaluate(document, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node node = nodeList.item(i);
			final Element element = (Element) node;
			final String itemName = element.getAttribute("unique-code");
			assertNotNull(itemName);
			final String itemDescription = element.getAttribute("description");
			assertNotNull(itemDescription);

			final Node subNode = (Node) xPath.compile(MELMServiceImpl.XPATH_LIBRARY_ELEMENTS_ICON_EXPRESSION).evaluate(node, XPathConstants.NODE);
			final Element subElement = (Element) subNode;
			final String fileName = subElement.getAttribute("file");
			assertNotNull(fileName);

			final NodeList nodeList2 = (NodeList) xPath.compile(MELMServiceImpl.XPATH_LIBRARY_ELEMENTS_CUSTOM_PROPERTY_EXPRESSION).evaluate(node, XPathConstants.NODESET);
			for (int j = 0; j < nodeList2.getLength(); j++) {
				final Node node2 = nodeList2.item(j);
				final Element element2 = (Element) node2;
				System.out.println(element2.getAttribute("key"));
				System.out.println(element2.getAttribute("type"));
			}
		}
	}
}
