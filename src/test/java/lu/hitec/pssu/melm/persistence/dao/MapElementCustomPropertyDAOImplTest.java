package lu.hitec.pssu.melm.persistence.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import lu.hitec.pssu.melm.IntegrationTestSetUpAndTearDown;
import lu.hitec.pssu.melm.persistence.entity.MapElementCustomProperty;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.utils.CustomPropertyType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/ctx-config-test.xml", "classpath:/ctx-dao.xml", "classpath:/ctx-persistence-test.xml", "classpath:/ctx-integration-test.xml" })
public class MapElementCustomPropertyDAOImplTest {

	@Autowired
	private IntegrationTestSetUpAndTearDown integrationTestSetUpAndTearDown;

	@Autowired
	private MapElementLibraryDAO mapElementLibraryDAO;

	@Autowired
	private MapElementIconDAO mapElementIconDAO;

	@Autowired
	private MapElementLibraryIconDAO mapElementLibraryIconDAO;
	
	@Autowired
	private MapElementCustomPropertyDAO mapElementCustomPropertyDAO;

	@Before
	public void setUp() {
		integrationTestSetUpAndTearDown.setUp();
	}

	@After
	public void tearDown() {
		integrationTestSetUpAndTearDown.tearDown();
	}

	@Test
	public void testCustomProperty() {
		final MapElementLibrary library = mapElementLibraryDAO.getMapElementLibrary("IntegrationTest", 1, 0);

		assertEquals(0, mapElementLibraryIconDAO.getIconsInLibrary(library).size());

		final MapElementIcon mapElementIcon = mapElementIconDAO.getMapElementIcon("IntegrationTestHash", 100);
		mapElementLibraryIconDAO.addIconToLibrary(library, mapElementIcon, 1, "iconNameInLibrary", "iconDescriptionInLibrary");
		
		final List<MapElementLibraryIcon> iconsInLibrary = mapElementLibraryIconDAO.getIconsInLibrary(library);
		assertEquals(1, iconsInLibrary.size());
		
		final MapElementLibraryIcon mapElementLibraryIcon = iconsInLibrary.get(0);
		
		final MapElementCustomProperty customProperty = mapElementCustomPropertyDAO.addCustomProperty(mapElementLibraryIcon, "number_of_casualties", CustomPropertyType.INTEGER);
		assertEquals(1, mapElementCustomPropertyDAO.getCustomProperties(mapElementLibraryIcon).size());
		
		mapElementCustomPropertyDAO.updateCustomProperty(customProperty.getId(), "name_of_casualty", CustomPropertyType.STRING);
		final MapElementCustomProperty customProperty2 = mapElementCustomPropertyDAO.getCustomProperty(customProperty.getId());
		assertEquals("name_of_casualty", customProperty2.getUniqueName());
		
		mapElementCustomPropertyDAO.deleteCustomProperty(customProperty.getId());
		assertEquals(0, mapElementCustomPropertyDAO.getCustomProperties(mapElementLibraryIcon).size());
		
		mapElementLibraryIconDAO.deleteLibraryIcon(mapElementLibraryIcon.getId());

		assertEquals(0, mapElementLibraryIconDAO.getIconsInLibrary(library).size());

	}

}
