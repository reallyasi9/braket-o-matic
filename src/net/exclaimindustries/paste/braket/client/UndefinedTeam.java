package net.exclaimindustries.paste.braket.client;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Subclass;

/**
 * A datastore entity that represents a team that has not yet been defined
 * 
 * @author paste
 *
 */
@Subclass(index = true)
@Cache
public class UndefinedTeam extends Team {

  public UndefinedTeam() {
    super();
    id = 0l;
  }

  public static Team get() {
    return new UndefinedTeam();
  }

  @Override
  public Object clone() {
    return new UndefinedTeam();
  }

  @Override
  public void setName(TeamName teamName) {
    return;
  }

  @Override
  public void setPicture(String picture) {
    return;
  }

  @Override
  public void setColor(RGBAColor color) {
    return;
  }

  @Override
  public void setId(Long id) {
    return;
  }

}
