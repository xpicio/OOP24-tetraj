package it.unibo.tetraj.model.pieces.selection;

import it.unibo.tetraj.model.pieces.AbstractTetromino;
import it.unibo.tetraj.model.pieces.TetrominoRegistry;
import it.unibo.tetraj.utils.Logger;
import it.unibo.tetraj.utils.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BagRandomizerStrategy implements PieceSelectionStrategy {

  private static final Logger LOGGER = LoggerFactory.getLogger(BagRandomizerStrategy.class);
  private final List<Class<? extends AbstractTetromino<?>>> bag = new ArrayList<>();
  private final List<Class<? extends AbstractTetromino<?>>> availableTypes =
      TetrominoRegistry.getInstance().getAvailableTypes();
  private final Random random = new Random();

  @Override
  public Class<? extends AbstractTetromino<?>> next() {
    final Class<? extends AbstractTetromino<?>> currentPiece;

    if (this.bag.isEmpty()) {
      LOGGER.info("Bag empty, shuffling {} pieces", availableTypes.size());
      this.bag.addAll(this.availableTypes);
      Collections.shuffle(this.bag, this.random);
    }
    currentPiece = this.bag.remove(0);
    LOGGER.info("Spawning piece {}", currentPiece.getSimpleName());
    return currentPiece;
  }
}
