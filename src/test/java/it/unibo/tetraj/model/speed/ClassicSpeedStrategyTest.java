package it.unibo.tetraj.model.speed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Unit tests for ClassicSpeedStrategy (NES Tetris timing). */
class ClassicSpeedStrategyTest {

  private static final double MS_PER_FRAME = 1000.0 / 60.0;
  private static final double DELTA = 0.01;
  private static final int FRAMES_LEVEL_0 = 48;
  private static final int KILL_SCREEN_LEVEL = 29;
  private static final int TEST_HIGH_LEVEL = 30;
  private static final int TEST_VERY_HIGH_LEVEL = 100;
  private static final int TEST_NEGATIVE_LEVEL = -100;
  private static final double SOFT_DROP_MS = 33.33;
  private ClassicSpeedStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new ClassicSpeedStrategy();
  }

  @Test
  @DisplayName("should return 800ms at level 0 (48 frames)")
  void shouldReturn800msAtLevel0() {
    // Arrange
    final double expectedMs = FRAMES_LEVEL_0 * MS_PER_FRAME;

    // Act
    final double result = strategy.getFallSpeed(0);

    // Assert
    assertEquals(expectedMs, result, DELTA);
  }

  @Test
  @DisplayName("should return kill screen speed at level 29 (1 frame)")
  void shouldReturnKillScreenSpeedAtLevel29() {
    // Arrange
    final double expectedMs = 1 * MS_PER_FRAME;

    // Act
    final double result = strategy.getFallSpeed(KILL_SCREEN_LEVEL);

    // Assert
    assertEquals(expectedMs, result, DELTA);
  }

  @Test
  @DisplayName("should cap speed at level 29 for higher levels")
  void shouldCapSpeedAtLevel29ForHigherLevels() {
    // Act
    final double level29Speed = strategy.getFallSpeed(KILL_SCREEN_LEVEL);

    // Assert
    assertEquals(level29Speed, strategy.getFallSpeed(TEST_HIGH_LEVEL), DELTA);
    assertEquals(level29Speed, strategy.getFallSpeed(TEST_VERY_HIGH_LEVEL), DELTA);
  }

  @Test
  @DisplayName("should handle negative levels as level 0")
  void shouldHandleNegativeLevelsAsLevel0() {
    // Act
    final double level0Speed = strategy.getFallSpeed(0);

    // Assert
    assertEquals(level0Speed, strategy.getFallSpeed(-1), DELTA);
    assertEquals(level0Speed, strategy.getFallSpeed(TEST_NEGATIVE_LEVEL), DELTA);
  }

  @Test
  @DisplayName("speed should increase monotonically with level")
  void speedShouldIncreaseMonotonicallyWithLevel() {
    // Act & Assert
    for (int level = 1; level <= KILL_SCREEN_LEVEL; level++) {
      final double previousSpeed = strategy.getFallSpeed(level - 1);
      final double currentSpeed = strategy.getFallSpeed(level);
      assertTrue(
          currentSpeed <= previousSpeed,
          String.format(
              "Level %d speed (%.2f) should be <= level %d speed (%.2f)",
              level, currentSpeed, level - 1, previousSpeed));
    }
  }

  @Test
  @DisplayName("soft drop should never be slower than normal fall")
  void softDropShouldNeverBeSlowerThanNormalFall() {
    // Act & Assert
    for (int level = 0; level <= TEST_HIGH_LEVEL; level++) {
      final double fallSpeed = strategy.getFallSpeed(level);
      final double softDropSpeed = strategy.getSoftDropSpeed(level);
      assertTrue(
          softDropSpeed <= fallSpeed,
          String.format(
              "Soft drop (%.2f) should be <= fall speed (%.2f) at level %d",
              softDropSpeed, fallSpeed, level));
    }
  }

  @Test
  @DisplayName("soft drop should be fixed at 2 frames for slow levels")
  void softDropShouldBeFixedAt2FramesForSlowLevels() {
    // Act & Assert (levels 0-19 have fall speed > 33.33ms)
    assertEquals(SOFT_DROP_MS, strategy.getSoftDropSpeed(0), DELTA);
    assertEquals(SOFT_DROP_MS, strategy.getSoftDropSpeed(10), DELTA);
  }
}
