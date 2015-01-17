package net.exclaimindustries.paste.braket.shared;

public class TeamNotInTournamentException extends Exception {

  private static final long serialVersionUID = 1L;

  public TeamNotInTournamentException() {
  }

  public TeamNotInTournamentException(String message) {
    super(message);
  }

  public TeamNotInTournamentException(Throwable cause) {
    super(cause);
  }

  public TeamNotInTournamentException(String message, Throwable cause) {
    super(message, cause);
  }

  public TeamNotInTournamentException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
