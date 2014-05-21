package lu.hitec.pssu.melm.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/ctx-config-test.xml", "classpath:/ctx-dao.xml", "classpath:/ctx-persistence-test.xml" })
public class MapElementIconDAOImplTest {

	@Autowired
	private MapElementIconDAO mapElementIconDAO;

	@Autowired
	private MapElementLibraryDAO mapElementLibraryDAO;

	@Autowired
	private MapElementLibraryIconDAO mapElementLibraryIconDAO;

	@Test
	public void testDAO() {
		assertEquals(0, mapElementIconDAO.listAllIcons().size());
		mapElementIconDAO.addMapElementIcon("14522dgdg22544dfgdfg225", 1264, "myDisplayName");
		assertEquals(1, mapElementIconDAO.listAllIcons().size());

		assertTrue(mapElementIconDAO.exist("14522dgdg22544dfgdfg225", 1264));
		assertFalse(mapElementIconDAO.exist("14522dgdg22544dfgdfg225", 1263));
		assertFalse(mapElementIconDAO.exist("jdjdjdfjdfjdfg", 1264));
		assertFalse(mapElementIconDAO.exist("jdjdjdfjdfjdfg", 1263));

		final long theId = mapElementIconDAO.listAllIcons().get(0).getId();
		mapElementIconDAO.delete(theId);

		assertEquals(0, mapElementIconDAO.listAllIcons().size());
		assertFalse(mapElementIconDAO.exist("14522dgdg22544dfgdfg225", 1264));
		assertFalse(mapElementIconDAO.exist("14522dgdg22544dfgdfg225", 1263));
		assertFalse(mapElementIconDAO.exist("jdjdjdfjdfjdfg", 1264));
		assertFalse(mapElementIconDAO.exist("jdjdjdfjdfjdfg", 1263));
	}

	@Test
	public void testIconsAvailable() {
		assertEquals(0, mapElementIconDAO.listAllIcons().size());
		assertFalse(mapElementIconDAO.iconsAvailable());

		final MapElementIcon icon = mapElementIconDAO.addMapElementIcon("14522dgdg22544dfgdfg225_", 1265, "myDisplayName_");

		assertEquals(1, mapElementIconDAO.listAllIcons().size());
		assertTrue(mapElementIconDAO.iconsAvailable());

		final MapElementLibrary library = mapElementLibraryDAO.addMapElementLibrary("name", 2, 1, "iconMd5");
		
		final MapElementLibraryIcon iconToLibrary = mapElementLibraryIconDAO.addIconToLibrary(library, icon, 1, "iconNameInLibrary", "iconDescriptionInLibrary");
		
		
		assertFalse(mapElementIconDAO.iconsAvailable());
		
		mapElementLibraryIconDAO.deleteLibraryIcon(iconToLibrary.getId());
		assertTrue(mapElementIconDAO.iconsAvailable());
		
		mapElementLibraryDAO.deleteMapElementLibrary(library.getId());
		mapElementIconDAO.delete(icon.getId());
		
		assertFalse(mapElementIconDAO.iconsAvailable());
	}
}