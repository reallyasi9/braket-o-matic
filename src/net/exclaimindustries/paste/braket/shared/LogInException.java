package net.exclaimindustries.paste.braket.shared;

/**
 * Lets the receiver know that a failure happened on login.
 * 
 * @author paste
 * 
 */
public class LogInException extends Exception {

    public LogInException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

    private static final long serialVersionUID = 1L;

    public LogInException() {
    }

    public LogInException(String message) {
        super(message);
    }

    public LogInException(Throwable cause) {
        super(cause);
    }

    public LogInException(String message, Throwable cause) {
        super(message, cause);
    }

}
