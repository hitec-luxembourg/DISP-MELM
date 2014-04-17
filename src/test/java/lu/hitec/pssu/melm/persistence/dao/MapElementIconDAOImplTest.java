package lu.hitec.pssu.melm.persistence.dao;

import org.junit.Assert;
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
		Assert.assertEquals(0, this.mapElementIconDAO.listAllIcons().size());
		this.mapElementIconDAO.addMapElementIcon("folder1/folder2", "14522dgdg22544dfgdfg225", 1264);

		Assert.assertEquals(1, this.mapElementIconDAO.listAllIcons().size());
	}

}