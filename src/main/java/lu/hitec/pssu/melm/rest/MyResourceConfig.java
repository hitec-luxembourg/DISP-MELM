package lu.hitec.pssu.melm.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class MyResourceConfig extends ResourceConfig {

	public MyResourceConfig() {
		super();
		register(AuthenticationExceptionMapper.class);
		register(LoginResource.class);
	}
}
