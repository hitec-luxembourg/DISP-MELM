package lu.hitec.pssu.melm.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

}