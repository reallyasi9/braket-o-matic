package net.exclaimindustries.paste.braket.shared;

import com.googlecode.objectify.Ref;

public final class RefDereferencer {

  public static <TYPE> TYPE dereference(Ref<TYPE> ref) {
    return (ref == null) ? null : ref.get();
  }
  
  private RefDereferencer() {}
  
}
