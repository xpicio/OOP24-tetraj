package it.unibo.tetraj.model.speed;

/**
 * Strategy interface for game speed progression. Defines how piece fall speed changes with level
 * advancement.
 */
public interface SpeedStrategy {

  /**
   * Gets the fall speed for a given level.
   *
   * @param level The current game level (0-based)
   * @return Fall speed in milliseconds per row
   */
  double getFallSpeed(int level);

  /**
   * Gets the soft drop speed for a given level. Typically much faster than normal fall speed.
   *
   * @param level The current game level (0-based)
   * @return Soft drop speed in milliseconds per row
   */
  double getSoftDropSpeed(int level);
}
