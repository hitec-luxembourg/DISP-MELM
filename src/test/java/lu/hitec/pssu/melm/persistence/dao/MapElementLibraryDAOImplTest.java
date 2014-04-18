package lu.hitec.pssu.melm.persistence.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/ctx-config-test.xml", "classpath:/ctx-dao.xml", "classpath:/ctx-persistence-test.xml" })
public class MapElementLibraryDAOImplTest {

	@Autowired
	private MapElementLibraryDAO mapElementLibraryDAO;

	@Test
	public void testAddMapElementLibrary() {
		assertEquals(0, mapElementLibraryDAO.listAllLibraries().size());
		mapElementLibraryDAO.addMapElementLibrary("emergency.lu", 1, 0);
		assertEquals(1, mapElementLibraryDAO.listAllLibraries().size());
	}
}
