package lu.hitec.pssu.melm.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class MyResourceConfig extends ResourceConfig {

  /**
   * The order is important with ExceptionMapper first.
   */
  public MyResourceConfig() {
    super();
    register(AuthenticationExceptionMapper.class);
    register(AuthenticationFilter.class);
    register(JacksonFeature.class);
    register(MultiPartFeature.class);
    register(LoginResource.class);
    register(LogoutResource.class);
    register(MELMResource.class);
  }
}
