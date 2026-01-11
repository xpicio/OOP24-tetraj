package it.unibo.tetraj.model.speed;

import it.unibo.tetraj.util.ApplicationProperties;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import java.util.Locale;

/**
 * Factory for creating speed strategies based on application configuration. Reads the strategy type
 * from application properties and instantiates the appropriate implementation.
 */
public final class SpeedStrategyFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpeedStrategyFactory.class);
  private static final String PROPERTY_KEY = "game.speedStrategy";
  private static final String DEFAULT_STRATEGY = "classic";

  /** Private constructor to prevent instantiation. */
  private SpeedStrategyFactory() {
    throw new UnsupportedOperationException("Factory class cannot be instantiated");
  }

  /**
   * Creates a speed strategy based on application configuration. Reads from the property
   * "game.speedStrategy" which can be "modern" or "classic". Defaults to classic strategy if
   * property is not set or invalid.
   *
   * @return The appropriate SpeedStrategy implementation
   */
  public static SpeedStrategy create() {
    final ApplicationProperties properties = ApplicationProperties.getInstance();
    final String strategyName = properties.getProperty(PROPERTY_KEY, DEFAULT_STRATEGY);
    return create(strategyName);
  }

  /**
   * Creates a speed strategy based on the provided strategy name.
   *
   * @param strategyName The strategy name ("modern" or "classic")
   * @return The appropriate SpeedStrategy implementation
   */
  public static SpeedStrategy create(final String strategyName) {
    final String strategyNameToLoad = (strategyName == null) ? DEFAULT_STRATEGY : strategyName;
    final SpeedStrategy speedStrategy;

    switch (strategyNameToLoad.toLowerCase(Locale.ROOT).trim()) {
      case "modern":
        speedStrategy = new ModernSpeedStrategy();
        break;

      case "classic":
        speedStrategy = new ClassicSpeedStrategy();
        break;

      default:
        speedStrategy = new ClassicSpeedStrategy();
        LOGGER.warn(
            "Unknown speed strategy '{}', using default: {}",
            strategyName,
            speedStrategy.getClass().getSimpleName());
        return speedStrategy;
    }
    LOGGER.info("Using {} speed strategy", speedStrategy.getClass().getSimpleName());
    return speedStrategy;
  }
}
