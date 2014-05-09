package lu.hitec.pssu.melm.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MELMResourceTest extends JerseyTest {
  
  @Test
  public void testHelloWorld() {
    final Response response = target("/rest/hello/world").request(MediaType.APPLICATION_JSON_TYPE).get();
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
  }

  @Override
  protected Application configure() {
    return new MyResourceConfig();
  }

}
