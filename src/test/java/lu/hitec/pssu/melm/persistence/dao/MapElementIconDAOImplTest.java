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
	public void test1() {
		assertEquals(0, this.mapElementIconDAO.listAllIcons().size());
		this.mapElementIconDAO.addMapElementIcon("14522dgdg22544dfgdfg225", 1264, "myDisplayName");
		assertEquals(1, this.mapElementIconDAO.listAllIcons().size());

		assertTrue(this.mapElementIconDAO.exist("14522dgdg22544dfgdfg225", 1264));
		assertFalse(this.mapElementIconDAO.exist("14522dgdg22544dfgdfg225", 1263));
		assertFalse(this.mapElementIconDAO.exist("jdjdjdfjdfjdfg", 1264));
		assertFalse(this.mapElementIconDAO.exist("jdjdjdfjdfjdfg", 1263));

		final long theId = this.mapElementIconDAO.listAllIcons().get(0).getId();
		this.mapElementIconDAO.delete(theId);

		assertEquals(0, this.mapElementIconDAO.listAllIcons().size());
		assertFalse(this.mapElementIconDAO.exist("14522dgdg22544dfgdfg225", 1264));
		assertFalse(this.mapElementIconDAO.exist("14522dgdg22544dfgdfg225", 1263));
		assertFalse(this.mapElementIconDAO.exist("jdjdjdfjdfjdfg", 1264));
		assertFalse(this.mapElementIconDAO.exist("jdjdjdfjdfjdfg", 1263));
	}

}