package net.exclaimindustries.paste.braket.shared;

/**
 * An exception to tell the recipient that a parse error has occurred.
 * 
 * @author paste
 * 
 */
public class ParseException extends Exception {

    private static final long serialVersionUID = 1L;

    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
