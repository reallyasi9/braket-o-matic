package net.exclaimindustries.paste.braket.shared;

public class OutcomeNotPairedToTournamentException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public OutcomeNotPairedToTournamentException() {
  }

  public OutcomeNotPairedToTournamentException(String message) {
    super(message);
  }

  public OutcomeNotPairedToTournamentException(Throwable cause) {
    super(cause);
  }

  public OutcomeNotPairedToTournamentException(String message, Throwable cause) {
    super(message, cause);
  }

  public OutcomeNotPairedToTournamentException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
