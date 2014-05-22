package lu.hitec.pssu.melm;

import lu.hitec.pssu.melm.persistence.dao.MapElementIconDAO;
import lu.hitec.pssu.melm.persistence.dao.MapElementLibraryDAO;
import lu.hitec.pssu.melm.utils.MapElementIconAnchor;

import org.springframework.beans.factory.annotation.Autowired;

public class IntegrationTestSetUpAndTearDown {

  @Autowired
  private MapElementLibraryDAO mapElementLibraryDAO;

  @Autowired
  private MapElementIconDAO mapElementIconDAO;

  public void setUp() {
    mapElementLibraryDAO.addMapElementLibrary("IntegrationTest", 1, 0, "");
    mapElementIconDAO.addMapElementIcon("IntegrationTestHash", 100, "IntegrationTestName", MapElementIconAnchor.NE);
  }

  public void tearDown() {
    mapElementLibraryDAO.deleteMapElementLibraryForUnitTest("IntegrationTest", 1, 0);
    mapElementIconDAO.deleteMapElementIconForUnitTest("IntegrationTestHash", 100);
  }
}
