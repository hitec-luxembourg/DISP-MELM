package lu.hitec.pssu.melm.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class MyResourceConfig extends ResourceConfig {

	public MyResourceConfig() {
		super();
		register(LoginResource.class);
    register(AuthenticationExceptionMapper.class);
	}
}
