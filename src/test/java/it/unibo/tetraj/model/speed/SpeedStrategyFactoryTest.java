package it.unibo.tetraj.model.speed;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Unit tests for SpeedStrategyFactory. */
class SpeedStrategyFactoryTest {

  @Test
  @DisplayName("should create ModernSpeedStrategy when strategy name is modern")
  void shouldCreateModernSpeedStrategyForModern() {
    // Arrange
    final String strategyName = "modern";

    // Act
    final SpeedStrategy result = SpeedStrategyFactory.create(strategyName);

    // Assert
    assertNotNull(result);
    assertInstanceOf(ModernSpeedStrategy.class, result);
  }

  @Test
  @DisplayName("should create ClassicSpeedStrategy when strategy name is classic")
  void shouldCreateClassicSpeedStrategyForClassic() {
    // Arrange
    final String strategyName = "classic";

    // Act
    final SpeedStrategy result = SpeedStrategyFactory.create(strategyName);

    // Assert
    assertNotNull(result);
    assertInstanceOf(ClassicSpeedStrategy.class, result);
  }

  @Test
  @DisplayName("should create ClassicSpeedStrategy when strategy name is unknown")
  void shouldCreateClassicSpeedStrategyForUnknown() {
    // Arrange
    final String strategyName = "unknown-strategy";

    // Act
    final SpeedStrategy result = SpeedStrategyFactory.create(strategyName);

    // Assert
    assertNotNull(result);
    assertInstanceOf(ClassicSpeedStrategy.class, result);
  }

  @Test
  @DisplayName("should create ClassicSpeedStrategy when strategy name is null")
  void shouldCreateClassicSpeedStrategyForNull() {
    // Arrange & Act
    final SpeedStrategy result = SpeedStrategyFactory.create((String) null);

    // Assert
    assertNotNull(result);
    assertInstanceOf(ClassicSpeedStrategy.class, result);
  }

  @Test
  @DisplayName("should handle case insensitive strategy names")
  void shouldHandleCaseInsensitiveStrategyNames() {
    // Arrange & Act & Assert
    assertInstanceOf(ModernSpeedStrategy.class, SpeedStrategyFactory.create("MODERN"));
    assertInstanceOf(ModernSpeedStrategy.class, SpeedStrategyFactory.create("Modern"));
    assertInstanceOf(ClassicSpeedStrategy.class, SpeedStrategyFactory.create("CLASSIC"));
    assertInstanceOf(ClassicSpeedStrategy.class, SpeedStrategyFactory.create("Classic"));
  }

  @Test
  @DisplayName("should handle strategy names with whitespace")
  void shouldHandleStrategyNamesWithWhitespace() {
    // Arrange & Act & Assert
    assertInstanceOf(ModernSpeedStrategy.class, SpeedStrategyFactory.create("  modern  "));
    assertInstanceOf(ClassicSpeedStrategy.class, SpeedStrategyFactory.create("  classic  "));
  }

  @Test
  @DisplayName("should create strategy from default configuration")
  void shouldCreateStrategyFromDefaultConfiguration() {
    // Act
    final SpeedStrategy result = SpeedStrategyFactory.create();

    // Assert
    assertNotNull(result);
  }
}
