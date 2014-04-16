package lu.hitec.pssu.melm.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import lu.hitec.pssu.melm.exceptions.AuthenticationException;
import lu.hitec.pssu.melm.services.MELMService;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/")
@Component
public class LoginResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginResource.class);

	private static String SESSION_PARAM_UID = "SESSION_PARAM_UID";

	@Autowired
	private MELMService melmService;

	@Context
	private HttpServletRequest request;

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("login/")
	@SuppressWarnings("static-method")
	public Response gotoLogin() {
		return Response.ok(new Viewable("/login")).build();
	}

	// @GET
	// @Produces(MediaType.TEXT_HTML)
	// public Response defaultPage(@Context final UriInfo uriInfo) {
	// final URI newURI = uriInfo.getBaseUriBuilder().path("/login").build();
	// return Response.seeOther(newURI).build();
	// }

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("logout/")
	public Response logout(@Context final UriInfo uriInfo) {
		final HttpSession session = request.getSession(false);
		session.invalidate();
		return buildRedirectResponse(uriInfo, "/login");
	}

	@POST
	@Path("login/")
	@Produces(MediaType.TEXT_HTML)
	@SuppressWarnings("unused")
	public Response performLogin(@FormParam("userId") @Nonnull final String userId, @FormParam("password") @Nonnull final String password, @Context final UriInfo uriInfo,
			@Context final SecurityContext sc) throws AuthenticationException, URISyntaxException {
		assert userId != null : "User id is null";
		assert password != null : "Password is null";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Entering login for user : %s", userId));
		}
		if (melmService != null) {
			final HttpSession session = request.getSession(true);
			session.setAttribute(SESSION_PARAM_UID, userId);
			// 1800 seconds = 30 minutes
			session.setMaxInactiveInterval(1800);

			// Dummy implementation of authentication.
			if ("toto".equalsIgnoreCase(userId) && "titi".equalsIgnoreCase(password)) {
				return buildRedirectResponse(uriInfo, "/start");
			} else {
				return buildRedirectResponse(uriInfo, "/login");
			}
		}
		throw new AuthenticationException();
	}

	private static Response buildRedirectResponse(@Context final UriInfo uriInfo, @Nonnull final String path) {
		final URI newURI = uriInfo.getBaseUriBuilder().path(path).build();
		return Response.seeOther(newURI).build();
	}
}