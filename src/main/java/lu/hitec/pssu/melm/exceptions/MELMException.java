package lu.hitec.pssu.melm.exceptions;

public class MELMException extends Exception {

  private static final long serialVersionUID = 4236274253658336599L;

  public MELMException(final String msg) {
    super(msg);
  }
  
  public MELMException(final String msg, final Exception e) {
    super(msg, e);
  }
}
