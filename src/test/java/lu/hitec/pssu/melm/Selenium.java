package lu.hitec.pssu.melm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

@Ignore
public class Selenium {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private final StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8080";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testSelenium() throws Exception {
    // 1.0.0.1
    driver.get(baseUrl + "/DISP-MELM/login/");
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys("disp");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("melm");
    driver.findElement(By.id("btn-login")).click();
    assertEquals("Map Assessment Library Manager", driver.findElement(By.cssSelector("h1")).getText());
    // 1.0.0.2
    driver.findElement(By.linkText("Log-out")).click();
    assertEquals("Login", driver.findElement(By.cssSelector("h1")).getText());
    // 1.0.0.1a
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys("");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("");
    driver.findElement(By.id("btn-login")).click();
    assertEquals("Wrong User id or Password!", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys("zuizuiz");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("zuizuizi");
    driver.findElement(By.id("btn-login")).click();
    assertEquals("Wrong User id or Password!", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    // 1.0.0.3
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys("disp");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("melm");
    driver.findElement(By.id("btn-login")).click();
    assertEquals("Map Assessment Library Manager", driver.findElement(By.cssSelector("h1")).getText());
    driver.findElement(By.cssSelector("button.btn.btn-import")).click();
    //driver.findElement(By.id("libraryFile")).clear();
    driver.findElement(By.id("libraryFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\ocha_activity-1.0.zip");
    assertEquals("ocha_activity", driver.findElement(By.xpath("//tr[1]/td[2]")).getText());
    driver.findElement(By.cssSelector("button.btn.btn-import")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) {
        fail("timeout");
      }
    	try { if (isElementPresent(By.xpath("//h4"))) {
        break;
      } } catch (final Exception e) {}
    	Thread.sleep(1000);
    }

    assertEquals("ocha_activity-1.0.zip has been successfully imported!", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[8]")).click();
    //driver.findElement(By.id("libraryFile")).clear();
    driver.findElement(By.id("libraryFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\ocha_camp-1.0.zip");
    assertEquals("ocha_camp", driver.findElement(By.xpath("//tr[2]/td[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) {
        fail("timeout");
      }
    	try { if (isElementPresent(By.xpath("//h4"))) {
        break;
      } } catch (final Exception e) {}
    	Thread.sleep(1000);
    }

    assertEquals("ocha_camp-1.0.zip has been successfully imported!", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[10]")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[8]")).click();
    // Warning: assertTextNotPresent may require manual changes
    assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*//tr\\[1\\]/td\\[2\\][\\s\\S]*$"));
    // Warning: assertTextNotPresent may require manual changes
    assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*//tr\\[2\\]/td\\[2\\][\\s\\S]*$"));
    // 1.0.0.3a
    //driver.findElement(By.id("libraryFile")).clear();
    driver.findElement(By.id("libraryFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\invalid.zip");
    // Warning: assertTextNotPresent may require manual changes
    assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*//tr\\[1\\]/td\\[2\\][\\s\\S]*$"));
    //driver.findElement(By.id("libraryFile")).clear();
    driver.findElement(By.id("libraryFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\ocha_camp-1.0.zip");
    driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) {
        fail("timeout");
      }
    	try { if (isElementPresent(By.xpath("//h4"))) {
        break;
      } } catch (final Exception e) {}
    	Thread.sleep(1000);
    }

    assertEquals("Library with name ocha_camp and version 1.0 already exist", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    // 1.0.0.4
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("Add")).click();
    //driver.findElement(By.id("libraryIconFile")).clear();
    driver.findElement(By.id("libraryIconFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\icon.png");
    driver.findElement(By.id("libraryName")).clear();
    driver.findElement(By.id("libraryName")).sendKeys("toto");
    driver.findElement(By.id("version")).clear();
    driver.findElement(By.id("version")).sendKeys("1.0");
    assertEquals("icon.png", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    assertEquals("toto", driver.findElement(By.xpath("//tr[4]/td[2]")).getText());
    assertEquals("1.0", driver.findElement(By.xpath("//tr[4]/td[3]")).getText());
    // 1.0.0.4a
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    //driver.findElement(By.id("libraryIconFile")).clear();
    driver.findElement(By.id("libraryIconFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\invalid.png");
    driver.findElement(By.id("libraryName")).clear();
    driver.findElement(By.id("libraryName")).sendKeys("toto");
    driver.findElement(By.id("version")).clear();
    driver.findElement(By.id("version")).sendKeys("1.0");
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) {
        fail("timeout");
      }
    	try { if (isElementPresent(By.xpath("//h4"))) {
        break;
      } } catch (final Exception e) {}
    	Thread.sleep(1000);
    }

    assertEquals("Invalid size for icon file (must be 40px by 40px)", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
    driver.findElement(By.cssSelector("button.btn.btn-danger")).click();
    //driver.findElement(By.id("libraryIconFile")).clear();
    driver.findElement(By.id("libraryIconFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\icon.png");
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) {
        fail("timeout");
      }
    	try { if (isElementPresent(By.xpath("//h4"))) {
        break;
      } } catch (final Exception e) {}
    	Thread.sleep(1000);
    }

    assertEquals("Library with name toto and version 1.0 already exist", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
    driver.findElement(By.id("libraryName")).clear();
    driver.findElement(By.id("libraryName")).sendKeys("titi");
    driver.findElement(By.id("version")).clear();
    driver.findElement(By.id("version")).sendKeys("1.1");
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    assertEquals("titi", driver.findElement(By.xpath("//tr[4]/td[2]")).getText());
    assertEquals("1.1", driver.findElement(By.xpath("//tr[4]/td[3]")).getText());
    // 1.0.0.5
    driver.findElement(By.linkText("Name")).click();
    assertEquals("toto", driver.findElement(By.xpath("//tr[2]/td[2]")).getText());
    driver.findElement(By.linkText("Name")).click();
    assertEquals("ocha_activity", driver.findElement(By.xpath("//tr[2]/td[2]")).getText());
    driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();
    try {
      assertEquals("on", driver.findElement(By.cssSelector("input.ng-pristine.ng-valid")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("on", driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("on", driver.findElement(By.xpath("(//input[@type='checkbox'])[4]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("on", driver.findElement(By.xpath("(//input[@type='checkbox'])[5]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();
    try {
      assertEquals("off", driver.findElement(By.cssSelector("input.ng-pristine.ng-valid")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("off", driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("off", driver.findElement(By.xpath("(//input[@type='checkbox'])[4]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("off", driver.findElement(By.xpath("(//input[@type='checkbox'])[5]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    // 1.0.0.6
    driver.findElement(By.xpath("//tr[4]/td[5]/ul/li[5]/button")).click();
    assertEquals("Confirmation required", driver.findElement(By.cssSelector("h4.modal-title.ng-binding")).getText());
    assertTrue(driver.findElement(By.xpath("//div/div/div[2]")).getText().matches("^exact:Do you really want to delete this resource [\\s\\S]$"));
    driver.findElement(By.xpath("(//button[@type='button'])[3]")).click();
    assertEquals("toto", driver.findElement(By.xpath("//tr[4]/td[2]")).getText());
    assertEquals("1.0", driver.findElement(By.xpath("//tr[4]/td[3]")).getText());
    driver.findElement(By.xpath("(//input[@type='checkbox'])[4]")).click();
    driver.findElement(By.cssSelector("button.btn.btn-delete")).click();
    assertEquals("Confirmation required", driver.findElement(By.cssSelector("h4.modal-title.ng-binding")).getText());
    assertTrue(driver.findElement(By.xpath("//div/div/div[2]")).getText().matches("^exact:Do you really want to delete these resources [\\s\\S]$"));
    driver.findElement(By.xpath("(//button[@type='button'])[3]")).click();
    assertFalse(isElementPresent(By.xpath("//table/tr[4]")));
    // 1.0.0.6a
    driver.findElement(By.xpath("//li[5]/button")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
    assertEquals("ocha_activity", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.cssSelector("input.ng-pristine.ng-valid")).click();
    driver.findElement(By.cssSelector("button.btn.btn-delete")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
    assertEquals("ocha_activity", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    // 1.0.0.8
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.xpath("//li[3]/button")).click();
    driver.findElement(By.id("libraryName")).clear();
    driver.findElement(By.id("libraryName")).sendKeys("ocha_activiti");
    driver.findElement(By.id("version")).clear();
    driver.findElement(By.id("version")).sendKeys("1.2");
    driver.findElement(By.xpath("(//input[@name='iconChoice'])[2]")).click();
    //driver.findElement(By.id("libraryIconFile")).clear();
    driver.findElement(By.id("libraryIconFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\icon2.png");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("ocha_activiti", driver.findElement(By.xpath("//tr[2]/td[2]")).getText());
    assertEquals("1.2", driver.findElement(By.xpath("//tr[2]/td[3]")).getText());
    // 1.0.0.8a
    driver.findElement(By.xpath("//li[3]/button")).click();
    driver.findElement(By.id("libraryName")).clear();
    driver.findElement(By.id("libraryName")).sendKeys("");
    driver.findElement(By.id("version")).clear();
    driver.findElement(By.id("version")).sendKeys("");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("Library name and version are mandatory", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    driver.findElement(By.xpath("(//input[@name='iconChoice'])[2]")).click();
    //driver.findElement(By.id("libraryIconFile")).clear();
    driver.findElement(By.id("libraryIconFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\invalid.png");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("Invalid size for icon file (must be 40px by 40px)", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    driver.findElement(By.cssSelector("button.btn.btn-default")).click();
    // 1.0.0.9
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.xpath("//li[2]/button")).click();
    driver.findElement(By.id("libraryName")).clear();
    driver.findElement(By.id("libraryName")).sendKeys("ocha_activita");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("ocha_activita", driver.findElement(By.xpath("//tr[2]/td[2]")).getText());
    // 1.0.0.9a
    driver.findElement(By.xpath("//li[2]/button")).click();
    driver.findElement(By.id("libraryName")).clear();
    driver.findElement(By.id("libraryName")).sendKeys("");
    driver.findElement(By.id("version")).clear();
    driver.findElement(By.id("version")).sendKeys("");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("Library name and version are mandatory", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    driver.findElement(By.id("libraryName")).clear();
    driver.findElement(By.id("libraryName")).sendKeys("ocha_activity");
    driver.findElement(By.id("version")).clear();
    driver.findElement(By.id("version")).sendKeys("1.0");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("Library with name ocha_activity and version 1.0 already exist", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    driver.findElement(By.cssSelector("button.btn")).click();
    // 1.0.0.10
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.cssSelector("button.btn.btn-list")).click();
    assertEquals("activity-advocacy", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.linkText("Element Name")).click();
    assertEquals("activity-training", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.linkText("Element Name")).click();
    assertEquals("activity-advocacy", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();
    try {
      assertEquals("on", driver.findElement(By.cssSelector("input.ng-pristine.ng-valid")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("on", driver.findElement(By.xpath("(//input[@type='checkbox'])[6]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("on", driver.findElement(By.xpath("(//input[@type='checkbox'])[9]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();
    try {
      assertEquals("off", driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("off", driver.findElement(By.xpath("(//input[@type='checkbox'])[6]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("off", driver.findElement(By.xpath("(//input[@type='checkbox'])[9]")).getAttribute("value"));
    } catch (final Error e) {
      verificationErrors.append(e.toString());
    }
    // 1.0.0.11
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.cssSelector("button.btn.btn-list")).click();
    driver.findElement(By.linkText("2")).click();
    assertEquals("activity-gap_analysis", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    assertEquals("activity-humanitarian_programme_cycle", driver.findElement(By.xpath("//tr[2]/td[3]")).getText());
    driver.findElement(By.xpath("//li[5]/button")).click();
    assertEquals("activity-humanitarian_programme_cycle", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    assertEquals("activity-gap_analysis", driver.findElement(By.xpath("//tr[2]/td[3]")).getText());
    driver.findElement(By.xpath("//tr[2]/td[6]/ul/li[4]/button")).click();
    assertEquals("activity-gap_analysis", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    assertEquals("activity-humanitarian_programme_cycle", driver.findElement(By.xpath("//tr[2]/td[3]")).getText());
    // 1.0.0.13
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.cssSelector("button.btn.btn-list")).click();
    assertEquals("activity-advocacy", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.xpath("//li[3]/button")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
    assertEquals("activity-analysis", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    assertEquals("activity-assessment", driver.findElement(By.xpath("//tr[2]/td[3]")).getText());
    driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).click();
    driver.findElement(By.xpath("//tr[2]/td[6]/ul/li[3]/button")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
    assertEquals("activity-civil_military_coordination", driver.findElement(By.xpath("//tr[2]/td[3]")).getText());
    // 1.0.0.13a
    driver.findElement(By.xpath("//li[3]/button")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
    assertEquals("activity-analysis", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.xpath("(//input[@type='checkbox'])[6]")).click();
    driver.findElement(By.cssSelector("button.btn.btn-delete")).click();
    driver.findElement(By.xpath("//body/div[4]")).click();
    assertEquals("activity-financing", driver.findElement(By.xpath("//tr[5]/td[3]")).getText());
    // 1.0.0.14
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.cssSelector("button.btn.btn-list")).click();
    driver.findElement(By.xpath("//li[2]/button")).click();
    driver.findElement(By.id("iconName")).clear();
    driver.findElement(By.id("iconName")).sendKeys("activity-analysa");
    driver.findElement(By.id("iconDescription")).clear();
    driver.findElement(By.id("iconDescription")).sendKeys("activity analysa");
    driver.findElement(By.xpath("//div[3]")).click();
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("activity-analysa", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    // 1.0.0.14a
    driver.findElement(By.xpath("//li[2]/button")).click();
    driver.findElement(By.id("iconName")).clear();
    driver.findElement(By.id("iconName")).sendKeys("");
    driver.findElement(By.id("iconDescription")).clear();
    driver.findElement(By.id("iconDescription")).sendKeys("");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("Element name and description are mandatory", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    driver.findElement(By.cssSelector("button.btn.btn-default")).click();
    driver.findElement(By.xpath("//li[2]/button")).click();
    driver.findElement(By.id("iconName")).clear();
    driver.findElement(By.id("iconName")).sendKeys("activity-deployment");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("Name activity-deployment is already used in this library", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    // 1.0.0.15 1.0.0.16 1.0.0.16a
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.cssSelector("button.btn.btn-list")).click();
    driver.findElement(By.cssSelector("button.btn.ng-scope")).click();
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop1");
    driver.findElement(By.id("newResource_type")).click();
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("DATE");
    driver.findElement(By.cssSelector("option[value=\"1\"]")).click();
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    assertEquals("prop1", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("DATE", driver.findElement(By.xpath("//td[3]/span")).getText());
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    assertEquals("Property name and type are mandatory", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop1");
    driver.findElement(By.id("newResource_type")).click();
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("INTEGER");
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    assertEquals("Property with name prop1 is already existing for this element activity-analysa", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
    driver.findElement(By.cssSelector("div.container > button.btn.btn-default")).click();
    assertEquals("activity-analysa", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    // 1.0.0.17
    driver.findElement(By.cssSelector("button.btn.ng-scope")).click();
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop4");
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("STRING");
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop5");
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("STRING");
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop6");
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("DATE");
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop7");
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("STRING");
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop8");
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("DATE");
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop2");
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("INTEGER");
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    driver.findElement(By.id("newResource_unique_name")).clear();
    driver.findElement(By.id("newResource_unique_name")).sendKeys("prop3");
    new Select(driver.findElement(By.id("newResource_type"))).selectByVisibleText("INTEGER");
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    assertEquals("prop1", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("DATE", driver.findElement(By.xpath("//td[3]/span")).getText());
    driver.findElement(By.linkText("Unique name")).click();
    assertEquals("prop8", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("DATE", driver.findElement(By.xpath("//td[3]/span")).getText());
    driver.findElement(By.linkText("Type")).click();
    assertEquals("DATE", driver.findElement(By.xpath("//td[3]/span")).getText());
    assertEquals("prop1", driver.findElement(By.xpath("//td[2]/span")).getText());
    driver.findElement(By.linkText("Type")).click();
    assertEquals("STRING", driver.findElement(By.xpath("//td[3]/span")).getText());
    assertEquals("prop4", driver.findElement(By.xpath("//td[2]/span")).getText());
    driver.findElement(By.linkText("Unique name")).click();
    assertEquals("prop1", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("DATE", driver.findElement(By.xpath("//td[3]/span")).getText());
    driver.findElement(By.linkText("2")).click();
    assertEquals("prop8", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("DATE", driver.findElement(By.xpath("//td[3]/span")).getText());
    driver.findElement(By.linkText("1")).click();
    assertEquals("prop1", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("DATE", driver.findElement(By.xpath("//td[3]/span")).getText());
    // 1.0.0.18
    driver.findElement(By.xpath("//li[2]/button")).click();
    assertEquals("Confirmation required", driver.findElement(By.cssSelector("h4.modal-title.ng-binding")).getText());
    assertTrue(driver.findElement(By.xpath("//div/div/div[2]")).getText().matches("^exact:Do you really want to delete this resource [\\s\\S]$"));
    driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
    assertEquals("prop2", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("INTEGER", driver.findElement(By.xpath("//td[3]/span")).getText());
    driver.findElement(By.cssSelector("td > input.ng-pristine.ng-valid")).click();
    driver.findElement(By.cssSelector("td > input.ng-pristine.ng-valid")).click();
    driver.findElement(By.cssSelector("button.btn.btn-delete")).click();
    assertEquals("Confirmation required", driver.findElement(By.cssSelector("h4.modal-title.ng-binding")).getText());
    assertTrue(driver.findElement(By.xpath("//div/div/div[2]")).getText().matches("^exact:Do you really want to delete these resources [\\s\\S]$"));
    driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
    assertEquals("prop4", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("STRING", driver.findElement(By.xpath("//td[3]/span")).getText());
    // 1.0.0.18a
    driver.findElement(By.xpath("//li[2]/button")).click();
    assertEquals("Confirmation required", driver.findElement(By.cssSelector("h4.modal-title.ng-binding")).getText());
    assertTrue(driver.findElement(By.xpath("//div/div/div[2]")).getText().matches("^exact:Do you really want to delete this resource [\\s\\S]$"));
    driver.findElement(By.xpath("(//button[@type='button'])[10]")).click();
    assertEquals("prop4", driver.findElement(By.xpath("//td[2]/span")).getText());
    assertEquals("STRING", driver.findElement(By.xpath("//td[3]/span")).getText());
    // 1.0.0.19
    assertEquals("Edit", driver.findElement(By.cssSelector("li > button.btn")).getText());
    driver.findElement(By.cssSelector("li > button.btn")).click();
    assertEquals("Update", driver.findElement(By.xpath("//button[@type='submit']")).getText());
    driver.findElement(By.name("uniqueName")).clear();
    driver.findElement(By.name("uniqueName")).sendKeys("prop1");
    assertEquals("Cancel", driver.findElement(By.cssSelector("button.btn.btn-default")).getText());
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("prop1", driver.findElement(By.xpath("//td[2]/span")).getText());
    // 1.0.0.19a
    driver.findElement(By.cssSelector("li > button.btn")).click();
    driver.findElement(By.name("uniqueName")).clear();
    driver.findElement(By.name("uniqueName")).sendKeys("");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Error", driver.findElement(By.cssSelector("h4.modal-title.text-danger > span.ng-binding")).getText());
    assertEquals("Name is mandatory", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[9]")).click();
    driver.findElement(By.name("uniqueName")).click();
    driver.findElement(By.name("uniqueName")).clear();
    driver.findElement(By.name("uniqueName")).sendKeys("prop5");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Error", driver.findElement(By.cssSelector("h4.modal-title.text-danger > span.ng-binding")).getText());
    assertEquals("Property with name prop5 is already existing", driver.findElement(By.xpath("//div/div/div[2]")).getText());
    driver.findElement(By.xpath("(//button[@type='button'])[9]")).click();
    assertEquals("prop1", driver.findElement(By.xpath("//td[2]/span")).getText());
    // 1.0.0.20 preparation
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.xpath("//tr[4]/td[5]/ul/li/button")).click();
    driver.findElement(By.xpath("//li[3]/button")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
    driver.findElement(By.xpath("//li[3]/button")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
    // 1.0.0.20
    driver.findElement(By.linkText("Icons")).click();
    driver.findElement(By.cssSelector("li.dropdown.open > ul.dropdown-menu > li > a")).click();
    assertEquals("advocacy", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.linkText("Name")).click();
    assertEquals("transition_site", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.linkText("Name")).click();
    assertEquals("advocacy", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    // 1.0.0.21
    driver.findElement(By.linkText("Linked libraries")).click();
    driver.findElement(By.linkText("Linked libraries")).click();
    assertEquals("idp_refugee_camp", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    assertEquals("permanent_camp", driver.findElement(By.xpath("//tr[3]/td[2]")).getText());
    driver.findElement(By.xpath("//li[2]/button")).click();
    assertEquals("Confirmation required", driver.findElement(By.cssSelector("h4.modal-title.ng-binding")).getText());
    assertTrue(driver.findElement(By.xpath("//div/div/div[2]")).getText().matches("^exact:Do you really want to delete this resource [\\s\\S]$"));
    driver.findElement(By.xpath("(//button[@type='button'])[3]")).click();
    assertEquals("permanent_camp", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    driver.findElement(By.cssSelector("input.ng-pristine.ng-valid")).click();
    driver.findElement(By.cssSelector("button.btn.btn-delete")).click();
    assertEquals("Confirmation required", driver.findElement(By.cssSelector("h4.modal-title.ng-binding")).getText());
    assertTrue(driver.findElement(By.xpath("//div/div/div[2]")).getText().matches("^exact:Do you really want to delete these resources [\\s\\S]$"));
    driver.findElement(By.xpath("(//button[@type='button'])[3]")).click();
    assertEquals("assessment", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    // 1.0.0.21a
    driver.findElement(By.linkText("Libraries")).click();
    driver.findElement(By.linkText("List")).click();
    driver.findElement(By.xpath("//tr[4]/td[5]/ul/li/button")).click();
    driver.findElement(By.cssSelector("input.ng-pristine.ng-valid")).click();
    driver.findElement(By.xpath("//li[3]/button")).click();
    driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
    driver.findElement(By.linkText("Icons")).click();
    driver.findElement(By.cssSelector("li.dropdown.open > ul.dropdown-menu > li > a")).click();
    driver.findElement(By.linkText("Linked libraries")).click();
    driver.findElement(By.linkText("Linked libraries")).click();
    driver.findElement(By.xpath("//li[2]/button")).click();
    assertEquals("Confirmation required", driver.findElement(By.cssSelector("h4.modal-title.ng-binding")).getText());
    assertTrue(driver.findElement(By.xpath("//div/div/div[2]")).getText().matches("^exact:Do you really want to delete this resource [\\s\\S]$"));
    driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
    assertEquals("registration", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    // 1.0.0.22
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    driver.findElement(By.id("displayName")).clear();
    driver.findElement(By.id("displayName")).sendKeys("1");
    driver.findElement(By.cssSelector("a.anchor.anchor-SW")).click();
    //driver.findElement(By.id("largeIconFile")).clear();
    driver.findElement(By.id("largeIconFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\100px\\Accident.png");
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    assertEquals("1", driver.findElement(By.cssSelector("td.ng-binding")).getText());
    assertEquals("SW", driver.findElement(By.xpath("//tr[2]/td[3]")).getText());
    // 1.0.0.22b
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    driver.findElement(By.id("displayName")).clear();
    driver.findElement(By.id("displayName")).sendKeys("2");
    driver.findElement(By.cssSelector("a.anchor.anchor-NW")).click();
    driver.findElement(By.cssSelector("a.anchor.anchor-N")).click();
    //driver.findElement(By.id("largeIconFile")).clear();
    driver.findElement(By.id("largeIconFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\100px\\invalid.png");
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    assertEquals("Invalid size for large icon file (must be 100px by 100px)", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    driver.findElement(By.cssSelector("button.btn.btn-add")).click();
    assertEquals("Display name and anchor are mandatory", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    // 1.0.0.23
    driver.findElement(By.linkText("Icons")).click();
    driver.findElement(By.cssSelector("li.dropdown.open > ul.dropdown-menu > li > a")).click();
    driver.findElement(By.xpath("//tr[3]/td[6]/ul/li/button")).click();
    driver.findElement(By.id("displayName")).clear();
    driver.findElement(By.id("displayName")).sendKeys("2");
    driver.findElement(By.cssSelector("a.anchor.anchor-W")).click();
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("2", driver.findElement(By.xpath("//tr[3]/td[2]")).getText());
    assertEquals("W", driver.findElement(By.xpath("//tr[3]/td[3]")).getText());
    // 1.0.0.23a
    driver.findElement(By.xpath("//tr[3]/td[6]/ul/li/button")).click();
    driver.findElement(By.id("displayName")).clear();
    driver.findElement(By.id("displayName")).sendKeys("");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("Display name and anchor are mandatory", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    driver.findElement(By.xpath("(//input[@name='iconChoice'])[2]")).click();
    //driver.findElement(By.id("largeIconFile")).clear();
    driver.findElement(By.id("largeIconFile")).sendKeys("M:\\PROJECTS-CE\\C\\PSSU\\1200_MASH_Libraries\\selenium\\100px\\invalid.png");
    driver.findElement(By.cssSelector("button.btn")).click();
    assertEquals("Invalid size for large icon file (must be 100px by 100px)", driver.findElement(By.cssSelector("div.alert.alert-danger")).getText());
    // 1.0.0.24
    driver.findElement(By.linkText("Icons")).click();
    driver.findElement(By.cssSelector("li.dropdown.open > ul.dropdown-menu > li > a")).click();
    driver.findElement(By.xpath("//td[5]/a/img")).click();
    assertEquals("1", driver.findElement(By.cssSelector("h3")).getText());
    assertEquals("LARGE", driver.findElement(By.cssSelector("td")).getText());
    assertEquals("MEDIUM", driver.findElement(By.xpath("//td[2]")).getText());
    assertEquals("SMALL", driver.findElement(By.xpath("//td[3]")).getText());
    assertEquals("TINY", driver.findElement(By.xpath("//td[4]")).getText());

  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    final String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(final By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (final NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (final NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      final Alert alert = driver.switchTo().alert();
      final String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
