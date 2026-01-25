package it.unibo.tetraj.model.speed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Unit tests for ModernSpeedStrategy (Tetris Guideline formula). */
class ModernSpeedStrategyTest {

  private static final double DELTA = 0.01;
  private static final double BASE_SPEED = 1000.0;
  private static final double MIN_FALL_SPEED = 1.0;
  private static final double MIN_SOFT_DROP = 50.0;
  private static final int TEST_HIGH_LEVEL = 30;
  private static final int TEST_MEDIUM_LEVEL = 50;
  private static final int TEST_VERY_HIGH_LEVEL = 100;
  private static final int TEST_NEGATIVE_LEVEL = -100;
  private ModernSpeedStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new ModernSpeedStrategy();
  }

  @Test
  @DisplayName("should return 1000ms at level 0")
  void shouldReturn1000msAtLevel0() {
    // Act
    final double result = strategy.getFallSpeed(0);

    // Assert
    assertEquals(BASE_SPEED, result, DELTA);
  }

  @Test
  @DisplayName("should return 1000ms for negative levels")
  void shouldReturn1000msForNegativeLevels() {
    // Act & Assert
    assertEquals(BASE_SPEED, strategy.getFallSpeed(-1), DELTA);
    assertEquals(BASE_SPEED, strategy.getFallSpeed(TEST_NEGATIVE_LEVEL), DELTA);
  }

  @Test
  @DisplayName("speed should decrease monotonically with level")
  void speedShouldDecreaseMonotonicallyWithLevel() {
    // Act & Assert
    for (int level = 1; level <= TEST_HIGH_LEVEL; level++) {
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
  @DisplayName("speed should not go below minimum")
  void speedShouldNotGoBelowMinimum() {
    // Act & Assert
    assertTrue(strategy.getFallSpeed(TEST_HIGH_LEVEL) >= MIN_FALL_SPEED);
    assertTrue(strategy.getFallSpeed(TEST_MEDIUM_LEVEL) >= MIN_FALL_SPEED);
    assertTrue(strategy.getFallSpeed(TEST_VERY_HIGH_LEVEL) >= MIN_FALL_SPEED);
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
  @DisplayName("soft drop should be 20x faster than normal at low levels")
  void softDropShouldBe20xFasterAtLowLevels() {
    // Arrange
    final double fallSpeed = strategy.getFallSpeed(0);
    final double expectedSoftDrop = Math.max(MIN_SOFT_DROP, fallSpeed / 20.0);

    // Act
    final double result = strategy.getSoftDropSpeed(0);

    // Assert
    assertEquals(expectedSoftDrop, result, DELTA);
  }

  @Test
  @DisplayName("soft drop should respect minimum at medium levels")
  void softDropShouldRespectMinimumAtMediumLevels() {
    // Act
    final double softDrop = strategy.getSoftDropSpeed(10);

    // Assert
    assertTrue(softDrop >= MIN_SOFT_DROP, "Soft drop should be at least 50ms");
  }

  @Test
  @DisplayName("speed at level 15 should be significantly faster than level 0")
  void speedAtLevel15ShouldBeSignificantlyFaster() {
    // Act
    final double level0Speed = strategy.getFallSpeed(0);
    final double level15Speed = strategy.getFallSpeed(15);

    // Assert - level 15 should be at least 10x faster
    assertTrue(
        level15Speed < level0Speed / 10,
        String.format(
            "Level 15 (%.2fms) should be at least 10x faster than level 0 (%.2fms)",
            level15Speed, level0Speed));
  }
}
