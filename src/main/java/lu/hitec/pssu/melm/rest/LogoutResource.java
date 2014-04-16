package lu.hitec.pssu.melm.rest;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import lu.hitec.pssu.melm.services.MELMService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/logout")
@Component
public class LogoutResource {
  // private static final Logger LOGGER = LoggerFactory.getLogger(LogoutResource.class);

  @Autowired
  private MELMService melmService;

  @Context
  private HttpServletRequest request;

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response logout(@Context final UriInfo uriInfo) {
    final HttpSession session = request.getSession(false);
    session.invalidate();
    final URI newURI = uriInfo.getBaseUriBuilder().path("/login/").build();
    return Response.seeOther(newURI).build();
  }

}