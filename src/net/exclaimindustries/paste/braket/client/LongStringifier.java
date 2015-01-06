package net.exclaimindustries.paste.braket.client;

import com.googlecode.objectify.stringifier.Stringifier;

public class LongStringifier implements Stringifier<Long> {

  @Override
  public Long fromString(String arg0) {
    return Long.decode(arg0);
  }

  @Override
  public String toString(Long arg0) {
    return arg0.toString();
  }

}
