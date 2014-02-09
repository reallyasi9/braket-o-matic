package net.exclaimindustries.paste.braket.shared;

/**
 * Lets the receiver know that a failure happened on login.
 * 
 * @author paste
 * 
 */
public class LogInException extends Exception {

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
