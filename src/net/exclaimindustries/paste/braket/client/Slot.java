package net.exclaimindustries.paste.braket.client;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

/**
 * A Slot holds a Team within a Game. It also holds information about what Game
 * fills the Slot.
 * 
 * @author paste
 *
 */
@Entity
@Cache
public class Slot implements Comparable<Slot>, IsSerializable {

  @Id
  private Long id = null;

  @Parent
  private Ref<Game> parentGame = null;

  /**
   * The Team that lives in this Slot (can be null)
   */
  @Load
  private Ref<BraketTeam> team = Ref.create(null);

  /**
   * The Game that feeds into this Slot (can be null if this is a play-in game)
   */
  @Load
  private Ref<Game> playInGame = Ref.create(null);

  /**
   * The score given to the team in this slot
   */
  private Integer score = null;

  /**
   * The tiebreaker value, in case scores are the same, for ranking purposes
   * within a game.
   */
  private double tiebreaker = 0.;

  @SuppressWarnings("unused")
  private Slot() {
  }

  public Slot(Game parentGame) {
    this.parentGame = Ref.create(parentGame);
  }

  public Slot(Game parentGame, BraketTeam team, Game playInGame) {
    this.parentGame = Ref.create(parentGame);
    this.team = Ref.create(team);
    this.playInGame = Ref.create(playInGame);
  }

  public Slot(Game parentGame, BraketTeam team) {
    this.parentGame = Ref.create(parentGame);
    this.team = Ref.create(team);
  }

  public Slot(Game parentGame, Game playInGame) {
    this.parentGame = Ref.create(parentGame);
    this.playInGame = Ref.create(playInGame);
  }

  public Optional<BraketTeam> getTeam() {
    return Optional.of(team.get());
  }

  public Optional<Game> getPlayInGame() {
    return Optional.of(playInGame.get());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Game getGameContainer() {
    return parentGame.get();
  }

  public Optional<Integer> getScore() {
    return Optional.of(score);
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public double getTiebreaker() {
    return tiebreaker;
  }

  public void setTiebreaker(double tiebreaker) {
    this.tiebreaker = tiebreaker;
  }

  @Override
  public int compareTo(Slot that) {
    if (that == null) {
      throw new NullPointerException();
    }
    int compareScore = Integer.compare(this.getScore().or(0), that.getScore()
        .or(0));
    return (compareScore == 0) ? Double.compare(this.tiebreaker,
        that.tiebreaker) : compareScore;
  }

}
