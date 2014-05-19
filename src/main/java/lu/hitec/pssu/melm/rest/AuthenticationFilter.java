package lu.hitec.pssu.melm.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jthai
 * @author seustachi
 */
@Component
public class AuthenticationFilter implements ContainerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

  private static final String SESSION_PARAM_UID = "SESSION_PARAM_UID";

  @Context
  private HttpServletRequest request;

  @Override
  public void filter(final ContainerRequestContext containerRequest) {
    final String path = containerRequest.getUriInfo().getPath();
    LOGGER.debug(String.format("Entering ContainerRequest.filter %s", path));
    if (path.contains("login")) {
      LOGGER.debug("Do nothing, we just display log-in page");
    } else if (path.contains("logout")) {
      LOGGER.debug("Do nothing, we are about to log-out");
    } else if (path.contains("rest")) {
      LOGGER.debug("about to check session");
      checkSecurity();
    } else {
      LOGGER.debug("Do nothing, this is not admin interface");
    }
  }

  private void checkSecurity() {
    final HttpSession session = request.getSession(false);
    if ((session != null) && (session.getAttribute(SESSION_PARAM_UID)!=null)) {
      final String uid = session.getAttribute(SESSION_PARAM_UID).toString();
      LOGGER.debug(String.format("got uid : %s in session", uid));
      request.setAttribute("uid", uid);
    } else {
      LOGGER.debug("No session, about to redirect to log-in page");

      URI newURI = null;
      try {
        newURI = new URI("login");
      } catch (final URISyntaxException e) {
        final String msg = "This exception should not appear!";
        LOGGER.warn(msg, e);
      }
      final Response response = Response.seeOther(newURI).build();

      throw new WebApplicationException(response);
    }

  }

}