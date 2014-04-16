package lu.hitec.pssu.melm.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import lu.hitec.pssu.melm.exceptions.AuthenticationException;

public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

	@Override
	public Response toResponse(final AuthenticationException exception) {
		return Response.status(Status.UNAUTHORIZED).entity("Invalid login/password provided").type(MediaType.TEXT_PLAIN).build();
	}

}