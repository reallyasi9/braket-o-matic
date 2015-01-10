package net.exclaimindustries.paste.braket.shared;

import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketTeam;

/**
 * An interface that provides a method to calculate the probability that a
 * certain result happened in a Game.
 * 
 * @author paste
 *
 */
public interface ResultProbabilityCalculator {

  /**
   * Given an ordered result from a Game, calculate the probability that it
   * happened.
   * 
   * @param result
   *          The ordered outcome from a Game (with the winning Team first,
   *          second place second, and so on).
   * @return The a priori calculated probability that this result would occur.
   */
  public double probabilityOf(List<BraketTeam> result);

}
