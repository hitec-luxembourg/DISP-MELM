package lu.hitec.pssu.melm.exceptions;

public class AuthenticationException extends Exception {

  private static final long serialVersionUID = -5275947330818795755L;

  public AuthenticationException() {
    super();
  }
  
  public AuthenticationException(final String msg, final Exception e) {
    super(msg, e);
  }
}
