package lu.hitec.pssu.melm.persistence.dao;

import static org.junit.Assert.assertEquals;
import lu.hitec.pssu.melm.IntegrationTestSetUpAndTearDown;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

import org.junit.After;
import org.junit.Before;
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
		this.integrationTestSetUpAndTearDown.setUp();
	}

	@After
	public void tearDown() {
		this.integrationTestSetUpAndTearDown.tearDown();
	}

	@Test
	public void testAddIconToLibrary() {
		final MapElementLibrary library = this.mapElementLibraryDAO.getMapElementLibrary("IntegrationTest", 1, 0);

		assertEquals(0, this.mapElementLibraryIconDAO.getIconsInLibrary(library).size());

		final MapElementIcon mapElementIcon = this.mapElementIconDAO.getMapElementIcon("IntegrationTestHash", 100);
		this.mapElementLibraryIconDAO.addIconToLibrary(library, mapElementIcon, 1, "iconNameInLibrary", "iconDescriptionInLibrary");

		assertEquals(1, this.mapElementLibraryIconDAO.getIconsInLibrary(library).size());

		this.mapElementLibraryIconDAO.removeIconFromLibrary(library, mapElementIcon);

		assertEquals(0, this.mapElementLibraryIconDAO.getIconsInLibrary(library).size());

	}

}
