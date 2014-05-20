package lu.hitec.pssu.melm.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import lu.hitec.pssu.melm.IntegrationTestSetUpAndTearDown;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/ctx-config-test.xml", "classpath:/ctx-dao.xml", "classpath:/ctx-persistence-test.xml", "classpath:/ctx-integration-test.xml" })
public class MapElementLibraryIconDAOImplTest {

	@Autowired
	private IntegrationTestSetUpAndTearDown integrationTestSetUpAndTearDown;

	@Autowired
	private MapElementLibraryDAO mapElementLibraryDAO;

	@Autowired
	private MapElementIconDAO mapElementIconDAO;

	@Autowired
	private MapElementLibraryIconDAO mapElementLibraryIconDAO;

	@Before
	public void setUp() {
		integrationTestSetUpAndTearDown.setUp();
	}

	@After
	public void tearDown() {
		integrationTestSetUpAndTearDown.tearDown();
	}

	@Test
	public void testAddIconToLibrary() {
		final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary("IntegrationTest", 1, 0);

		assertEquals(0, mapElementLibraryIconDAO.getIconsInLibrary(library).size());

		final MapElementIcon mapElementIcon = mapElementIconDAO.getMapElementIcon("IntegrationTestHash", 100);
		mapElementLibraryIconDAO.addIconToLibrary(library, mapElementIcon, 1, "iconNameInLibrary", "iconDescriptionInLibrary");

		final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		assertEquals(1, iconsInLibrary.size());

		mapElementLibraryIconDAO.deleteLibraryIcon(iconsInLibrary.get(0).getId());

		assertEquals(0, mapElementLibraryIconDAO.getIconsInLibrary(library).size());

	}
	
	@Test
	@Ignore
	public void testGetPrevious() {
		final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary("IntegrationTest", 1, 0);

		assertEquals(0, mapElementLibraryIconDAO.getIconsInLibrary(library).size());

		// Testing with one element
		final MapElementIcon i0 = mapElementIconDAO.addMapElementIcon("Hash_00", 100, "Name_00");
		mapElementLibraryIconDAO.addIconToLibrary(library, i0, 1, "in_00", "id_00");

		List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		assertEquals(1, iconsInLibrary.size());

		MapElementLibraryIcon icon = iconsInLibrary.get(0);
		MapElementLibraryIcon previousLibraryIcon = mapElementLibraryIconDAO.getPreviousLibraryIcon(icon);
		assertNull(previousLibraryIcon);
		
		// Testing with two element
		final MapElementIcon i1 = mapElementIconDAO.addMapElementIcon("Hash_01", 100, "Name_01");
		mapElementLibraryIconDAO.addIconToLibrary(library, i1, 2, "in_01", "id_01");

		iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		assertEquals(2, iconsInLibrary.size());

		icon = iconsInLibrary.get(0);
		previousLibraryIcon = mapElementLibraryIconDAO.getPreviousLibraryIcon(icon);
		assertNull(previousLibraryIcon);
		
		icon = iconsInLibrary.get(1);
		previousLibraryIcon = mapElementLibraryIconDAO.getPreviousLibraryIcon(icon);
		assertEquals(iconsInLibrary.get(0).getId(), previousLibraryIcon.getId());
		
		// Testing with three element
		final MapElementIcon i2 = mapElementIconDAO.addMapElementIcon("Hash_02", 100, "Name_02");
		mapElementLibraryIconDAO.addIconToLibrary(library, i2, 10, "in_02", "id_02");

		iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		assertEquals(3, iconsInLibrary.size());

		icon = iconsInLibrary.get(0);
		previousLibraryIcon = mapElementLibraryIconDAO.getPreviousLibraryIcon(icon);
		assertNull(previousLibraryIcon);
		
		icon = iconsInLibrary.get(1);
		previousLibraryIcon = mapElementLibraryIconDAO.getPreviousLibraryIcon(icon);
		assertEquals(iconsInLibrary.get(0).getId(), previousLibraryIcon.getId());
		
		icon = iconsInLibrary.get(2);
		previousLibraryIcon = mapElementLibraryIconDAO.getPreviousLibraryIcon(icon);
		assertEquals(iconsInLibrary.get(1).getId(), previousLibraryIcon.getId());
		
		// Delete everything
		for (final MapElementLibraryIcon icon_ : iconsInLibrary) {
			mapElementLibraryIconDAO.deleteLibraryIcon(icon_.getId());
		}
		assertEquals(0, mapElementLibraryIconDAO.getIconsInLibrary(library).size());
		
		mapElementIconDAO.deleteMapElementIconForUnitTest("Hash_00", 100);
		mapElementIconDAO.deleteMapElementIconForUnitTest("Hash_01", 100);
		mapElementIconDAO.deleteMapElementIconForUnitTest("Hash_02", 100);
	}
	
	@Test
	@Ignore
	public void testGetNext() {
		final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary("IntegrationTest", 1, 0);

		assertEquals(0, mapElementLibraryIconDAO.getIconsInLibrary(library).size());

		// Testing with one element
		final MapElementIcon i0 = mapElementIconDAO.addMapElementIcon("Hash_00", 100, "Name_00");
		mapElementLibraryIconDAO.addIconToLibrary(library, i0, 1, "in_00", "id_00");

		List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		assertEquals(1, iconsInLibrary.size());

		MapElementLibraryIcon icon = iconsInLibrary.get(0);
		MapElementLibraryIcon nextLibraryIcon = mapElementLibraryIconDAO.getNextLibraryIcon(icon);
		assertNull(nextLibraryIcon);
		
		// Testing with two element
		final MapElementIcon i1 = mapElementIconDAO.addMapElementIcon("Hash_01", 100, "Name_01");
		mapElementLibraryIconDAO.addIconToLibrary(library, i1, 2, "in_01", "id_01");

		iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		assertEquals(2, iconsInLibrary.size());

		icon = iconsInLibrary.get(1);
		nextLibraryIcon = mapElementLibraryIconDAO.getNextLibraryIcon(icon);
		assertNull(nextLibraryIcon);
		
		icon = iconsInLibrary.get(0);
		nextLibraryIcon = mapElementLibraryIconDAO.getNextLibraryIcon(icon);
		assertEquals(iconsInLibrary.get(1).getId(), nextLibraryIcon.getId());
		
		// Testing with three element
		final MapElementIcon i2 = mapElementIconDAO.addMapElementIcon("Hash_02", 100, "Name_02");
		mapElementLibraryIconDAO.addIconToLibrary(library, i2, 10, "in_02", "id_02");

		iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		assertEquals(3, iconsInLibrary.size());

		icon = iconsInLibrary.get(2);
		nextLibraryIcon = mapElementLibraryIconDAO.getNextLibraryIcon(icon);
		assertNull(nextLibraryIcon);
		
		icon = iconsInLibrary.get(1);
		nextLibraryIcon = mapElementLibraryIconDAO.getNextLibraryIcon(icon);
		assertEquals(iconsInLibrary.get(2).getId(), nextLibraryIcon.getId());
		
		icon = iconsInLibrary.get(1);
		nextLibraryIcon = mapElementLibraryIconDAO.getNextLibraryIcon(icon);
		assertEquals(iconsInLibrary.get(2).getId(), nextLibraryIcon.getId());
		
		// Delete everything
		for (final MapElementLibraryIcon icon_ : iconsInLibrary) {
			mapElementLibraryIconDAO.deleteLibraryIcon(icon_.getId());
		}
		assertEquals(0, mapElementLibraryIconDAO.getIconsInLibrary(library).size());
		
		mapElementIconDAO.deleteMapElementIconForUnitTest("Hash_00", 100);
		mapElementIconDAO.deleteMapElementIconForUnitTest("Hash_01", 100);
		mapElementIconDAO.deleteMapElementIconForUnitTest("Hash_02", 100);
	}
}
