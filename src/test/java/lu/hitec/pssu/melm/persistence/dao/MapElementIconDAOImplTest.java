package lu.hitec.pssu.melm.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

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
	public void test1() {
		assertEquals(0, mapElementIconDAO.listAllIcons().size());
		mapElementIconDAO.addMapElementIcon("folder1/folder2", "14522dgdg22544dfgdfg225", 1264, "myDisplayName");
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
	public void testGetAlbumName(){
    assertEquals(0, mapElementIconDAO.listAllIcons().size());
    mapElementIconDAO.addMapElementIcon("D:\\opt\\pssu\\melmservice\\icons\\imported", "14522dgdg22544dfgdfg225", 1264, "myDisplayName");
    assertEquals(1, mapElementIconDAO.listAllIcons().size());
    final MapElementIcon mapElementIcon = mapElementIconDAO.listAllIcons().get(0);
    assertEquals("imported", mapElementIcon.getAlbumName());
    final long theId = mapElementIcon.getId();
    mapElementIconDAO.delete(theId);
	}

}