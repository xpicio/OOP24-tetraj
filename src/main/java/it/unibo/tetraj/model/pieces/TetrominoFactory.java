package it.unibo.tetraj.model.pieces;

import it.unibo.tetraj.model.pieces.selection.PieceSelectionStrategy;
import java.util.Objects;

/**
 * Factory class for creating tetromino pieces. Handles random piece generation and spawn position
 * calculation.
 */
public final class TetrominoFactory {
  private PieceSelectionStrategy strategy;

  public TetrominoFactory(final PieceSelectionStrategy strategy) {
    this.strategy = Objects.requireNonNull(strategy);
  }

  public AbstractTetromino<?> create() {
    final Class<? extends AbstractTetromino<?>> clazz = strategy.next();
    return TetrominoRegistry.getInstance().create(clazz, 0, 0);
  }
}
