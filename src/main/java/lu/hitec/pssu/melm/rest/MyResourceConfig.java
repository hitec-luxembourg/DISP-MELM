package lu.hitec.pssu.melm.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class MyResourceConfig extends ResourceConfig {

  /**
   * The order is important with ExceptionMapper first.
   */
	public MyResourceConfig() {
		super();
		register(AuthenticationExceptionMapper.class);
		register(LoginResource.class);
		register(LogoutResource.class);
		register(MELMResource.class);
	}
}
