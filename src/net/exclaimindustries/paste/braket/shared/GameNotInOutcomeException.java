package net.exclaimindustries.paste.braket.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GameNotInOutcomeException extends Exception implements
    IsSerializable {

  private static final long serialVersionUID = 1L;

  public GameNotInOutcomeException() {
    super();
  }

  public GameNotInOutcomeException(String arg0, Throwable arg1,
      boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public GameNotInOutcomeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public GameNotInOutcomeException(String arg0) {
    super(arg0);
  }

  public GameNotInOutcomeException(Throwable arg0) {
    super(arg0);
  }

}
