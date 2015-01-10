package net.exclaimindustries.paste.braket.shared;

/**
 * Lets the receiver know that a Game has not yet ended.
 * 
 * @author paste
 *
 */
public class GameNotFinalException extends Exception {

  private static final long serialVersionUID = 1L;

  public GameNotFinalException() {
    super();
  }

  public GameNotFinalException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public GameNotFinalException(String message, Throwable cause) {
    super(message, cause);
  }

  public GameNotFinalException(String message) {
    super(message);
  }

  public GameNotFinalException(Throwable cause) {
    super(cause);
  }

}
