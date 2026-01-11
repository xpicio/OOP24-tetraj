package it.unibo.tetraj.model.piece.selection;

import it.unibo.tetraj.util.ApplicationProperties;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import java.util.Locale;

/**
 * Factory for creating piece selection strategies based on application configuration. Supports both
 * modern 7-bag system and classic pure random selection.
 */
public final class PieceSelectionFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(PieceSelectionFactory.class);
  private static final String PROPERTY_KEY = "game.pieceSelection";
  private static final String DEFAULT_STRATEGY = "random";

  /** Private constructor to prevent instantiation. */
  private PieceSelectionFactory() {
    throw new UnsupportedOperationException("Factory class cannot be instantiated");
  }

  /**
   * Creates a piece selection strategy based on application configuration. Reads from the property
   * "game.pieceSelection" which can be "7-bag" or "random". Defaults to bag strategy if property is
   * not set or invalid.
   *
   * @return The appropriate PieceSelectionStrategy implementation
   */
  public static PieceSelectionStrategy create() {
    final ApplicationProperties properties = ApplicationProperties.getInstance();
    final String strategyName = properties.getProperty(PROPERTY_KEY, DEFAULT_STRATEGY);
    return create(strategyName);
  }

  /**
   * Creates a piece selection strategy based on the provided strategy name.
   *
   * @param strategyName The strategy name ("7-bag", "random", etc.)
   * @return The appropriate PieceSelectionStrategy implementation
   */
  public static PieceSelectionStrategy create(final String strategyName) {
    final String strategyNameToLoad = (strategyName == null) ? DEFAULT_STRATEGY : strategyName;
    final PieceSelectionStrategy pieceSelectionStrategy;

    switch (strategyNameToLoad.toLowerCase(Locale.ROOT).trim()) {
      case "7-bag":
        pieceSelectionStrategy = new BagRandomizerStrategy();
        break;

      case "random":
        pieceSelectionStrategy = new RandomStrategy();
        break;

      default:
        pieceSelectionStrategy = new RandomStrategy();
        LOGGER.warn(
            "Unknown piece selection strategy '{}', using default: {}",
            strategyName,
            pieceSelectionStrategy.getClass().getSimpleName());
        return pieceSelectionStrategy;
    }
    LOGGER.info(
        "Using {} piece selection strategy", pieceSelectionStrategy.getClass().getSimpleName());
    return pieceSelectionStrategy;
  }
}
